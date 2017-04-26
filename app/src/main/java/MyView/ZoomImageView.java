package MyView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import interfaces.OnItemClickListener;
import interfaces.OnclickFinish;

/**
 * 图片预览与多点触控ImageView
 * <p>
 * <p>
 * Created by wjc on 2017/3/8.
 */

public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
         ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    /**
     * 因为缩放只需在加载图片完成后进行一次，所以需要设置一个标记
     */
    private boolean mOnce;

    /**
     * 初始化时缩放的值，也就是根据图片于控件宽高计算得到的缩放比例
     */
    private float mInitScale;

    /**
     * 双击放大的的scale比例
     */
    private float mMidScale;

    /**
     * 允许放大的最大缩放比例
     */
    private float mMaxScale;


    /**
     * 缩放矩阵
     */
    private Matrix mScaleMatrix;

    /**
     * 可以获取到当前用户手势的 缩放比例
     */
    private ScaleGestureDetector mScaleGestureDetector;

//------------------自由移动所需变量
    /**
     * 记录上一次多点触控的点的数量，因为4个手指变为2个手指的时候，
     * 触控的中心可能就瞬间发生很大的跳跃变化,如果只是根据触控中心位置实时移动图片，用户体验差
     */
    private int mLastPointerCount;

    private float mLastX; //记录上次多指的中心点

    private float mlastY;

    private int mTouchSlop;

    private boolean isCanDrag;

    private RectF matrixRectF;

    private boolean isCheckLeftAndRight;

    private boolean isCheckTopAndBottom;


//-----------------双击放大与缩小


    private GestureDetector mGestureDetector;

    private boolean isAutoScale; //当前是否处在双击后的缩放过程当中







    private OnclickFinish mOnClickFinish;

    public void setmOnClickFinish(OnclickFinish mOnClickFinish) {
        this.mOnClickFinish = mOnClickFinish;
    }

    public ZoomImageView(Context context) {

        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScaleMatrix = new Matrix();

        setScaleType(ScaleType.MATRIX);//覆盖xml的scaleType 因为这里的缩放是建立在ScaleType.MATRIX的

        mScaleGestureDetector = new ScaleGestureDetector(context, this);

        setOnTouchListener(this);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onContextClick(MotionEvent e) {
                return super.onContextClick(e);
            }


            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.e("TAG","finish confirm ");

                if(mOnClickFinish!=null){
                    mOnClickFinish.onClickFinish();
                }
                return  true;

            }

            /**
             *
             * 双击的图片放大缩小，注意边界处理
             * 如果双击直接放大到目标大小，中间是没有变化过程的，会非常突兀，可以使用runnable，分成多次，延时post
             *
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                /**
                 * 当前仍在缩放 就直接return；
                 */
                if (isAutoScale) {
                    return true;
                }

                float x = e.getX();
                float y = e.getY();
                float currentScale = getScale();

                /**
                 * 两个特殊情况：
                 * 当前为 mMidScale 会缩小为mInitScale
                 * 当前为 mInitScale 会放大为mMidScale
                 * 也就是说  不会出现双击后无变化的情况。mTargetScale不会等于getScale；
                 */
                if (currentScale < mMidScale) {
//                    mScaleMatrix.postScale(mMidScale / currentScale, mMidScale / currentScale, x, y); //这是直接缩放，不可取

                    postDelayed(new AutoScaleRunnable(mMidScale, x, y), 16);
                    isAutoScale = true;


                } else {

//                    mScaleMatrix.postScale(mInitScale / currentScale, mInitScale / currentScale, x, y); //
                    postDelayed(new AutoScaleRunnable(mInitScale, x, y), 16);
                    isAutoScale = true;
                }


                return true;
            }
        });


    }


    /**
     * 自动缓慢放大与缩小
     */
    private class AutoScaleRunnable implements Runnable {
        /**
         * 缩放的目标值以及缩放的中心点位置（双击位置）
         */
        private float mTargetScale;
        private float x;
        private float y;

        private final float BIGGER = 1.07f;
        private final float SMALLER = 0.93f;

        private float tmpScale;

        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            //确定当前双击 每次的scale系数，放大还是缩小
            if (mTargetScale > getScale()) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }


        }

        @Override
        public void run() {

            /**
             * 进行缩放
             */
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScale();

            /**
             *    判断本次缩放之后如果仍未达到目标值，接着进行下一循环的缩放，判断
             */
            if (tmpScale > 1.0f && currentScale < mTargetScale || tmpScale < 1.0f && currentScale > mTargetScale) {
                postDelayed(this, 16);

            } else {
                /**
                 * 临界判定
                 *
                 * 如果本次缩放之后已经达到或者超过了目标值
                 * 那么直接缩放到目标值
                 *
                 */
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);

                isAutoScale = false;

            }


        }
    }


    /**
     * 注册与取消注册注册 OnGlobalLayoutListener
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);

    }

    /**
     * 全局布局完成后 调用以下OnGlobalLayoutListener的实现方法
     * 在这里获取控件的宽高是比较合适的
     * 获取ImageView加载完成的图片，并且比较图片大小与屏幕大小 根据结果进行缩放
     * 缩放之后，能居中显示完整的图片，并且宽或者高顶到ImageView的边
     */
    @Override
    public void onGlobalLayout() {

        if (!mOnce) {


            int width = getWidth(); //获取当前控件的宽和高
            int height = getHeight();

            Drawable d = getDrawable();
            if (d == null) {
                return;
            }

            int dw = d.getIntrinsicWidth(); //获得加载完成的图片宽高属性
            int dh = d.getIntrinsicHeight();


            float scale = 1.0f;

            //四种情况

            /**
             * 缩放策略
             */
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;

            } else if (dw < width && dh > height) {
                scale = height * 1.0f / dh;
            } else {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            mInitScale = scale;
            mMidScale = mInitScale * 2;
            mMaxScale = mInitScale * 4;

            /**
             * 将图片移动到控件的中心
             */
            int translateX = (width - dw) / 2;
            int translateY = (height - dh) / 2;


            mScaleMatrix.postTranslate(translateX, translateY);
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);

            mOnce = true;
        }


    }


    /**
     * 获取当前的总体缩放比例(在缩放矩阵中可以查到)
     */
    public float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];


    }

    /**
     * ScaleGestureDetector的三个要实现的方法
     * 监听 由nTouch传递过来的多点触控的缩放手势
     * <p>
     * 本次onScale完成后的最终总体缩放结果，就是
     * 原来的总体缩放结果getScale的值乘以本次的缩放因子scaleFactor
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {


        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();//获得当前手势的缩放比例，可能大于1 也可能小于1


        if (getDrawable() == null) {
            return true;
        }

        //注意判断在每一次调用onScare前后，总的缩放比例都应该在 mInitScale和mMaxScale 之间
        if (scale < mMaxScale && scaleFactor > 1.0f || scale > mInitScale && scaleFactor < 1.0f) {

            //在满足前面的条件下，如果在这次缩放之后 比例超出了范围  就要调整scaleFactor
            if (scale * scaleFactor < mInitScale) {
                /**
                 * 修正scaleFactor使最终的缩放结果为mInitScale，不能再小了
                 * 理论上 使scaleFactor直接等于1也可以，也就是在本次缩放会触及边界的情况下，取消本次缩放
                 * 但是如果是修正缩放结果为刚好达到边界，效果自然是最好的
                 */
                scaleFactor = mInitScale / scale; // 使最终的缩放结果为mInitScale，不能再小了


            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale; // 使最终的缩放结果为mMaxScale，不能再大了

            }
            /**
             * 后两个参数是缩放的中心点，将其设为当前缩放手势的中心点
             */
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(),
                    detector.getFocusY());

            /**
             *
             * 如果直接设置缩放中心点为手势点的话，几次缩放之后不仅图片中心会偏移，屏幕上可能还会出现留白
             * 所以需要在缩放之前 即时检测调整,并且将调整后的偏移一起post到mScaleMatrix中
             */
            checkBorderAndCenterWhenScale();

            setImageMatrix(mScaleMatrix);

        }


        return true;
    }


    /**
     * 在缩放的时候进行边界控制 以及位置控制
     * 总体思路是 图片较大的时候 处理留白 ， 图片较小的时候 处理居中
     */
    private void checkBorderAndCenterWhenScale() {


        RectF rectF = getMatrixRectF();

        //为了修正需要偏移的值
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        /**
         * 进行边界判断
         * 当图片宽度大于控件宽度，而控件左右有留白
         * 当图片高度大于控件高度，而控件上下有留白
         * 都是需要进行偏移处理覆盖留白的
         */
        if (rectF.width() >= width) {

            if (rectF.left > 0) {
                deltaX = -rectF.left;

            }
            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }

        }

        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }

        }

        /**
         * 如果图片的宽度或者高度小于控件的 则进行居中
         * 计算的偏移量的时候要注意此时的图片的边不一定与控件的左边或者上边重合（基本不会重合）
         */

        if (rectF.width() < width) {
            deltaX = width / 2f - rectF.left - rectF.width() / 2f;

        }
        if (rectF.height() < height) {
            deltaY = height / 2f - rectF.top - rectF.height() / 2f;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * RectF:保存一个矩形的left;top;right;bottom;四个参数，且都是float
     * <p>
     * 获取当前图片的矩形坐标
     */
    private RectF getMatrixRectF() {

        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d != null) {
            //设定图片在经过Matrix变换之前的Rect坐标
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

            //将rectF坐标进行矩阵变换之后的坐标重新赋值给rectF
            matrix.mapRect(rectF);
        }

        return rectF;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }


    /**
     * 实现OnTouchListener 并且监听onTouchListener，然后在实现方法onTouch中 将触摸时事件交给ScaleGestureDetector来处理
     * 毕竟是系统的多点触控处理API，很专业 哈哈
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //优先处理双击，以免在双击的时候产生缩放或者移动
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }


        //将多点的scale移交给mScaleGestureDetector
        mScaleGestureDetector.onTouchEvent(event);


        /**
         * 以下是平移操作
         */
        float x = 0;//保存多点触控的中心点
        float y = 0;

        //获取当前的触控点数
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);

        }
        x /= pointerCount;//算出当前的中心点位置
        y /= pointerCount;


        /**
         * isCanDrag为false有两种情况，一种是触控点数发生了变化，我们只是记录位置
         * 另一种是位移太小
         * 这两种情况 我们都不会postTranslate
         *
         */
        if (mLastPointerCount != pointerCount) {
            isCanDrag = false;
            mLastX = x;
            mlastY = y;
        }
        mLastPointerCount = pointerCount;
        RectF rect= getMatrixRectF();
        switch (event.getAction()) {

            /**
             * 在 down和move的时候进行判断 如果当前图片已经经过人为放大了，那么就请求父控件（Viewpager）
             * 不要拦截touch事件，让图片可以移动查看 而不会滑动到viewpager的下一张
             *
             * 这里加 0.01 是因为避免浮点数计算丢失精确度 确保在mInitScale的情况下可以翻到下一张
             */
            case MotionEvent.ACTION_DOWN:

              if(rect.width()>getWidth()+0.01||rect.height()>getHeight()+0.01){

                  if(getParent()instanceof ViewPager)
                  getParent().requestDisallowInterceptTouchEvent(true);
            }


                break;

            case MotionEvent.ACTION_MOVE:

                if(rect.width()>getWidth()+0.01||rect.height()>getHeight()+0.01){
                    if(getParent()instanceof ViewPager)

                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                float dx = x - mLastX;
                float dy = y - mlastY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }
                if (isCanDrag) {

                    RectF rectF = getMatrixRectF();
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;

                        /**
                         *  图片宽度小于控件宽度，不允许横向移动
                         *  既然不能移动，也就不需要移动时边界控制
                         */

                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        //图片高度小于控件高度，不允许纵向移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }

                        mScaleMatrix.postTranslate(dx, dy);

                        /**
                         * 平移的时候也是需要边界控制的
                         */
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);

                    }


                }

                mLastX = x;//移动过程中不断记录上一次的xy
                mlastY = y;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                mLastPointerCount = 0;
                break;

            default:


                break;

        }


        return true;
    }

    /**
     * 移动时 进行边界控制
     */
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;


        int width = getWidth();
        int height = getHeight();


        /**
         * 左侧有留白，并且此时图片宽度是大于控件宽度的
         */
        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;

        }

        if (rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }

        if (rectF.top > 0 && isCheckTopAndBottom) {
            deltaY = -rectF.top;

        }

        if (rectF.bottom < height && isCheckTopAndBottom) {

            deltaY = height - rectF.bottom;
        }


        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 判断偏移量是否足以触发move
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}

package MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import jintong.museum2.R;

/**
 * 主页底部tab 的四个Item的
 * 自定义View
 *
 * 1、attr.xml 自定义属性
 * 2、布局文件中使用
 * 3、构造方法中获取自定义属性
 * 4、onMeasure
 * 5、onDraw
 *
 */


/**
 * Created by wjc on 2017/2/8.
 */
public class ChangeColorView extends View{

    //自定义属性
    private int mColor=0xFF45c01a;
    private Bitmap mIconBitmap;
    private String mText;
    //单位转换为px
    private int mTextSize=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12,getResources().getDisplayMetrics());

    //绘图属性
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;
    private float mAlpha; //用于颜色的变化

    private Rect mIconRect; //图标的所占区域
    private Rect mTextBound;//text的所占区域
    private Paint mTextPaint;



    //两个保存状态的key
    private static final String INSTANCE_STATUS="instance_status";
    private static final String STATUS_ALPHA="status_alpha";


    /**
     * Activity因为旋转屏幕等因素意外停止时，如果重新启动
     * View需要恢复至之前的状态
     *
     *
     */
    //保存状态
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE_STATUS,super.onSaveInstanceState());//这个key用于保存原来（父View）已有的状态
        bundle.putFloat(STATUS_ALPHA,mAlpha);//这个key用于保存当前View的alpha值
        return bundle;
    }

    //恢复状态
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
      //先判断是否是我们自己传的Bundle
        if(state instanceof Bundle){
            Bundle bundle= (Bundle) state;
             mAlpha=bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));//读取原先父View需要读取的状态

            return;
        }

        super.onRestoreInstanceState(state);


    }



    //构造方法依次调用多参构造方法
    public ChangeColorView(Context context) {
        this(context,null);
    }

    public ChangeColorView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }


    /**
     *
     * 获得自定义的的属性值
     */
    public ChangeColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
      //获得自定义属性的属性值（设置在xml文件中）
        TypedArray array=context.getTheme().obtainStyledAttributes(attrs, R.styleable.ChangeColorView,defStyleAttr,0);

        //从TypedArray中将需要的属性值取出
        int n=array.getIndexCount();
        for (int i=0;i<n;i++){
            int attr=array.getIndex(i);
            switch (attr){
                case R.styleable.ChangeColorView_color_:
                    mColor=array.getColor(attr,0xFF45c01a);
                    break;
                case R.styleable.ChangeColorView_icon_:
                    BitmapDrawable drawable= (BitmapDrawable) array.getDrawable(attr);
                    mIconBitmap=drawable.getBitmap();
                    break;
                case R.styleable.ChangeColorView_text:
                    mText=array.getString(attr);
                    break;
                case R.styleable.ChangeColorView_text_size:
                    mTextSize= (int) array.getDimension(attr,
                           TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12,getResources().getDisplayMetrics())
                    );
                    break;
                default:
                    break;

            }


        }
        array.recycle();

        mTextBound=new Rect();
        mTextPaint=new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xffa9a9a9);
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);





    }



    /**
     * 这里icon的边长应该是
     *
     * view的宽度-leftpadding-rightpadding
     * view的高度-toppadding-bottompadding-mTextBound.height
     * 两者中的较小值
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int iconWidth=Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(),
                getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height());

        // left right 两个数值是mIconRect左上角点的坐标
        int left=getMeasuredWidth()/2-iconWidth/2;
        int top=getMeasuredHeight()/2-(mTextBound.height()+iconWidth)/2;



        mIconRect=new Rect(left+5,top+5,left+iconWidth-5,top+iconWidth-5);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画出图标
        canvas.drawBitmap(mIconBitmap,null,mIconRect,null);

        //mAlpha 从0.0到1.0 ， alpha 为0到255
        int alpha= (int) Math.ceil(255*mAlpha);
        //内存去准备mBitmap，setAlpha，纯色，xfermode，图标
        setupTargetBitmap(alpha);


        //绘制原文本 绘制变色文本
        drawSourceText(canvas,alpha);

        drawTargetText(canvas,alpha);


        canvas.drawBitmap(mBitmap,0,0,null);
        
    }



    /**
     * 绘制原文本
     */
    private void drawSourceText(Canvas canvas,int alpha ) {
        mTextPaint.setColor(0xff242525);
        mTextPaint.setAlpha(255-alpha);
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y=mIconRect.bottom+mTextBound.height()+4;
        mCanvas.drawText(mText,x,y,mTextPaint);
    }


    /**
     * 绘制可变色的文本
     */
    private void drawTargetText(Canvas canvas,int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y=mIconRect.bottom+mTextBound.height()+4;
        mCanvas.drawText(mText,x,y,mTextPaint);
    }

    /**
     * 在内存中绘制可变色的Icon
     * @param alpha
     */
    private void setupTargetBitmap(int alpha) {
        mBitmap=Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas(mBitmap);
        mPaint=new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);

        mCanvas.drawRect(mIconRect,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap,null,mIconRect,mPaint);

    }
    public void setIconAlpha(float alpha){
        this.mAlpha=alpha;
        invalidateView();

    }

    /**
     *
     * 重绘
     */
    private void invalidateView() {
        //判断属否是UI线程
        if(Looper.getMainLooper()==Looper.myLooper()){

            invalidate();
        }else {
            postInvalidate();
        }

    }


}

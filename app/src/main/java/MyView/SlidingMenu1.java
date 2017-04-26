package MyView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import jintong.museum2.R;


/**
 *
 *
 * 为主界面设置仿QQ的侧滑菜单，暂时弃用
 *
 * Created by wjc on 2017/1/11.
 * <p/>
 * 自定义ViewGroup重点要实现的方法
 * 1.onMeasure
 * 决定内部View（子View）以及自身的宽和高
 * 2.onLayout
 * 决定子View的放置的位置
 * 3.onTouchEvent
 */
public class SlidingMenu1 extends HorizontalScrollView {


    private LinearLayout mWrapper;//包裹菜单和内容
    private ViewGroup mMenu;//菜单
    private ViewGroup mContent;//内容

    private int mScreenWidth;//屏幕宽度
    private int mMenuRightPadding;//菜单右侧距离屏幕右侧的距离
    private int mMenuWidth;//menu的宽度

    //设置一个标记，防止onMeasure多次测量
    private boolean once = true;

    //设置标记，菜单当前是否打开
    private  boolean isOpen;



    public SlidingMenu1(Context context) {

        this(context,null);
        Log.e("hhhahah"," xixixix ");

    }

    /**
     * 未使用自定义属性时，会调用这个两个参数的构造函数
     */
    public SlidingMenu1(Context context, AttributeSet attrs) {


        this(context,attrs,0);
        Log.e("hhhahah","  "+context.obtainStyledAttributes(attrs,R.styleable.SlidingMenu1));


    }

    /**
     * 如果在Code中实例化一个View会调用第一个构造函数，如果在xml中定义会调用第二个构造函数，
     * 而第三个函数系统是不调用的，要由View（我们自定义的或系统预定义的View，如此处的CustomTextView和Button）显式调用，
     * 比如在这里我们在第二个构造函数中调用了第三个构造函数，并将R.attr.CustomizeStyle传给了第三个参数。
     *
     *当在布局文件中调用本控件时使用了自定义属性时，会调用此构造方法（三个参数）
     */
    public SlidingMenu1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性,这里获取了<declare-styleable name="SlidingMenu1">的所有值
        TypedArray ta=context.getTheme().obtainStyledAttributes(attrs,R.styleable.SlidingMenu1,defStyleAttr,0);


        /**
         * 获取dimension的方法有几种，区别不大
         * 共同点是都会将dp，sp的单位转为px，px单位的保持不变
         *
         * getDimension() 返回float，
         * getDimensionPixelSize 返回int 小数部分四舍五入
         * getDimensionPixelOffset 返回int，但是会抹去小数部分
         */

        mMenuRightPadding=(int)ta.getDimension(R.styleable.SlidingMenu1_rightPadding,50);


        ta.recycle();
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        //DisplayMetrics经过getMetrics()方法后 ，宽高就被赋值了
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mScreenWidth = outMetrics.widthPixels;



    }


    /**
     * 设置子View和自身的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //如果是第一次，进行测量，并且在测量设置完成后，once=false
        if (once) {
            //取出HorizontalScrollView的第一个元素也就是LineaLayout
            mWrapper = (LinearLayout) getChildAt(0);
            //LineaLayout的第一个元素menu
            mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mContent = (ViewGroup) mWrapper.getChildAt(1);

            //宽度的设置，menu和content的宽度就可以决定wrapper的宽度，就不用设置了
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;

            once = false;

        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 在不做任何偏移的起始情况下，menu是完全显示的
     * 所以要通过设置偏移量，先将menu隐藏
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        //参数changed表示view有新的尺寸或位置；
        if (changed) {
            //整个布局偏移一个菜单的宽度，使菜单完全隐藏
            // 这个偏移是瞬间完成的
            this.scrollTo(mMenuWidth, 0);

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:

                /**
                 *  这里的ScrollX表示用户在滑动抬起之后 未拉出的菜单宽度
                 *  就是仍然隐藏的宽度
                 *  当这个宽度小于菜单宽度的一半时，应让菜单显示
                 *  多于菜单的一半时，让菜单继续隐藏
                 *
                 */

                int scrollX = getScrollX();
                if (scrollX >= mMenuWidth / 2) {//完全隐藏
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen=false;
                } else {//完全显示，就是不做偏移的起始位置
                    this.smoothScrollTo(0, 0);

                    isOpen=true;

                }
                return true;


        }


        return super.onTouchEvent(ev);
    }

    //打开菜单
    public void openMenu(){
        if(isOpen)return;
        this.smoothScrollTo(0,0);
        isOpen=true;

    }

    //关闭菜单
    public void closeMenu(){
        if(!isOpen)return;
        this.smoothScrollTo(mMenuWidth,0);
        isOpen=false;
    }

    //菜单状态切换
    public void toggle(){
        if(isOpen){
            closeMenu();

        }else{
            openMenu();

        }
    }


    /**
     *
     *重点是 l 参数
     * 这里l=getScrollX(); 也就是滑动的当前未拉出的部分，
     * 我们需要设置translationX 将那部分拉出
     *
     *
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float scale=l*1.0f/mMenuWidth;
        //调用属性动画 设置TranslationX

        ObjectAnimator.ofFloat(mMenu,"translationX",l*0.6f).setDuration(0).start();



    }
}

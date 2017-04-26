package MyView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 热门关键词 和 搜索历史 的容器ViewGroup
 * Created by wjc on 2017/3/20.
 */

public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure( widthMeasureSpec,  heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec); //布局文件写的是fill_parent，那么这里就是确切的宽度值
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec); //这里就是EXACTLY

        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec); //布局文件写的是wrap_content，那么这里就是基于子控件的值
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec); //这里就是AT_MOST

        /**
         * 如果mode是AT_MOST，那就不能直接得到他的值，需要进行下面的判断和计算
         */
        int width = 0; //这是最后计算得到的宽高值
        int height = 0;

        //记录每一行的宽度与高度
        int lineWidth = 0;
        int lineHeight = 0;

        //得到内部元素的个数
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {

            View child = getChildAt(i);
            //逐个测量子 View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到子 View的layoutParams（由其父View决定），在generateLayoutParams方法中已经预先定义过
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(childWidth, lineWidth); //宽度为逐一比较后的最大行宽
                lineWidth = childWidth;  //重置行宽
                height += lineHeight; //叠加高度
                lineHeight = childHeight;

            } else {

                //未换行

                lineWidth += childWidth; //叠加行宽
                lineHeight = Math.max(lineHeight, childHeight); //得到当前行最大高度


            }

            if (i == cCount-1) {//最后一个控件

                width = Math.max(lineWidth, width);
                height +=lineHeight;


            }

//            if(modeWidth==MeasureSpec.AT_MOST){
//                setMeasuredDimension(width,height);
//            }else {
//                setMeasuredDimension(sizeWidth,sizeHeight);
//            }


        }

        Log.e("TAG", "sizeWidth " + sizeWidth);
        Log.e("TAG", "sizeHeight " + sizeHeight);

        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);

    }


    /**
     * 逐行 存储所有的View
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllViews.clear(); //因为onLayout跟OnMeasure一样会被多次调用，所以这里需要清空一下
        mLineHeight.clear();

        int width = getWidth();  //已经测量过的当前ViewGroup的宽度

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<View>();
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {

            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);

                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

                lineViews = new ArrayList<View>();


            }

            //如果不需要换行
            lineWidth += childWidth + lp.topMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);

        }

        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        //s设置子 View的位置
        int left = 0;
        int top = 0;
        int lineNum = mAllViews.size();
        for (int i = 0; i < lineNum; i++) {

            //当前行的所有的View
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);

                //如果当前View是不可见的  直接跳过
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                //子 View布局
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;


            }

            left = 0;
            top += lineHeight;

        }


    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}

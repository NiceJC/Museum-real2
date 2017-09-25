package myView;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;

import jintong.museum2.R;

/**
 *
 */
public abstract class PullBaseView<T extends RecyclerView> extends LinearLayout {

    protected T mRecyclerView;
    private boolean isCanScrollAtRereshing = true;//刷新时是否可滑动
    private boolean isCanPullDown = true;//是否可下拉
    private boolean isCanPullUp = true;//滑动到底部时是否需要进行刷新（还有没有更多数据）
    // pull state
    private static final int PULL_UP_STATE = 0;
    private static final int PULL_DOWN_STATE = 1;
    // refresh states
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;

    private int mLastMotionY;
    //headerview
    private View mHeaderView;
    private int mHeaderViewHeight;
    private ImageView mHeaderImageView;
    private TextView mHeaderTextView;
    private TextView mHeaderUpdateTextView;
    private ProgressBar mHeaderProgressBar;


    private LayoutInflater mInflater;
    private int mHeaderState;

    private int mPullState;

    private RotateAnimation mFlipAnimation;//变为向下的箭头,改变箭头方向
    private RotateAnimation mReverseFlipAnimation;//变为逆向的箭头,旋转
    private OnRefreshListener refreshListener;

    public PullBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullBaseView(Context context) {
        super(context);
    }


    /**
     * 防止tab切换回自定义View时，恢复状态时将id一并恢复，造成控件id重复的情况
     *
     * @param state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        try {
            super.onRestoreInstanceState(state);
        } catch (Exception e) {

        }
        state = null;

    }

    /**
     * init
     */
    private void init(Context context, AttributeSet attrs) {
        // Load all of the animations we need in code rather than through XML
        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);

        mInflater = LayoutInflater.from(getContext());
        // header view 在此添加,保证是第一个添加到linearlayout的最上端
        addHeaderView();
        mRecyclerView = createRecyclerView(context, attrs);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        addView(mRecyclerView);
    }

    private void addHeaderView() {
        // header view
        mHeaderView = mInflater.inflate(R.layout.refresh_header, this, false);

        mHeaderImageView = (ImageView) mHeaderView.findViewById(R.id.pull_to_refresh_image);
        mHeaderTextView = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_text);
        mHeaderUpdateTextView = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_updated_at);
        mHeaderProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.pull_to_refresh_progress);
        mHeaderUpdateTextView.setText("最近更新:" + getFormatDateString("MM-dd HH:mm"));
        // header layout
        measureView(mHeaderView);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
        // 设置topMargin的值为负的header View高度,即将其隐藏在最上方
        params.topMargin = -(mHeaderViewHeight);
        // mHeaderView.setLayoutParams(params1);
        addView(mHeaderView, params);

    }

    public static final String getFormatDateString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {//刷新时禁止滑动  直接把滑动事件消费了
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isCanScrollAtRereshing) {
                    if (mHeaderState == REFRESHING) {
                        return true;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getRawY();
        int x = (int) e.getRawX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 首先拦截down事件,记录y坐标
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 是向下运动,< 0是向上运动
                int deltaY = y - mLastMotionY;
                if (isRefreshViewScroll(deltaY)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }


    /**
     * 如果onInterceptTouchEvent返回了true，即拦截了事件（内部的RecyclerView滑动到低端或者顶端的时候）
     * 那么交由下面的onTouchEvent来处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastMotionY;
                if (isCanPullDown && mPullState == PULL_DOWN_STATE) {
                    headerPrepareToRefresh(deltaY);
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int topMargin = getHeaderTopMargin();
                if (isCanPullDown && mPullState == PULL_DOWN_STATE) {
                    if (topMargin >= 0) {
                        // 开始刷新
                        headerRefreshing();
                    } else {
                        // 还没有执行刷新，重新隐藏
                        setHeaderTopMargin(-mHeaderViewHeight);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 是否应该由本View来处理滑动 而不是交给子View（RecyclerView）
     *
     * @param deltaY , deltaY > 0 是向下运动,< 0是向上运动
     *               deltaY的值时与当次滑动的ACTION_DOWN坐标进行比对
     * @return
     */
    private boolean isRefreshViewScroll(int deltaY) {
        if (mHeaderState == REFRESHING ) {
            return false;
        }
        if (deltaY >= -20 && deltaY <= 20) //滑动太小也不做处理
            return false;

        if (mRecyclerView != null) {
            // 子view(ListView or GridView)滑动到最顶端
            if (deltaY > 0) {
                View child = mRecyclerView.getChildAt(0);
                if (child == null) {
                    // 如果mRecyclerView中没有数据,不拦截
                    return false;
                }
                if (isScrollTop() && child.getTop() == 0) {
                    mPullState = PULL_DOWN_STATE;
                    return true;
                }
                int top = child.getTop();
                int padding = mRecyclerView.getPaddingTop();
                if (isScrollTop() && Math.abs(top - padding) <= 8) {// 这里之前用3可以判断,但现在不行,还没找到原因
                    mPullState = PULL_DOWN_STATE;
                    return true;
                }

            } else if (deltaY < 0) {

                    return false;

            }
        }
        return false;
    }

    /**
     * 判断mRecyclerView是否滑动到顶部
     *
     * @return
     */
    boolean isScrollTop() {

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();


        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;


            int[] firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);

            if (findMin(firstPositions) == 0) {
                return true;
            } else {
                return false;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;

            if (gridLayoutManager.findFirstVisibleItemPosition() == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断mRecyclerView是否滑动到底部
     * (mRecyclerView.getAdapter().getItemCount() - 1)
     *
     * @return
     */
    boolean isScrollBottom() {


        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();


        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;


            int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);

            if (findMax(lastPositions) == (mRecyclerView.getAdapter().getItemCount() - 1)) {
                return true;
            } else {
                return false;
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.findLastVisibleItemPosition() == (mRecyclerView.getAdapter().getItemCount() - 1)) {
                return true;
            } else {
                return false;
            }
        } else {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;

            if (gridLayoutManager.findLastVisibleItemPosition() == (mRecyclerView.getAdapter().getItemCount() - 1)) {
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * header 准备刷新,手指移动过程,还没有释放
     *
     * @param deltaY ,手指滑动的距离
     */
    private void headerPrepareToRefresh(int deltaY) {
        int newTopMargin = changingHeaderViewTopMargin(deltaY);
        // 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
        if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
            mHeaderTextView.setText(R.string.pull_to_refresh_release_label);
            mHeaderUpdateTextView.setVisibility(View.VISIBLE);
            mHeaderImageView.clearAnimation();
            mHeaderImageView.startAnimation(mFlipAnimation);
            mHeaderState = RELEASE_TO_REFRESH;
        } else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
            mHeaderImageView.clearAnimation();
            mHeaderImageView.startAnimation(mFlipAnimation);
            // mHeaderImageView.
            mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
            mHeaderState = PULL_TO_REFRESH;
        }
    }



    /**
     * 修改Header view top margin的值
     *
     * @param deltaY
     * @description
     */
    private int changingHeaderViewTopMargin(int deltaY) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        float newTopMargin = params.topMargin + deltaY * 0.3f;
        // 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了,感谢网友yufengzungzhe的指出
        // 表示如果是在上拉后一段距离,然后直接下拉
        if (deltaY > 0 && mPullState == PULL_UP_STATE && Math.abs(params.topMargin) <= mHeaderViewHeight) {
            return params.topMargin;
        }
        // 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
        if (deltaY < 0 && mPullState == PULL_DOWN_STATE && Math.abs(params.topMargin) >= mHeaderViewHeight) {
            return params.topMargin;
        }
        params.topMargin = (int) newTopMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
        return params.topMargin;
    }


    /**
     * header refreshing
     */
    public void headerRefreshing() {
        mHeaderState = REFRESHING;
        setHeaderTopMargin(0);
        mHeaderImageView.setVisibility(View.GONE);
        mHeaderImageView.clearAnimation();
        mHeaderImageView.setImageDrawable(null);
        mHeaderProgressBar.setVisibility(View.VISIBLE);
        mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);
        if (refreshListener != null) {
            refreshListener.onHeaderRefresh(this);
        }
    }



    /**
     * 设置header view 的topMargin的值
     *
     * @param topMargin ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
     * @description
     */
    private void setHeaderTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.topMargin = topMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
    }

    /**
     * header view 完成更新后恢复初始状态
     */
    public void onHeaderRefreshComplete() {
        setHeaderTopMargin(-mHeaderViewHeight);
        mHeaderImageView.setVisibility(View.VISIBLE);
        mHeaderImageView.setImageResource(R.mipmap.ic_pulltorefresh_arrow);
        mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
        mHeaderUpdateTextView.setText("最近更新：" + getFormatDateString("MM-dd HH:mm"));
        mHeaderProgressBar.setVisibility(View.GONE);
        mHeaderState = PULL_TO_REFRESH;
    }





    /**
     * footer view 完成更新后恢复初始状态
     */
    public void onFooterRefreshComplete() {
//        setHeaderTopMargin(-mHeaderViewHeight);
//        mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
//        mFooterTextView.setVisibility(View.VISIBLE);
//        mFooterProgressBar.setVisibility(View.GONE);
//        mFooterState = PULL_TO_REFRESH;
//
//        if (mRecyclerView != null) {
//            mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
//        }

    }

    /**
     * 获取当前header view 的topMargin
     *
     * @description
     */
    private int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        return params.topMargin;
    }


    /**
     * set headerRefreshListener
     *
     * @description
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * Interface definition for a callback to be invoked when list/grid footer
     * view should be refreshed.
     */
    public interface OnRefreshListener {
        void onHeaderRefresh(PullBaseView view);

        void onFooterRefresh(PullBaseView view);
    }

    /**
     * 设置是否可以在刷新时滑动
     *
     * @param canScrollAtRereshing
     */
    public void setCanScrollAtRereshing(boolean canScrollAtRereshing) {
        isCanScrollAtRereshing = canScrollAtRereshing;
    }

    /**
     * 设置是否可上拉
     *
     * @param canPullUp
     */
    public void setCanPullUp(boolean canPullUp) {
//        isCanPullUp = canPullUp;
//        if (!canPullUp) {
//            mFooterView.setVisibility(GONE);
//        }
    }

    /**
     * 设置是否可下拉
     *
     * @param canPullDown
     */
    public void setCanPullDown(boolean canPullDown) {
        isCanPullDown = canPullDown;

        if (!canPullDown) {
            mHeaderView.setVisibility(GONE);
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


    protected abstract T createRecyclerView(Context context, AttributeSet attrs);


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    private int findMin(int[] lastPositions) {
        int min = lastPositions[0];
        for (int value : lastPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }


}

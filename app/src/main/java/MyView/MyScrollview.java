package MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 屏蔽滑动事件
 *
 * 用于在RecyclerView外层嵌套ScrollView
 * 直接在ScrollView层将竖直的滑动事件拦截下来，避免与内层的RecyclerView滑动冲突
 *
 */
public class MyScrollview extends ScrollView {

    private int downY;
    private int mTouchSlop;

    public MyScrollview(Context context) {

        this(context,null);
    }

    public MyScrollview(Context context, AttributeSet attrs) {
        this(context,attrs,0);

    }

    public MyScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}  
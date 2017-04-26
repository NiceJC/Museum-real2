package MyView;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import util.SysUtils;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public GridItemDecoration(int space, Activity activity) {
       this.space=  SysUtils.DpToPx(activity, space);

    }


    /**
     *
     * outRect的四个参数是在原来的技术上，增加一个额外的padding
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space/2;
        outRect.right=0;
        outRect.bottom = space;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) %2==0) {
            outRect.left = 0;
        }
    }

}
package interfaces;

import android.view.View;
/**
 *
 * 用于RecyclerView的点击时间的回调接口
 */


public interface OnItemClickListener{

        void onItemClick(View view, int position);
        void OnItemLongClick(View view,int position);

    }
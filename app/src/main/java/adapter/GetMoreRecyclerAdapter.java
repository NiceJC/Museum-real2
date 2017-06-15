package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wjc on 2017/5/15.
 */

public class GetMoreRecyclerAdapter<T,H extends RecyclerView.ViewHolder> extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{



    public static final int STATE_UP_TO_LOAD =0;
    public static final int STATE_LODING=1;
    public static final int STATE_NOMORE=2;

    private int mState=STATE_UP_TO_LOAD; //默认可以加载更多
    private ProgressBar progressBar;
    private TextView textView;
    private View footView;
    public static final int TYPE_FOOT=Integer.MAX_VALUE;

    List<T> data;
    Context context;
    private  LayoutInflater mInflater;
    private  int layoutId;

    public GetMoreRecyclerAdapter(List<T> data, Context context,int layoutId) {
        this.data=data;
        this.layoutId=layoutId;
        this.context=context;
        mInflater=LayoutInflater.from(context);

    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }
}

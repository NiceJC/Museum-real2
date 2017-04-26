package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import entity.Exhibition;
import interfaces.OnItemClickListener;
import jintong.museum2.R;

/**
 * 首页展览列表的adapter
 * Created by wjc on 2017/3/2.
 */

public class ExhibitionListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Exhibition> datas;
    private RequestManager requestManager;

    private OnItemClickListener mOnItemClickListener;

    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //无更多内容
    public static final int NO_MORE_TO_LOAD = 2;

    //上拉加载更多状态-默认为0
    private int load_more_status = 0;


    private LayoutInflater mInflater;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public ExhibitionListRecyclerAdapter(Context context, List<Exhibition> datas) {
        this.context = context;
        this.datas = datas;
        requestManager = Glide.with(context);
        mInflater = LayoutInflater.from(context);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.main_fragment_1_item, parent, false);
            ExhibitionViewHolder viewHolder = new ExhibitionViewHolder(view);


            return viewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.foot_view, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ExhibitionViewHolder) {
            Exhibition exhibition = datas.get(position);
            ((ExhibitionViewHolder) holder).exhibitName.setText(exhibition.getExhibitName());
            requestManager.load(exhibition.getImageURLs().get(0)).into(((ExhibitionViewHolder) holder).exhibitImage);
            ((ExhibitionViewHolder) holder).exhibitMuseumName.setText(exhibition.getMuseumName());

            ((ExhibitionViewHolder) holder).exhibitMuseumLocation.setText(exhibition.getLocateCity());
            ((ExhibitionViewHolder) holder).exhibitMuseumDistance.setText("99KM"); //这里是需要根据经纬度实时计算的

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickListener.OnItemLongClick(holder.itemView, position);
                        return false;
                    }
                });

            }
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.progressBar.setVisibility(View.GONE);
                    footViewHolder.textView.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.progressBar.setVisibility(View.VISIBLE);
                    footViewHolder.textView.setText("正在加载更多数据...");
                    break;

                case NO_MORE_TO_LOAD:
                    footViewHolder.progressBar.setVisibility(View.GONE);
                    footViewHolder.textView.setText("哎呀...到底了");
                default:
                    break;
            }

        }

    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    public void refreshItem(List<Exhibition> exhibitions, int status) {


//        datas.remove(0);
//        datas.remove(1);
//

        datas.clear();
        datas.addAll(exhibitions);
        load_more_status = status;
        notifyDataSetChanged();

    }

    public void addMoreItem(List<Exhibition> exhibitions, int status) {
        datas.addAll(exhibitions);
        load_more_status = status;

        notifyDataSetChanged();

    }


}


class ExhibitionViewHolder extends RecyclerView.ViewHolder {


    ImageView exhibitImage; //展览图片
    TextView exhibitName; //展览名称
    TextView exhibitMuseumName; //博物馆名称
    TextView exhibitMuseumLocation; //博物馆地址
    TextView exhibitMuseumDistance; //博物馆距离


    public ExhibitionViewHolder(View itemView) {
        super(itemView);

        exhibitImage = (ImageView) itemView.findViewById(R.id.exhibit_image);
        exhibitName = (TextView) itemView.findViewById(R.id.exhibit_name);
        exhibitMuseumName = (TextView) itemView.findViewById(R.id.exhibit_museum_name);
        exhibitMuseumLocation = (TextView) itemView.findViewById(R.id.exhibit_museum_location);
        exhibitMuseumDistance = (TextView) itemView.findViewById(R.id.exhibit_museum_distance);


    }


}


class FootViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    ProgressBar progressBar;

    public FootViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.foot_view_text);

        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}

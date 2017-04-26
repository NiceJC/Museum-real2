package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import entity.Collection;
import entity.Exhibition;
import entity.Museum;
import interfaces.OnItemClickListener;
import jintong.museum2.CollectionActivity;
import jintong.museum2.ExhibitionRoomActivity;
import jintong.museum2.MuseumActivity;
import jintong.museum2.R;

/**
 * Created by wjc on 2017/3/22.
 */

public class SearchingResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<Museum> museumList;
    private List<Exhibition> exhibitionList;
    private List<Collection> collectionList;

    private RequestManager requestManager;

    private OnItemClickListener mOnItemClickListener;

    private static final int TYPE_FOOTER = 0;  //顶部FootView

    private static final int TYPE_MUSEUM = 1;

    private static final int TYPE_EXHIBITION = 2;

    private static final int TYPE_COLLECTION = 3;


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


    public SearchingResultAdapter(Context context, List<Museum> museumList, List<Exhibition> exhibitionList, List<Collection> collectionList) {
        this.context = context;
        this.museumList = museumList;
        this.exhibitionList = exhibitionList;
        this.collectionList = collectionList;

        requestManager = Glide.with(context);
        mInflater = LayoutInflater.from(context);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_MUSEUM) {
            View view = mInflater.inflate(R.layout.search_result_museum, parent, false);
            ResultMuseumViewHolder viewHolder = new ResultMuseumViewHolder(view);


            return viewHolder;
        } else if (viewType == TYPE_EXHIBITION) {
            View view = mInflater.inflate(R.layout.search_result_exhibit, parent, false);
            ResultExhibitViewHolder viewHolder = new ResultExhibitViewHolder(view);

            return viewHolder;
        } else if (viewType == TYPE_COLLECTION) {
            View view = mInflater.inflate(R.layout.search_result_colt, parent, false);
            ResultColtViewHolder viewHolder = new ResultColtViewHolder(view);

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

        if (holder instanceof ResultMuseumViewHolder) {
            Museum museum = museumList.get(position);
            ((ResultMuseumViewHolder) holder).typeName.setText("类型：博物馆");
            requestManager.load(museum.getImageURLs().get(0)).into(((ResultMuseumViewHolder) holder).itemImage);
            ((ResultMuseumViewHolder) holder).itemName.setText(museum.getMuseumName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MuseumActivity.class);
                    context.startActivity(intent);

                }
            });


        } else if (holder instanceof ResultExhibitViewHolder) {
            Exhibition exhibition = exhibitionList.get(position - museumList.size());
            ((ResultExhibitViewHolder) holder).typeName.setText("类型：展览");
            requestManager.load(exhibition.getImageURLs().get(0)).into(((ResultExhibitViewHolder) holder).itemImage);
            ((ResultExhibitViewHolder) holder).itemName.setText(exhibition.getExhibitName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExhibitionRoomActivity.class);
                    context.startActivity(intent);


                }
            });


        }else if(holder instanceof ResultColtViewHolder){

            Collection collection = collectionList.get(position - museumList.size()-exhibitionList.size());
            ((ResultColtViewHolder) holder).typeName.setText("类型：展品");
            requestManager.load(collection.getColtImageURLs().get(0)).into(((ResultColtViewHolder) holder).itemImage);
            ((ResultColtViewHolder) holder).itemName.setText(collection.getColtName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CollectionActivity.class);
                    context.startActivity(intent);



                }
            });


        }


        else if (holder instanceof FootViewHolder) {
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
        return museumList.size() + exhibitionList.size() + collectionList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position < museumList.size()) {
            return TYPE_MUSEUM;
        } else if (position < museumList.size() + exhibitionList.size()) {
            return TYPE_EXHIBITION;
        } else if (position < getItemCount() - 1) {
            return TYPE_COLLECTION;
        } else {
            return TYPE_FOOTER;
        }
    }


    public void refreshItem(List<Exhibition> exhibitions, int status) {

//        datas.clear();
//        datas.addAll(exhibitions);
        load_more_status = status;
        notifyDataSetChanged();

    }

    public void addMoreItem(List<Exhibition> exhibitions, int status) {
//        datas.addAll(exhibitions);
        load_more_status = status;

        notifyDataSetChanged();

    }


}


class ResultMuseumViewHolder extends RecyclerView.ViewHolder {


    ImageView itemImage; //展览图片
    TextView typeName; //类型名称
    TextView itemName; //名称

    public ResultMuseumViewHolder(View itemView) {
        super(itemView);

        itemImage = (ImageView) itemView.findViewById(R.id.result_item_image);
        typeName = (TextView) itemView.findViewById(R.id.result_item_type);
        itemName = (TextView) itemView.findViewById(R.id.result_item_name);


    }


}

class ResultExhibitViewHolder extends RecyclerView.ViewHolder {


    ImageView itemImage; //展览图片
    TextView typeName; //类型名称
    TextView itemName; //名称

    public ResultExhibitViewHolder(View itemView) {
        super(itemView);

        itemImage = (ImageView) itemView.findViewById(R.id.result_item_image);
        typeName = (TextView) itemView.findViewById(R.id.result_item_type);
        itemName = (TextView) itemView.findViewById(R.id.result_item_name);


    }


}

class ResultColtViewHolder extends RecyclerView.ViewHolder {


    ImageView itemImage; //展览图片
    TextView typeName; //类型名称
    TextView itemName; //名称

    public ResultColtViewHolder(View itemView) {
        super(itemView);

        itemImage = (ImageView) itemView.findViewById(R.id.result_item_image);
        typeName = (TextView) itemView.findViewById(R.id.result_item_type);
        itemName = (TextView) itemView.findViewById(R.id.result_item_name);


    }


}


class FootViewHolder2 extends RecyclerView.ViewHolder {

    TextView textView;
    ProgressBar progressBar;

    public FootViewHolder2(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.foot_view_text);

        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }


}

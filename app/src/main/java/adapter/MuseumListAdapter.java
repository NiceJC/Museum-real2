package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import entity.Exhibition;
import entity.Museum;
import interfaces.OnItemClickListener;
import jintong.museum2.R;

/**
 *
 * 首页展馆列表RecyclerView的Adapter
 *
 * Created by wjc on 2017/3/3.
 */

public class MuseumListAdapter extends RecyclerView.Adapter<MuseumListViewHolder> {

    private Context context;
    private List<Museum> datas;
    private RequestManager requestManager;


    private OnItemClickListener mOnItemClickListener;
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public MuseumListAdapter(Context context, List<Museum> datas) {
        this.context = context;
        this.datas = datas;
        requestManager = Glide.with(context);


    }

    @Override
    public MuseumListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_fragment_2_item, parent, false);
        MuseumListViewHolder viewHolder = new MuseumListViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MuseumListViewHolder holder, final int position) {

        final Museum museum = datas.get(position);

        requestManager.load(museum.getImageURLs().get(0)).into(holder.museumImage);
        holder.museumName.setText(museum.getMuseumName());

        holder.museumLocation.setText(museum.getLocateCity());
        holder.museumDistance.setText("99KM"); //这里是需要根据经纬度实时计算的


        if(mOnItemClickListener!=null) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mOnItemClickListener.onItemClick(holder.itemView,position);


                    return false;
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}




class MuseumListViewHolder extends RecyclerView.ViewHolder {

    ImageView museumImage; //博物馆图片

    TextView museumName; //博物馆名称
    TextView museumLocation; //博物馆地址
    TextView museumDistance; //博物馆距离


    public MuseumListViewHolder(View itemView) {
        super(itemView);

        museumImage = (ImageView) itemView.findViewById(R.id.museum_image);

        museumName = (TextView) itemView.findViewById(R.id.museum_name);
        museumLocation = (TextView) itemView.findViewById(R.id.museum_location);
        museumDistance = (TextView) itemView.findViewById(R.id.museum_distance);


    }
}
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
 * 首页展馆列表RecyclerView的Adapter
 * <p>
 * Created by wjc on 2017/3/3.
 */

public class MuseumListAdapter extends BaseAdapter<MuseumListAdapter.MuseumListViewHolder> {


    public MuseumListAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        super(context, listDatas, onViewClickListener);
    }

    @Override
    public MuseumListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MuseumListViewHolder(mInflater.inflate(R.layout.main_fragment_2_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MuseumListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Museum museum = (Museum) listDatas.get(position);

        requestManager.load(museum.getImageURLs().get(0)+ "!/fxfn/1080x1080").into(holder.museumImage);
        holder.museumName.setText(museum.getMuseumName());

        holder.museumLocation.setText(museum.getLocateCity());
        holder.museumDistance.setText("99KM"); //这里是需要根据经纬度实时计算的

    }

    @Override
    public int getItemCount() {
        return listDatas.size();
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

    class ViewClickListener implements View.OnClickListener {

        OnViewClickListener onViewClickListener;
        int position;
        int viewType;

        public ViewClickListener(OnViewClickListener onViewClickListener, int position, int viewType) {
            this.onViewClickListener = onViewClickListener;
            this.position = position;
            this.viewType = viewType;
        }

        @Override
        public void onClick(View v) {
            onViewClickListener.onViewClick(position, viewType);

        }
    }


}




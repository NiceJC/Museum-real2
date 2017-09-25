package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Exhibition;
import jintong.museum2.R;
import util.DistanceUtil;

/**
 * Created by wjc on 2017/5/16.
 */

public class ExhibitionListAdapter extends BaseAdapter<ExhibitionListAdapter.ExhibitViewHolder> {



    public ExhibitionListAdapter(Context context, List<Object> listDatas) {


        super(context, listDatas);
    }

    @Override
    public ExhibitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ExhibitViewHolder(mInflater.inflate(R.layout.main_fragment_1_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ExhibitViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Exhibition exhibition = (Exhibition) listDatas.get(position);

        holder.exhibitName.setText(exhibition.getExhibitName());
        requestManager.load(exhibition.getImage1().getFileUrl()+ "!/fxfn/1080x1080").into((holder).exhibitImage);
        holder.exhibitMuseumName.setText(exhibition.getToMuseum().getMuseumName());
        holder.exhibitMuseumLocation.setText(exhibition.getToMuseum().getLocateCity());
        holder.exhibitMuseumDistance.setText(DistanceUtil. getCurrentLocal(context,exhibition.getToMuseum().getGeoPoint())+"KM"); //这里是需要根据经纬度实时计算的
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }


    class ExhibitViewHolder extends RecyclerView.ViewHolder {

        ImageView exhibitImage; //展览图片
        TextView exhibitName; //展览名称
        TextView exhibitMuseumName; //博物馆名称
        TextView exhibitMuseumLocation; //博物馆地址
        TextView exhibitMuseumDistance; //博物馆距离

        public ExhibitViewHolder(View itemView) {
            super(itemView);
            exhibitImage = (ImageView) itemView.findViewById(R.id.exhibit_image);
            exhibitName = (TextView) itemView.findViewById(R.id.exhibit_name);
            exhibitMuseumName = (TextView) itemView.findViewById(R.id.exhibit_museum_name);
            exhibitMuseumLocation = (TextView) itemView.findViewById(R.id.exhibit_museum_location);
            exhibitMuseumDistance = (TextView) itemView.findViewById(R.id.exhibit_museum_distance);

        }
    }

    class ViewClickListener implements View.OnClickListener{

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
            onViewClickListener.onViewClick(position,viewType);

        }
    }

}

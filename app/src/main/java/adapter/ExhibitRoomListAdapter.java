package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.ExhibitRoom;
import jintong.museum2.R;

/**
 *
 * Created by wjc on 2017/5/22.
 */

public class ExhibitRoomListAdapter extends BaseAdapter<ExhibitRoomListAdapter.ExhibitRoomViewHolder> {

    public ExhibitRoomListAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        super(context, listDatas, onViewClickListener);
    }
    public ExhibitRoomListAdapter(Context context, List<Object> listDatas) {
        super(context, listDatas);
    }
    @Override
    public ExhibitRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExhibitRoomViewHolder(mInflater.inflate(R.layout.exhibit_room_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ExhibitRoomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position) ;
        ExhibitRoom room = (ExhibitRoom) listDatas.get(position);

        requestManager.load(room.getImage1().getFileUrl()+"!/fxfn/700x700").into(holder.imageView1);
//        requestManager.load(room.getImageURL2()+"!/fxfn/300x300").error(R.drawable.blog_watch_icon).into(holder.imageView2);
//
//        requestManager.load(room.getImageURL3()+"!/fxfn/300x300").error(R.drawable.blog_watch_icon).into(holder.imageView3);
//        requestManager.load(room.getImageURL4()+"!/fxfn/300x300").error(R.drawable.blog_watch_icon).into(holder.imageView4);


        holder.name.setText(room.getName());
        holder.num.setText(room.getCollectionNum() + "");

    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }


    class ExhibitRoomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1; //展厅图片
        ImageView imageView2;
        ImageView imageView3;
        ImageView imageView4;

        TextView name; //展厅名称
        TextView num; //展厅展品数量


        public ExhibitRoomViewHolder(View itemView) {
            super(itemView);

            imageView1 = (ImageView) itemView.findViewById(R.id.exhibit_room_image_1);
//            imageView2 = (ImageView) itemView.findViewById(R.id.exhibit_room_image_2);
//            imageView3 = (ImageView) itemView.findViewById(R.id.exhibit_room_image_3);
//            imageView4 = (ImageView) itemView.findViewById(R.id.exhibit_room_image_4);



            name = (TextView) itemView.findViewById(R.id.exhibit_room_name);
            num = (TextView) itemView.findViewById(R.id.exhibit_room_count);


        }
    }
}

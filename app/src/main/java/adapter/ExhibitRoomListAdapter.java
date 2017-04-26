package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import entity.ExhibitRoom;
import interfaces.OnItemClickListener;
import jintong.museum2.R;

/**
 *
 *博物馆详情页的展厅的Adapter
 * Created by wjc on 2017/3/6.
 */

public class ExhibitRoomListAdapter extends RecyclerView.Adapter<ExhibitRoomViewHolder> {

    Context context;
    List<ExhibitRoom> rooms;
    RequestManager requestManager;


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ExhibitRoomListAdapter(Context context, List<ExhibitRoom> datas) {
        this.context=context;
        this.rooms=datas;
        requestManager= Glide.with(context);


    }

    @Override
    public ExhibitRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.exhibit_room_item,parent,false);
        ExhibitRoomViewHolder viewHolder=new ExhibitRoomViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ExhibitRoomViewHolder holder, final int position) {

        ExhibitRoom room=rooms.get(position);
        requestManager.load(room.getImageURL()).into(holder.imageView);
        holder.name.setText(room.getName());
        holder.num.setText(room.getCollectionNum()+"");

        if(onItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClickListener.onItemClick(holder.itemView,position);
                }
            });



        }




    }

    @Override
    public int getItemCount() {
        return rooms.size() ;
    }


}
class ExhibitRoomViewHolder extends RecyclerView.ViewHolder {
     ImageView imageView; //展厅图片
    TextView name; //展厅名称
    TextView num; //展厅展品数量


    public ExhibitRoomViewHolder(View itemView) {
        super(itemView);

        imageView= (ImageView) itemView.findViewById(R.id.exhibit_room_image);
        name= (TextView) itemView.findViewById(R.id.exhibit_room_name);
        num= (TextView) itemView.findViewById(R.id.exhibit_room_count);



    }
}
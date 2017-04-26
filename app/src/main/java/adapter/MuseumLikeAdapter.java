package adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import entity.Museum;
import jintong.museum2.R;

/**
 *
 *
 * Created by wjc on 2017/2/28.
 */

public class MuseumLikeAdapter extends RecyclerView.Adapter<MuseumLikeViewHolder> {

    private List<Museum> datas;
    private Activity context;
    private RequestManager requestManager;

    public MuseumLikeAdapter(Activity context,List<Museum> datas) {

        requestManager=Glide.with(context);
        this.context=context;
        this.datas=datas;




    }

    @Override
    public MuseumLikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.mine_fragment_1_item,parent,false);

        MuseumLikeViewHolder viewHolder=new MuseumLikeViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MuseumLikeViewHolder holder, int position) {

        Museum museum=datas.get(position);
        holder.textView.setText(museum.getMuseumName());

        requestManager.load(museum.getImageURLs().get(0)).into(holder.imageView);



    }


    @Override
    public int getItemCount() {
        return datas.size();
    }
}


class MuseumLikeViewHolder extends RecyclerView.ViewHolder {


    ImageView imageView;  //博物馆图片
    TextView textView;    //博物馆名称


    public MuseumLikeViewHolder(View itemView) {
        super(itemView);


        imageView= (ImageView) itemView.findViewById(R.id.mine_f1_item_image);
        textView= (TextView) itemView.findViewById(R.id.mine_f1_item_name);
    }
}

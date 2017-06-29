package adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;


import entity.Collection;
import entity.Exhibition;
import entity.Museum;
import jintong.museum2.CollectionActivity;
import jintong.museum2.ExhibitionActivity;
import jintong.museum2.MuseumActivity;
import jintong.museum2.R;
import util.SysUtils;

import static util.ParameterBase.COLT_ID;
import static util.ParameterBase.EXHIBITION_ID;
import static util.ParameterBase.MUSEUM_ID;

/**
 *
 *
 * Created by wjc on 2017/2/28.
 */

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.MuseumLikeViewHolder> {
    private int mImageViewWidth;

    private List<Object> datas;
    private Activity context;
    private RequestManager requestManager;

    public GridRecyclerAdapter(Activity context, List<Object> datas) {

        mImageViewWidth = (SysUtils.getScreenWidth((Activity) context) - SysUtils.DpToPx(context, 8)) / 2;

        this.context=context;
        this.datas=datas;
        this.requestManager=Glide.with(context);

    }

    @Override
    public MuseumLikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.mine_fragment_1_item,parent,false);

        MuseumLikeViewHolder viewHolder=new MuseumLikeViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MuseumLikeViewHolder holder, int position) {
        holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(mImageViewWidth, mImageViewWidth));

        Object object=datas.get(position);
        if(object instanceof Museum){
            final Museum museum= (Museum) object;
            holder.textView.setText(museum.getMuseumName());
            requestManager.load(museum.getImageURLs().get(0)+ "!/fxfn/500x500").into(holder.imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MuseumActivity.class);
                    intent.putExtra(MUSEUM_ID,museum.getObjectId());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.in_from_right,R.anim.none);
                }
            });
        }else if(object instanceof Exhibition){
            final Exhibition exhibition= (Exhibition) object;
            holder.textView.setText(exhibition.getExhibitName());
            requestManager.load(exhibition.getImage1().getFileUrl()+ "!/fxfn/500x500").into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ExhibitionActivity.class);
                    intent.putExtra(EXHIBITION_ID,exhibition.getObjectId());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.in_from_right,R.anim.none);
                }
            });
        }else{
            final Collection collection= (Collection) object;
            holder.textView.setText(collection.getColtName());
            requestManager.load(collection.getImage1().getFileUrl()+ "!/fxfn/500x500").into(holder.imageView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, CollectionActivity.class);
                    intent.putExtra(COLT_ID,collection.getObjectId());
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.in_from_right,R.anim.none);
                }
            });
        }



    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MuseumLikeViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;  //图片
        TextView textView;    //名称


        public MuseumLikeViewHolder(View itemView) {
            super(itemView);


            imageView= (ImageView) itemView.findViewById(R.id.mine_f1_item_image);
            textView= (TextView) itemView.findViewById(R.id.mine_f1_item_name);
        }
    }

}


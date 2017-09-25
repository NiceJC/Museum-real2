package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import jintong.museum2.R;
import util.SysUtils;

/**
 * Created by wjc on 2017/7/13.
 */

public class NineImageRecyAdapter extends BaseAdapter<NineImageRecyAdapter.NineViewHolder> {

    private int mImageViewWidth;

    public NineImageRecyAdapter(Context context, List<Object> listDatas) {
        super(context, listDatas);
    }

    @Override
    public NineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NineViewHolder(mInflater.inflate(R.layout.image_gridview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(NineViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);


        if(listDatas.size()==1){
            mImageViewWidth = (SysUtils.getScreenWidth((Activity) context) - SysUtils.DpToPx(context, 18)) /2;

        }else{
            mImageViewWidth = (SysUtils.getScreenWidth((Activity) context) - SysUtils.DpToPx(context, 18)) / 3;


        }
        holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(mImageViewWidth, mImageViewWidth));

        String imageUrl= (String) listDatas.get(position);
        requestManager.load(imageUrl).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    class NineViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public NineViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.imageView_item);
        }


    }


}

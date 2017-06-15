package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import entity.Collection;
import jintong.museum2.R;

/**
 *
 * 分类藏品的瀑布流图片展示
 *
 * Created by wjc on 2017/3/24.
 */

public class TypeColtAdapter extends BaseAdapter<TypeColtAdapter.TypeColtViewHolder>{


    public TypeColtAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        super(context, listDatas, onViewClickListener);
    }

    @Override
    public TypeColtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TypeColtViewHolder(mInflater.inflate(R.layout.colt_by_type_item,parent,false));
    }

    @Override
    public void onBindViewHolder(TypeColtAdapter.TypeColtViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);


        Collection collection= (Collection) listDatas.get(position);
        collection.getImage1().getFileUrl();
        requestManager.load(collection.getImage1().getFileUrl()+ "!/fxfn/500x500"
        )

                .animate(R.anim.image_animate)
                .crossFade()
                .placeholder(R.drawable.loadingimage)
                .into(holder.imageView);


        holder.textView.setText(collection.getColtName());
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }


    class TypeColtViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;



        public TypeColtViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.colt_by_type_item_image);
            textView= (TextView) itemView.findViewById(R.id.colt_by_type_item_name);

        }
    }
//    private class DriverViewTarget extends BitmapImageViewTarget {
//
//        public DriverViewTarget(ImageView view, RecyclerView.ViewHolder) {
//            super(view);
//        }
//
//        @Override
//        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//
//            int viewWidth = mBinding.viewImgFeed.getWidth();
//            float scale = resource.getWidth() / (viewWidth * 1.0f);
//            int viewHeight = (int) (resource.getHeight() * scale);
//            setCardViewLayoutParams(viewWidth, viewHeight);
//            super.onResourceReady(resource, glideAnimation);
//        }
//    }




}

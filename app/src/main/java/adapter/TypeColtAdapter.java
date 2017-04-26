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
 * Created by wjc on 2017/3/24.
 */

public class TypeColtAdapter extends RecyclerView.Adapter<TypeColtAdapter.TypeColtViewHolder>{


    private LayoutInflater inflater;
    private Context context;
    private List<Collection> datas;
    private RequestManager requestManager;


    public TypeColtAdapter(Context context, List<Collection> datas) {
        this.context = context;
        this.datas = datas;
        inflater=LayoutInflater.from(context);
        requestManager=Glide.with(context);


    }

    @Override
    public TypeColtAdapter.TypeColtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view=inflater.inflate(R.layout.colt_by_type_item,parent,false);
        TypeColtViewHolder viewHolder=new TypeColtViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(TypeColtAdapter.TypeColtViewHolder holder, int position) {

        Collection collection=datas.get(position);
        requestManager.load(collection.getColtImageURLs().get(0))

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.imageView);
//       requestManager.load(collection.getColtImageURLs().get(0))
//               .asBitmap()
//               .fitCenter()
//               .override(holder.imageView.getWidth(), BitmapImageViewTarget.SIZE_ORIGINAL)
//               .into(new DriverViewTarget((ImageView) holder.itemView));


        holder.textView.setText(collection.getColtName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
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

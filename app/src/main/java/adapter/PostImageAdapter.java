package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import interfaces.OnItemClickListener;
import jintong.museum2.R;
import util.SysUtils;

/**
 * Created by wjc on 2017/4/25.
 */

public class PostImageAdapter extends BaseAdapter {

    private Activity context;

    private List<String> datas;


    private Bitmap plus;


    private LayoutInflater mInflater;

    private int imageWidth;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PostImageAdapter(Activity context, List<String> datas) {
        this.context = context;

        this.datas = datas;


        mInflater = LayoutInflater.from(context);

        //九宫格item的的宽度
        this.imageWidth = (SysUtils.getScreenWidth(context) - SysUtils.DpToPx(context, 60)) / 3;

        plus = BitmapFactory.decodeResource(context.getResources(), R.drawable.plusp);
    }

    @Override
    public int getCount() {
        return datas.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < datas.size()) {
            return datas.get(position);

        } else {
            return plus;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PostImageViewHolder viewHolder;

        if (position < datas.size()) {


            if (convertView == null || convertView.getTag() == null) {
                convertView = mInflater.inflate(R.layout.post_image_item, parent, false);
                viewHolder = new PostImageViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.post_item_image);
                viewHolder.deleteView = (ImageView) convertView.findViewById(R.id.post_item_delete);
                convertView.setTag(viewHolder);


            } else {
                viewHolder = (PostImageViewHolder) convertView.getTag();

            }

            viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClickListener.OnItemLongClick(null,position);

                }
            });


            //自定义的单张图片的ImageView的宽度
            ViewGroup.LayoutParams lp = convertView.getLayoutParams();


            lp.height = imageWidth;
            lp.width = imageWidth;


            convertView.setLayoutParams(lp);


            String  bitmapPath=datas.get(position);


            Glide.with(context).load(bitmapPath)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                    .centerCrop()
                    .dontAnimate()
                    .into(viewHolder.imageView);




            return convertView;
        } else {

            convertView = mInflater.inflate(R.layout.post_image_item_2, parent, false);


            //自定义的单张图片的ImageView的宽度
            ViewGroup.LayoutParams lp = convertView.getLayoutParams();

            final ImageView imageView = (ImageView) convertView.findViewById(R.id.post_item_image_2);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(imageView, position);
                }
            });

            lp.height = imageWidth;
            lp.width = imageWidth;


            convertView.setLayoutParams(lp);

            imageView.setImageBitmap(plus);


            return convertView;
        }

    }


    class PostImageViewHolder {
        ImageView imageView;
        ImageView deleteView;
    }
}

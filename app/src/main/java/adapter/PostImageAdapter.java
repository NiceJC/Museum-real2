package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import jintong.museum2.R;
import util.SysUtils;
import util.ToastUtils;

/**
 * Created by wjc on 2017/4/25.
 */

public class PostImageAdapter extends BaseAdapter {

    private Activity context;

    private List<Bitmap> datas;




    private LayoutInflater mInflater;

    private int imageWidth;


    public PostImageAdapter(Activity context, List<Bitmap> datas) {
        this.context = context;

        this.datas = datas;


        mInflater=LayoutInflater.from(context);

        //九宫格item的的宽度
        this.imageWidth=(SysUtils.getScreenWidth(context)-SysUtils.DpToPx(context,60))/3;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PostImageViewHolder viewHolder;
        if (convertView == null||convertView.getTag()==null) {
            convertView = mInflater.inflate(R.layout.post_image_item,parent, false);
            viewHolder = new PostImageViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.post_item_image);
            viewHolder.deleteView= (ImageView) convertView.findViewById(R.id.post_item_delete);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (PostImageViewHolder) convertView.getTag();

        }

        viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                notifyDataSetChanged();
                ToastUtils.toast(context,position+"will be deleted");
            }
        });



        //自定义的单张图片的ImageView的宽度
        ViewGroup.LayoutParams lp=convertView.getLayoutParams();


            lp.height=imageWidth;
            lp.width=imageWidth;


        convertView.setLayoutParams(lp);

        viewHolder.imageView.setImageBitmap(datas.get(position));




        return convertView;
    }



    class PostImageViewHolder {
        ImageView imageView;
        ImageView deleteView;
    }
}

package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import jintong.museum2.R;
import util.SysUtils;

/**
 *
 * 图片九宫格GridView的Adapter
 * Created by wjc on 2017/2/17.
 */

public class ImageGridViewAdapter extends BaseAdapter {
    private Activity context;

    private List<String> datas;

    private int imageWidth1;
    private int imageWidth2;
    private RequestManager requestManager;

    private int itemNum;
    private ViewGroup.LayoutParams params1;


    private LayoutInflater mInflater;



    public ImageGridViewAdapter(Activity context, List<String> datas) {
        this.context = context;

        this.datas = datas;
        this.itemNum=datas.size();

        mInflater=LayoutInflater.from(context);

        //九宫格item的的宽度
        this.imageWidth2=(SysUtils.getScreenWidth(context)-SysUtils.DpToPx(context,30))/3;
        //当图片数量只有1时  适当放大ImageVew
        this.imageWidth1=imageWidth2*2+SysUtils.DpToPx(context,5);

        this.requestManager = Glide.with(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageGridViewHolder viewHolder;
        if (convertView == null||convertView.getTag()==null) {
            convertView = mInflater.inflate(R.layout.image_gridview_item,parent, false);
            viewHolder = new ImageGridViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView_item);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ImageGridViewHolder) convertView.getTag();

        }


        //自定义的单张图片的ImageView的宽度
      ViewGroup.LayoutParams lp=convertView.getLayoutParams();

        if(itemNum==1){


            lp.height=imageWidth1;
            lp.width=imageWidth1;

            viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);



        }else {
            lp.height=imageWidth2;
            lp.width=imageWidth2;
        }

        convertView.setLayoutParams(lp);
        requestManager.load(datas.get(position)).into(viewHolder.imageView);




        return convertView;
    }


}

class ImageGridViewHolder {
    ImageView imageView;

}

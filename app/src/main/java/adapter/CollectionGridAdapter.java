package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.w3c.dom.Text;

import java.util.List;

import entity.Collection;
import jintong.museum2.R;
import util.SysUtils;

/**
 * 宝库页推荐藏品的RecyclerView的Adapter，
 * 动态测量屏幕宽度，再设置每个Item的宽度
 *
 *
 * Created by wjc on 2017/2/21.
 */

public class CollectionGridAdapter extends RecyclerView.Adapter<CollectionGridViewHolder> implements View.OnClickListener {


    private Context context;
    private LayoutInflater mInflater;
    private List<Collection> mDatas;
    private RequestManager requestManager;

    private int mImageViewWidth;

    private int mItemWidth;
    private int mItemHeight;


    public CollectionGridAdapter(Activity context, List<Collection> datas) {


        this.context = context;

        this.mDatas = datas;
        this.requestManager = Glide.with(context);
        mInflater = LayoutInflater.from(context);

        //根据屏幕宽度确定ImageView的宽度
        mImageViewWidth = (SysUtils.getScreenWidth(context) - SysUtils.DpToPx(context, 8 + 8 + 10)) / 2;



    }

    @Override
    public CollectionGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_colt_item, parent, false);


        CollectionGridViewHolder viewHolder = new CollectionGridViewHolder(view);


        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(CollectionGridViewHolder holder, int position) {

        Collection collection = mDatas.get(position);
        holder.coltName.setText(collection.getColtName());
        holder.likeNum.setText(collection.getColtLikeNum() + "");
        holder.coltToMuseumName.setText(collection.getColtToMuseumName());

        holder.likeClick.setOnClickListener(this);


        holder.coltImage.setLayoutParams(new FrameLayout.LayoutParams(mImageViewWidth, mImageViewWidth));


        requestManager.load(collection.getColtImageURLs().get(0)).into(holder.coltImage);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.likeIcon_coltGrid_item:
                Toast.makeText(context, "I like it", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}


class CollectionGridViewHolder extends RecyclerView.ViewHolder {
    ImageView coltImage; //藏品图片
    LinearLayout likeClick; //喜欢的点击控件

    TextView likeNum; //喜欢的数量
    ImageView likeIcon; //喜欢的图标


    TextView coltName; //藏品名称
    TextView coltToMuseumName; //所属博物馆名称

    LinearLayout linearLayout;

    public CollectionGridViewHolder(View itemView) {
        super(itemView);

        coltImage = (ImageView) itemView.findViewById(R.id.coltImage_coltGrid_item);
        likeClick = (LinearLayout) itemView.findViewById(R.id.coltLike_coltGrid_item);
        likeNum = (TextView) itemView.findViewById(R.id.coltLikeNum_coltGrid_item);
        likeIcon = (ImageView) itemView.findViewById(R.id.likeIcon_coltGrid_item);
        coltName = (TextView) itemView.findViewById(R.id.coltName_coltGrid_item);
        coltToMuseumName = (TextView) itemView.findViewById(R.id.museumName_coltGrid_item);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.colt_item);



    }
}
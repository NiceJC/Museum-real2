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
 * <p>
 * <p>
 * Created by wjc on 2017/2/21.
 */

public class CollectionGridAdapter extends BaseAdapter<CollectionGridAdapter.CollectionGridViewHolder> {


    private int mImageViewWidth;

    private int mItemWidth;
    private int mItemHeight;


    public CollectionGridAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {

        super(context, listDatas, onViewClickListener);
        mImageViewWidth = (SysUtils.getScreenWidth((Activity) context) - SysUtils.DpToPx(context, 16)) / 2;

    }


    @Override
    public CollectionGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectionGridViewHolder(mInflater.inflate(R.layout.grid_colt_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    @Override
    public void onBindViewHolder(CollectionGridViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Collection collection = (Collection) listDatas.get(position);
        holder.coltName.setText(collection.getColtName());
        holder.likeNum.setText(collection.getColtLikeNum() + "");
        if (collection.getColtToMuseum() != null) {
            holder.coltToMuseumName.setText(collection.getColtToMuseum().getMuseumName());


        }

        holder.likeClick.setOnClickListener(new ViewClickListener(onViewClickListener, position, 1));


        holder.coltImage.setLayoutParams(new FrameLayout.LayoutParams(mImageViewWidth, mImageViewWidth));


        requestManager.load(collection.getImage1().getFileUrl() + "!/fxfn/500x500").into(holder.coltImage);


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

    class ViewClickListener implements View.OnClickListener {

        OnViewClickListener onViewClickListener;
        int position;
        int viewType;

        public ViewClickListener(OnViewClickListener onViewClickListener, int position, int viewType) {
            this.onViewClickListener = onViewClickListener;
            this.position = position;
            this.viewType = viewType;
        }

        @Override
        public void onClick(View v) {
            onViewClickListener.onViewClick(position, viewType);

        }
    }
}


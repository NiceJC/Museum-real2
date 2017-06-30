package adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import entity.Collection;
import entity.Comments;
import entity.Museum;
import interfaces.OnItemClickListener;
import jintong.museum2.CommentActivity;
import jintong.museum2.R;

/**
 *
 * Created by wjc on 2017/3/8.
 */

public class ColtListAdapter extends BaseAdapter<ColtListAdapter.ColtListViewHolder> {


    public ColtListAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        super(context, listDatas, onViewClickListener);
    }

    @Override
    public ColtListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new ColtListViewHolder(mInflater.inflate(R.layout.activity_exhibitroom_item, parent, false));

    }

    @Override
    public void onBindViewHolder( ColtListViewHolder holder,  int position) {
        super.onBindViewHolder(holder, position);
        Collection collection = (Collection) listDatas.get(position);

        requestManager.load(collection.getImage1().getFileUrl()+"!/fxfn/1080x540").into(holder.coltImage);
//        holder.likeNum.setText(collection.getColtLikeNum() + "");

        ObjectAnimator.ofFloat(holder.likeMove, "alpha", 1, 0).setDuration(0).start();

        holder.name.setText(collection.getColtName());
        holder.size.setText(collection.getColtSize());
        holder.dynasty.setText(collection.getColtDynasty());

        String coltIntro;
        if(collection.getColtIntru()==null||collection.getColtIntru().equals("")){
            coltIntro="暂无资料";
        }else{
            coltIntro=collection.getColtIntru();
        }
        holder.introduction.setText(coltIntro);


        /**
         * 显示评论的数量
         * 点击后进入评论的详情页
         */
//        holder.commentNum.setText(collection.getColtCommentNum()+"");
//
//
//        holder.commentClick.setOnClickListener(new ViewClickListener(onViewClickListener,position,1));
//        holder.likeClick.setOnClickListener(new ViewClickListener(onViewClickListener,position,2));

        holder.coltImage.setOnClickListener(new ViewClickListener(onViewClickListener,position,3));

//        holder.commentClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context, CommentActivity.class);
//
//                intent.putExtra("coltID",collection.getObjectId());
//
//                context.startActivity(intent);
//
//                context.overridePendingTransition(R.anim.in_from_right, R.anim.none);
//
//            }
//        });


        //点击你就喜欢上了他  嗯 这是个腊鸡动画 爱看不看
//        holder.likeClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                holder.likeMove.getBackground().setAlpha(155);
//                Toast.makeText(context, "I like" + position, Toast.LENGTH_SHORT).show();
//
//                AnimatorSet set = new AnimatorSet();
//                set.playTogether(
//
//
//                        ObjectAnimator.ofFloat(holder.likeMove, "scaleX", 1, 5),
//                        ObjectAnimator.ofFloat(holder.likeMove, "scaleY", 1, 5),
//
//                        ObjectAnimator.ofFloat(holder.likeMove, "translationX", 0, 30, -30, 0),
//                        ObjectAnimator.ofFloat(holder.likeMove, "translationY", 0, -200),
//                        ObjectAnimator.ofFloat(holder.likeMove, "alpha", 1, 0.7f, 0)
//
//
//                );
//                set.setDuration(1500).start();
//
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }



    class ColtListViewHolder extends RecyclerView.ViewHolder {

        ImageView coltImage; //藏品图片

//        LinearLayout likeClick; //喜欢的点击
//
//        ImageView likeIcon; //喜欢的图标
//
//        TextView likeNum; //喜欢数量

        TextView name; //藏品名称

        TextView size; //尺寸

        TextView dynasty; //朝代

        TextView introduction; //详情介绍

        ImageView likeMove; //用作点赞的动画
//
//        LinearLayout commentClick;//评论的点击
//
//        TextView commentNum;//评论的数量


        public ColtListViewHolder(View itemView) {
            super(itemView);

            coltImage = (ImageView) itemView.findViewById(R.id.museumRoom_item_image);
//            likeClick = (LinearLayout) itemView.findViewById(R.id.likeClick_museumRoom_item);
//            likeIcon = (ImageView) itemView.findViewById(R.id.likeIcon_museumRoom_item);
//            likeNum = (TextView) itemView.findViewById(R.id.coltLikeNum_museumRoom_item);

            name = (TextView) itemView.findViewById(R.id.museumRoom_item_name);
            size = (TextView) itemView.findViewById(R.id.museumRoom_item_size);
            dynasty = (TextView) itemView.findViewById(R.id.museumRoom_item_dynasty);
            introduction = (TextView) itemView.findViewById(R.id.museumRoom_item_intro);

            likeMove = (ImageView) itemView.findViewById(R.id.move_like);
//
//            commentClick = (LinearLayout) itemView.findViewById(R.id.commentClick_museumRoom_item);
//            commentNum = (TextView) itemView.findViewById(R.id.coltCommentNum_museumRoom_item);


        }

    }
    class ViewClickListener implements View.OnClickListener{

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
            onViewClickListener.onViewClick(position,viewType);

        }
    }

}


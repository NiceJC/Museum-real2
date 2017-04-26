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
 * Created by wjc on 2017/3/8.
 */

public class ColtListAdapter extends RecyclerView.Adapter<ColtListViewHolder> {


    private Activity context;
    private List<Collection> datas;
    private RequestManager requestManager;


    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public ColtListAdapter(Activity context, List<Collection> datas) {
        this.context = context;
        this.datas = datas;
        requestManager = Glide.with(context);


    }

    @Override
    public ColtListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_exhibitroom_item, parent, false);
        ColtListViewHolder viewHolder = new ColtListViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ColtListViewHolder holder, final int position) {

        final Collection collection = datas.get(position);

        requestManager.load(collection.getColtImageURLs().get(0)).into(holder.coltImage);
        holder.likeNum.setText(collection.getColtLikeNum() + "");

        ObjectAnimator.ofFloat(holder.likeMove, "alpha", 1, 0).setDuration(0).start();

        holder.name.setText(collection.getColtName());
        holder.size.setText(collection.getColtSize());
        holder.dynasty.setText(collection.getColtDynasty());
        holder.introduction.setText(collection.getColtIntru());

        if (mOnItemClickListener != null) {

            holder.coltImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.coltImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mOnItemClickListener.onItemClick(holder.itemView, position);


                    return false;
                }
            });


        }

        /**
         * 显示评论的数量
         * 点击后进入评论的详情页
         */
        holder.commentNum.setText(collection.getColtCommentNum()+"");
        holder.commentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CommentActivity.class);

                intent.putExtra("coltID",collection.getColtID());
                intent.putExtra("commentType", Comments.COMMENT_TO_COLLECTION);
                context.startActivity(intent);

                context.overridePendingTransition(R.anim.in_from_right, R.anim.none);

            }
        });


        //点击你就喜欢上了他  嗯 这是个腊鸡动画 爱看不看
        holder.likeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.likeMove.getBackground().setAlpha(155);
                Toast.makeText(context, "I like" + position, Toast.LENGTH_SHORT).show();

                AnimatorSet set = new AnimatorSet();
                set.playTogether(


                        ObjectAnimator.ofFloat(holder.likeMove, "scaleX", 1, 5),
                        ObjectAnimator.ofFloat(holder.likeMove, "scaleY", 1, 5),

                        ObjectAnimator.ofFloat(holder.likeMove, "translationX", 0, 30, -30, 0),
                        ObjectAnimator.ofFloat(holder.likeMove, "translationY", 0, -200),
                        ObjectAnimator.ofFloat(holder.likeMove, "alpha", 1, 0.7f, 0)


                );
                set.setDuration(1500).start();


            }
        });


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}


class ColtListViewHolder extends RecyclerView.ViewHolder {

    ImageView coltImage; //藏品图片

    LinearLayout likeClick; //喜欢的点击

    ImageView likeIcon; //喜欢的图标

    TextView likeNum; //喜欢数量

    TextView name; //藏品名称

    TextView size; //尺寸

    TextView dynasty; //朝代

    TextView introduction; //详情介绍

    ImageView likeMove; //用作点赞的动画

    LinearLayout commentClick;//评论的点击

    TextView commentNum;//评论的数量


    public ColtListViewHolder(View itemView) {
        super(itemView);

        coltImage = (ImageView) itemView.findViewById(R.id.museumRoom_item_image);
        likeClick = (LinearLayout) itemView.findViewById(R.id.likeClick_museumRoom_item);
        likeIcon = (ImageView) itemView.findViewById(R.id.likeIcon_museumRoom_item);
        likeNum = (TextView) itemView.findViewById(R.id.coltLikeNum_museumRoom_item);

        name = (TextView) itemView.findViewById(R.id.museumRoom_item_name);
        size = (TextView) itemView.findViewById(R.id.museumRoom_item_size);
        dynasty = (TextView) itemView.findViewById(R.id.museumRoom_item_dynasty);
        introduction = (TextView) itemView.findViewById(R.id.museumRoom_item_intro);

        likeMove = (ImageView) itemView.findViewById(R.id.move_like);

        commentClick = (LinearLayout) itemView.findViewById(R.id.commentClick_museumRoom_item);
        commentNum = (TextView) itemView.findViewById(R.id.coltCommentNum_museumRoom_item);


    }

}

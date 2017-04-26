package adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;



import java.util.List;

import MyView.GlideCircleTransform;
import entity.Comments;
import jintong.museum2.R;

import static jintong.museum2.R.id.comment_text;

/**
 *
 *
 * Created by wjc on 2017/3/15.
 *
 */

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerViewHolder> {

    private List<Comments> mComments;
    private Activity context;
    private RequestManager requestManager;

    public CommentRecyclerAdapter(Activity context,List<Comments> comments) {
        this.mComments=comments;
        this.context=context;
        requestManager = Glide.with(context);


    }

    @Override
    public CommentRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        CommentRecyclerViewHolder viewHolder=new CommentRecyclerViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentRecyclerViewHolder holder, int position) {
        Comments comment=mComments.get(position);

        requestManager.load(comment.getAuthorIconURL()).bitmapTransform(new GlideCircleTransform(context)).into(holder.icon);
        holder.userName.setText(comment.getAuthorName());
        holder.time.setText(comment.getCommentTime()+"");
        holder.contentText.setText(comment.getCommentText());




    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }


}


class CommentRecyclerViewHolder extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView userName;
    TextView time;
    TextView contentText;



    public CommentRecyclerViewHolder(View itemView) {
        super(itemView);
        icon= (ImageView) itemView.findViewById(R.id.comment_user_icon);
        userName= (TextView) itemView.findViewById(R.id.comment_username);
        time= (TextView) itemView.findViewById(R.id.comment_time);
        contentText= (TextView) itemView.findViewById(comment_text);


    }
}
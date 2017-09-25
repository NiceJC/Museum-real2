package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import jintong.museum2.CollectionActivity;
import model.Comments;
import jintong.museum2.R;

import static util.ParameterBase.COLT_ID;

/**
 * Created by wjc on 2017/6/19.
 */

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentsListViewHolder> {

    private List<Comments> commentsList;
    private Activity context;
    private RequestManager requestManager;


    public CommentsListAdapter(List<Comments> commentsList, Context context) {
        this.commentsList = commentsList;
        this.context = (Activity) context;
        this.requestManager = Glide.with(context);
    }

    @Override
    public CommentsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentsListViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item,parent,false));
    }

    @Override
    public void onBindViewHolder(CommentsListViewHolder holder, int position) {
        final Comments comments =  commentsList.get(position);
        requestManager.load(comments.getCollection().getImage1().getFileUrl()+ "!/fxfn/200x200").into(holder.coltImage);
        holder.coltName.setText(comments.getCollection().getColtName());
        holder.commentText.setText(comments.getCommentText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CollectionActivity.class);
                intent.putExtra(COLT_ID,comments.getCollection().getObjectId());
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.in_from_right,R.anim.none);
            }
        });




    }


    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class CommentsListViewHolder extends  RecyclerView.ViewHolder{

        ImageView coltImage;
        TextView coltName;
        TextView commentText;


        public CommentsListViewHolder(View itemView) {
            super(itemView);
            coltImage= (ImageView) itemView.findViewById(R.id.news_item_colt_image);
            coltName= (TextView) itemView.findViewById(R.id.news_item_colt_name);
            commentText= (TextView) itemView.findViewById(R.id.news_item_comment);
        }
    }


}

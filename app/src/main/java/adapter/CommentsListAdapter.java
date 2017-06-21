package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.w3c.dom.Text;

import java.util.List;

import entity.Comments;
import jintong.museum2.R;

/**
 * Created by wjc on 2017/6/19.
 */

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentsListViewHolder> {

    private List<Object> commentsList;
    private Context context;
    private RequestManager requestManager;


    public CommentsListAdapter(List<Object> commentsList, Context context) {
        this.commentsList = commentsList;
        this.context = context;
        this.requestManager = Glide.with(context);
    }

    @Override
    public CommentsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentsListViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item,parent,false));
    }

    @Override
    public void onBindViewHolder(CommentsListViewHolder holder, int position) {
        Comments comments = (Comments) commentsList.get(position);
        requestManager.load(comments.getCollection().getImage1().getFileUrl()+ "!/fxfn/200x00").into(holder.coltImage);
        holder.coltName.setText(comments.getCollection().getColtName());
        holder.commentText.setText(comments.getCommentText());

    }


    @Override
    public int getItemCount() {
        return 0;
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

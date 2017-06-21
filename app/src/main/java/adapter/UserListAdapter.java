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

import java.util.List;

import MyView.GlideCircleTransform;
import entity.User;
import interfaces.OnItemClickListener;
import jintong.museum2.R;

/**
 * Created by wjc on 2017/6/19.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {


    private List<User> userList;
    private RequestManager requestManager;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public UserListAdapter(List<User> userList,  Context context) {
        this.userList = userList;
        this.context = context;
        this.requestManager = Glide.with(context);

    }


    public void setClickListener( OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserListViewHolder(LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, final int position) {
       User user=userList.get(position);

        requestManager
                .load(user.getPortraitURL())
                .transform(new GlideCircleTransform(context))
                .into(holder.imageView);

        holder.userName.setText(user.getNickName());

        if(user.getUserInfo()==null||user.getUserInfo().equals("")){
            holder.userInfo.setText("这家伙很懒，啥也没留下");
        }else{
            holder.userInfo.setText(user.getUserInfo());

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    class UserListViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView userName;
        TextView userInfo;


        public UserListViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.userListItem_icon);
            userName = (TextView) itemView.findViewById(R.id.userListItem_userName);
            userInfo = (TextView) itemView.findViewById(R.id.userListItem_info);


        }
    }


}

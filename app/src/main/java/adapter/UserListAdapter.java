package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import myView.GlideCircleTransform;
import model.User;
import jintong.museum2.R;

/**
 *
 * Created by wjc on 2017/6/19.
 */

public class UserListAdapter extends BaseAdapter<UserListAdapter.UserListViewHolder> {


    public UserListAdapter(Context context, List<Object> listDatas) {
        super(context, listDatas);
    }


    public void setClickListener( OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserListViewHolder(mInflater.inflate(R.layout.user_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        User user= (User) listDatas.get(position);

        requestManager
                .load(user.getPortraitURL())
                .transform(new GlideCircleTransform(context))
                .into(holder.imageView);

        holder.userName.setText(user.getNickName());

        if(user.getShortIntroducon()==null||user.getShortIntroducon().equals("")){
            holder.userInfo.setText("这家伙很懒，啥也没留下");
        }else{
            holder.userInfo.setText(user.getShortIntroducon());

        }

    }

    @Override
    public int getItemCount() {
        return listDatas.size();
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

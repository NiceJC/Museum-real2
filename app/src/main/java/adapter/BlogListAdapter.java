package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobSocialUtil;
import MyView.GlideCircleTransform;
import MyView.GridImageView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import entity.Blog;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.BlogActivity;
import jintong.museum2.R;
import jintong.museum2.ZoomImageActivity;
import util.SysUtils;

/**
 * Created by wjc on 2017/5/22.
 */

public class BlogListAdapter extends BaseAdapter<BlogListAdapter.BlogRecyclerViewHolder> {


    private int everyImageWidth;

    private int gridViewWidth;


    private void initGridViewWidth(BlogRecyclerViewHolder holder, Blog blog) {

        everyImageWidth = (SysUtils.getScreenWidth((Activity) context) - SysUtils.DpToPx(context, 30)) / 3;

        switch (blog.getImageURLs().size()) {
            case 1:
                gridViewWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
                holder.gridImageView.setNumColumns(1);
                break;
            case 2:
            case 4:
                gridViewWidth = everyImageWidth * 2 + SysUtils.DpToPx(context, 5);
                holder.gridImageView.setNumColumns(2);
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:

                gridViewWidth = everyImageWidth * 3 + SysUtils.DpToPx(context, 10);

                holder.gridImageView.setNumColumns(3);
                break;

            default:
                break;

        }
    }


    public BlogListAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        super(context, listDatas, onViewClickListener);
    }

    @Override
    public BlogRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new BlogRecyclerViewHolder(mInflater.inflate(R.layout.blog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final BlogRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);


        final Blog blog = (Blog) listDatas.get(position);

        requestManager
                .load(blog.getAuthor().getPortraitURL())
                .transform(new GlideCircleTransform(context))
                .into(holder.userIcon);

        holder.time.setText(blog.getCreatedAt());
        holder.userName.setText(blog.getAuthor().getNickName());
        holder.content.setText(blog.getContentText());

        //点赞 点赞或者取消点赞 变色，改数字，传后台

        holder.commentNum.setText(blog.getCommentNums() + "");

        if(blog.getLiked()==null){
            getPraise(listDatas,position);

        }else{
            holder.praiseIcon.setSelected(blog.getLiked());

            holder.praiseNum.setText(blog.getLikeNum()+"");

        }



        //点击头像  1
        holder.userIcon.setOnClickListener(new ViewClickListener(onViewClickListener, position, 1));

        //点击评论 2
        holder.commentIcon.setOnClickListener(new ViewClickListener(onViewClickListener, position, 2));


        holder.praiseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ifLike) {
//                    holder.praiseIcon.setSelected(false);
//                    holder.praiseNum.setText((praiseNumber - 1) + "");
//                    BmobSocialUtil.getInstance(context).cancelLikeBlog(blog.getObjectId());
//                } else {
//
//                    holder.praiseIcon.setSelected(true);
//                    holder.praiseNum.setText((praiseNumber + 1) + "");
//                    BmobSocialUtil.getInstance(context).likeBlog(blog.getObjectId());
//                }

            }
        });


        //当无图片需要显示时  直接返回
        if (blog.getImageURLs() == null || blog.getImageURLs().size() == 0) {

            holder.gridImageView.setAdapter(null);
            return;
        }


        initGridViewWidth(holder, blog);


        holder.gridImageView.setLayoutParams(new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

        holder.gridImageView.setAdapter(new ImageGridViewAdapter((Activity) context, blog.getImageURLs()));

        holder.gridImageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ZoomImageActivity.class);

                intent.putStringArrayListExtra("imageURLs", (ArrayList<String>) blog.getImageURLs());
                intent.putExtra("position", position);

                context.startActivity(intent);

                ((Activity) context).overridePendingTransition(R.anim.in_zoom, R.anim.none);


            }
        });


    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    public void getPraise(List<Object> list, final int position){

        final Blog blog= (Blog) list.get(position);


        BmobSocialUtil bmobSocialUtil=BmobSocialUtil.getInstance(context);
        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                List<User> userList= (List<User>) Obj;
                if(userList!=null&&userList.size()!=0){

                    boolean isLiked=false;
                    for (User user :userList) {
                        if(user.getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())){
                            isLiked=true;
                            break;
                        }
                    }

                    blog.setLiked(isLiked);

                    blog.setLikeNum(userList.size());

                }else{

                    blog.setLiked(false);

                    blog.setLikeNum(0);
                }
                notifyItemChanged(position);
            }
            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobSocialUtil.getBlogLikesByID(blog.getObjectId());


    }



    class BlogRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView userIcon; //  发布人头像
        TextView userName; // 发布人用户名
        TextView time; //  发布时间
        LinearLayout nameAndTime;
        TextView content; //文字内容
        GridImageView gridImageView; // 九宫图

        ImageView praiseIcon; //点赞图标
        TextView praiseNum; //点赞数
        ImageView commentIcon; // 评论图标
        TextView commentNum; // 评论数


        /**
         * 构造方法的参数 就是Item的布局
         */

        public BlogRecyclerViewHolder(View itemView) {
            super(itemView);
            userIcon = (ImageView) itemView.findViewById(R.id.blog_user_icon);
            userName = (TextView) itemView.findViewById(R.id.blog_username);
            time = (TextView) itemView.findViewById(R.id.blog_time);
            nameAndTime = (LinearLayout) itemView.findViewById(R.id.blog_user_nameAndTime);

            content = (TextView) itemView.findViewById(R.id.blog_content_text);
            gridImageView = (GridImageView) itemView.findViewById(R.id.blog_image_grid_view);


            praiseIcon = (ImageView) itemView.findViewById(R.id.blog_praise_icon);
            praiseNum = (TextView) itemView.findViewById(R.id.blog_praise_num);

            commentIcon = (ImageView) itemView.findViewById(R.id.blog_comment_icon);
            commentNum = (TextView) itemView.findViewById(R.id.blog_comment_num);


        }
    }



    class ViewClickListener implements View.OnClickListener {

        BaseAdapter.OnViewClickListener onViewClickListener;
        int position;
        int viewType;

        public ViewClickListener(BaseAdapter.OnViewClickListener onViewClickListener, int position, int viewType) {
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

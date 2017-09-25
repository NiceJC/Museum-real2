package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobSocialUtil;
import db.MuseumDB;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.MainActivity;
import myView.GlideCircleTransform;
import model.Blog;
import jintong.museum2.R;
import jintong.museum2.ZoomImageActivity;
import util.DateUtils;

/**
 *
 * Created by wjc on 2017/5/22.
 */

public class BlogListAdapter extends BaseAdapter<BlogListAdapter.BlogRecyclerViewHolder> {


    private List<Blog> likedBlogs;

    public BlogListAdapter(Context context, List<Object> listDatas, OnViewClickListener onViewClickListener) {
        super(context, listDatas, onViewClickListener);

    }

    public void putLikedBlogsInDB(List<Blog> likedBlogs) {
        this.likedBlogs = likedBlogs;
    }


    public BlogListAdapter(Context context, List<Object> listDatas) {
        super(context, listDatas);
    }

    //判断当前blog是否已经被点赞了
    public boolean isLiked(List<Blog> blogList, Blog blog) {
        boolean isliked = false;
        if (blogList == null || blogList.size() == 0) {
            return isliked;
        }
        for (Blog blogIn :
                blogList) {
            if (blogIn.getObjectId().equals(blog.getObjectId())) {
                isliked = true;
                break;
            }
        }
        return isliked;
    }


    @Override
    public BlogRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new BlogRecyclerViewHolder(mInflater.inflate(R.layout.blog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final BlogRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);


        final Blog blog = (Blog) listDatas.get(position);

        blog.setLiked(isLiked(likedBlogs, blog));

        requestManager
                .load(blog.getAuthor().getPortraitURL())
                .transform(new GlideCircleTransform(context))
                .into(holder.userIcon);

        holder.time.setText(DateUtils.geRegularTime(blog.getCreatedAt()));
        holder.userName.setText(blog.getAuthor().getNickName());
        holder.content.setText(blog.getContentText());

        //点赞 点赞或者取消点赞 变色，改数字，传后台

        holder.commentNum.setText(blog.getCommentNums() + "");


        holder.praiseIcon.setSelected(blog.getLiked());

        holder.praiseNum.setText(blog.getLikeNum() + "");


        //点击头像  1
        holder.userIcon.setOnClickListener(new ViewClickListener(onViewClickListener, position, 1));

        //点击评论 2
        holder.commentClick.setOnClickListener(new ViewClickListener(onViewClickListener, position, 2));


        holder.praiseclick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                v.setClickable(false);

                if (holder.praiseIcon.isSelected()) {

                    //取消点赞  先给反应，网络返回把结果存进本地数据库
                    holder.praiseIcon.setSelected(false);
                    holder.praiseNum.setText(blog.getLikeNum()-1 + "");

                    BmobSocialUtil bmobsocialUtil = BmobSocialUtil.getInstance(context);
                    bmobsocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                        @Override
                        public void onSuccess(Object Obj) {
//                            int likeNum = (int) Obj;
                            MuseumDB museumDB = MuseumDB.getInstance(context);
                            museumDB.deleteLikedBlog(blog);


//                            blog.setLiked(false);
//                            blog.setLikeNum(likeNum);

                            v.setClickable(true);
                        }

                        @Override
                        public void onFail(Object Obj) {

                            v.setClickable(true);
                        }
                    });
                    bmobsocialUtil.cancelLikeBlog(blog.getObjectId());

                } else {
                    //点赞  先给反应，网络返回不管

                    holder.praiseIcon.setSelected(true);
                    holder.praiseNum.setText(blog.getLikeNum()+1 + "");


                    BmobSocialUtil bmobsocialUtil = BmobSocialUtil.getInstance(context);
                    bmobsocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                        @Override
                        public void onSuccess(Object Obj) {
//                            int likeNum = (int) Obj;

//                            blog.setLiked(true);
//                            blog.setLikeNum(likeNum);

                            MuseumDB museumDB = MuseumDB.getInstance(context);
                            museumDB.saveLikedBlog(blog);
                            v.setClickable(true);
                        }

                        @Override
                        public void onFail(Object Obj) {
                            v.setClickable(true);
                        }
                    });
                    bmobsocialUtil.likeBlog(blog.getObjectId());


                }
            }
        });


        //当无图片需要显示时  直接返回
        if (blog.getImageURLs() == null || blog.getImageURLs().size() == 0) {
            holder.nineRecyclerView.setAdapter(null);
            return;
        }
        List<Object> imageUrls = new ArrayList<Object>();

        imageUrls.addAll(blog.getImageURLs());

        holder.nineRecyclerView.setFocusable(false);

        NineImageRecyAdapter adapter = new NineImageRecyAdapter(context, imageUrls);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, ZoomImageActivity.class);

                intent.putStringArrayListExtra("imageURLs", (ArrayList<String>) blog.getImageURLs());
                intent.putExtra("position", position);

                context.startActivity(intent);

                ((Activity) context).overridePendingTransition(R.anim.in_zoom, R.anim.none);

            }
        });
        holder.nineRecyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager;
        if (blog.getImageURLs().size() == 1) {
            layoutManager = new GridLayoutManager(context, 2);
        } else {
            layoutManager = new GridLayoutManager(context, 3);
        }
        holder.nineRecyclerView.setLayoutManager(layoutManager);


    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    class BlogRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView userIcon; //  发布人头像
        TextView userName; // 发布人用户名
        TextView time; //  发布时间
        LinearLayout nameAndTime;
        TextView content; //文字内容
        RecyclerView nineRecyclerView; // 九宫图
        ImageView praiseIcon; //点赞图标
        TextView praiseNum; //点赞数
        LinearLayout praiseclick; //点赞点击区域

        ImageView commentIcon; // 评论图标
        TextView commentNum; // 评论数
        LinearLayout commentClick; //评论点击区域


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
            nineRecyclerView = (RecyclerView) itemView.findViewById(R.id.blog_image_grid_view);


            praiseIcon = (ImageView) itemView.findViewById(R.id.blog_praise_icon);
            praiseNum = (TextView) itemView.findViewById(R.id.blog_praise_num);
            praiseclick = (LinearLayout) itemView.findViewById(R.id.blog_praise_click);

            commentIcon = (ImageView) itemView.findViewById(R.id.blog_comment_icon);
            commentNum = (TextView) itemView.findViewById(R.id.blog_comment_num);
            commentClick = (LinearLayout) itemView.findViewById(R.id.blog_comment_click);


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

package adapter;

import android.app.Activity;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import MyView.GlideCircleTransform;
import MyView.GridImageView;
import MyView.ImageViewInCircle;
import entity.Blog;
import interfaces.OnItemClickListener;
import jintong.museum2.R;
import jintong.museum2.ZoomImageActivity;
import util.SysUtils;

/**
 *
 *
 * 社区页面 RecyclerView的Adapter
 * Created by wjc on 2017/2/15.
 */
public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerViewHolder>{

    private LayoutInflater mInflater;

    private Activity context;
    private List<Blog> mDatas;
    private  RequestManager requestManager;

    private int everyImageWidth;

    private int gridViewWidth;

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public BlogRecyclerAdapter(Activity context, List<Blog> datas){

        this.context=context;

        this.mDatas=datas;
        this.requestManager=Glide.with(context);
        mInflater=LayoutInflater.from(context);


    }


    private void initGridViewWidth(BlogRecyclerViewHolder holder, Blog blog) {


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


    //创建ViewHolder
    @Override
    public BlogRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view=mInflater.inflate(R.layout.blog_item,parent,false);
        BlogRecyclerViewHolder viewHolder=new BlogRecyclerViewHolder(view);

        return viewHolder;
    }

    //绑定ViewHolder的数据
    @Override
    public void onBindViewHolder(final BlogRecyclerViewHolder holder, final int position) {

//        holder.userIcon.setImageBitmap((Bitmap) mDatas.get(position).get("icon"));

        final Blog blog=mDatas.get(position);



        requestManager
                .load(blog.getAuthor().getPortraitURL())
                .transform(new GlideCircleTransform(context))
                .into(holder.userIcon);

        holder.time.setText(blog.getCreatedAt());

        holder.userName.setText(blog.getAuthor().getNickName());
        holder.content.setText(blog.getContentText());
/**
 * 待改******88
 */
        holder.watchIcon.setSelected(true);
        holder.praiseIcon.setSelected(false);
        holder.commentNum.setText(blog.getCommentNums()+"");
        holder.praiseNum.setText(blog.getPraiseNums()+"");


        holder.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,mDatas.get(position).getAuthor().getNickName(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.watchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//        holder.time.setOnClickListener(this);
//        holder.userName.setOnClickListener(this);
//


        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.OnItemLongClick(holder.itemView,position);


                    return false;
                }
            });

        }


        //当无图片需要显示时  直接返回
        if(blog.getImageURLs()==null||blog.getImageURLs().size()==0){

            holder.gridImageView.setAdapter(null);
            return;
        }

        everyImageWidth = (SysUtils.getScreenWidth(context) - SysUtils.DpToPx(context, 30)) / 3;

        initGridViewWidth(holder,blog);



        holder.gridImageView.setLayoutParams(new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

        holder.gridImageView.setAdapter(new ImageGridViewAdapter(context, blog.getImageURLs()));

        holder.gridImageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,ZoomImageActivity.class);

                intent.putStringArrayListExtra("imageURLs",(ArrayList<String>) blog.getImageURLs());
                intent.putExtra("position",position);

                context.startActivity(intent);

                context.overridePendingTransition(R.anim.in_zoom, R.anim.none);


            }
        });







    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }



    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//
//        }
//
//
//
//f
//    }


class BlogRecyclerViewHolder extends RecyclerView.ViewHolder{

    ImageView userIcon; //  发布人头像
    TextView userName; // 发布人用户名
    TextView time; //  发布时间
    LinearLayout nameAndTime;
    TextView content; //文字内容
    GridImageView gridImageView; // 九宫图

    ImageView watchIcon; //关注图标
    ImageView praiseIcon; //点赞图标
    TextView praiseNum; //点赞数
    ImageView commentIcon; // 评论图标
    TextView commentNum; // 评论数



    /**
     * 构造方法的参数 就是Item的布局
     */

    public BlogRecyclerViewHolder(View itemView) {
        super(itemView);
        userIcon= (ImageView) itemView.findViewById(R.id.blog_user_icon);
        userName= (TextView) itemView.findViewById(R.id.blog_username);
        time= (TextView) itemView.findViewById(R.id.blog_time);
        nameAndTime= (LinearLayout) itemView.findViewById(R.id.blog_user_nameAndTime);

        content= (TextView) itemView.findViewById(R.id.blog_content_text);
        gridImageView= (GridImageView) itemView.findViewById(R.id.blog_image_grid_view);

        watchIcon= (ImageView) itemView.findViewById(R.id.blog_watch_icon);

        praiseIcon= (ImageView) itemView.findViewById(R.id.blog_praise_icon);
        praiseNum= (TextView) itemView.findViewById(R.id.blog_praise_num);

        commentIcon= (ImageView) itemView.findViewById(R.id.blog_comment_icon);
        commentNum= (TextView) itemView.findViewById(R.id.blog_comment_num);




    }
}
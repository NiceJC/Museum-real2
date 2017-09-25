package jintong.museum2;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import adapter.BaseAdapter;
import adapter.NineImageRecyAdapter;
import bmobUtils.BmobComment;
import bmobUtils.BmobSocialUtil;
import bmobUtils.BmobUserRelation;
import db.MuseumDB;
import myView.GlideCircleTransform;
import myView.GridImageView;
import adapter.CommentRecyclerAdapter;
import adapter.ImageGridViewAdapter;
import cn.bmob.v3.BmobUser;
import model.Blog;
import model.Comments;
import model.User;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import util.DateUtils;
import util.SysUtils;
import util.ToastUtils;

import static model.Comments.COMMENT_TO_BLOG;
import static util.ParameterBase.BLOG_ID;
import static util.ParameterBase.BLOG_ISLIKED;

/**
 * 发布状态的详情页
 * Created by wjc on 2017/3/15.
 */

public class BlogActivity extends BaseActivity implements View.OnClickListener {
    private RequestManager requestManager;

    private int everyImageWidth;

    private int gridViewWidth;

    private OnItemClickListener mOnItemClickListener;


    private ImageView back;
    private RecyclerView recyclerView;
    private EditText newCommentText;
    private ImageView newCommentSend;

    private int height; //保存测量得到的软键盘高度
    private boolean haveChanged = false;


    private LinearLayout editBar;


    private ImageView userIcon; //  发布人头像
    private TextView userName; // 发布人用户名
    private TextView time; //  发布时间
    private LinearLayout nameAndTime;
    private TextView content; //文字内容
    private RecyclerView imageRecyclerView; // 九宫图

    private ImageView watchIcon; //关注图标
    private ImageView praiseIcon; //点赞图标
    private TextView praiseNum; //点赞数
    private ImageView commentIcon; // 评论图标
    private TextView commentNum; // 评论数
    private LinearLayout praiseClick;
    private LinearLayout commentClick;


    private ImageGridViewAdapter adapter;
    private CommentRecyclerAdapter commentListAdapter;
    private Blog blog;
    private String blog_ID;
    private User user;
    private boolean isLiked;
    private List<Comments> commentList = new ArrayList<Comments>();

    private LoadToast loadToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_blog);


        initView();
        initData();
        initEvents();


    }

    private void initView() {


        userIcon = (ImageView) findViewById(R.id.blog_user_icon);
        userName = (TextView) findViewById(R.id.blog_username);
        time = (TextView) findViewById(R.id.blog_time);
        nameAndTime = (LinearLayout) findViewById(R.id.blog_user_nameAndTime);

        content = (TextView) findViewById(R.id.blog_content_text);
        imageRecyclerView = (RecyclerView) findViewById(R.id.blog_image_rec_view);
        imageRecyclerView.setNestedScrollingEnabled(false);

        watchIcon = (ImageView) findViewById(R.id.blog_watch_icon);

        praiseIcon = (ImageView) findViewById(R.id.blog_praise_icon);
        praiseNum = (TextView) findViewById(R.id.blog_praise_num);
        praiseClick = (LinearLayout) findViewById(R.id.blog_praise_click);

        commentIcon = (ImageView) findViewById(R.id.blog_comment_icon);
        commentNum = (TextView) findViewById(R.id.blog_comment_num);
        commentClick = (LinearLayout) findViewById(R.id.blog_comment_click);


        back = (ImageView) findViewById(R.id.blog_a_back);
        recyclerView = (RecyclerView) findViewById(R.id.blog_a_comment_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        newCommentText = (EditText) findViewById(R.id.blog_a_comment_text);

        newCommentSend = (ImageView) findViewById(R.id.blog_a_comment_commit);
        editBar = (LinearLayout) findViewById(R.id.blog_a_editText_bar);

        commentListAdapter = new CommentRecyclerAdapter(this, commentList);
        recyclerView.setAdapter(commentListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

    }

    private void initData() {

        blog_ID = getIntent().getStringExtra(BLOG_ID);

        isLiked = getIntent().getBooleanExtra(BLOG_ISLIKED, false);
        requestManager = Glide.with(this);

        getBlogFromServer(blog_ID);
        getCommentsFromServer(blog_ID);

    }

    private void setData() {


        requestManager
                .load(blog.getAuthor().getPortraitURL())
                .transform(new GlideCircleTransform(this))
                .bitmapTransform(new GlideCircleTransform(this))
                .into(userIcon);

        time.setText(DateUtils.geRegularTime(blog.getCreatedAt()));

        userName.setText(blog.getAuthor().getNickName());
        content.setText(blog.getContentText());


        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(BlogActivity.this);
        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                List<User> userList = (List<User>) Obj;
                if (userList != null && userList.size() != 0) {

                    boolean isLiked = false;
                    for (User user : userList) {
                        if (user.getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                            isLiked = true;
                            break;
                        }
                    }

                    praiseIcon.setSelected(isLiked);

                    praiseNum.setText(userList.size() + "");

                } else {
                    praiseIcon.setSelected(false);

                    praiseNum.setText(0);
                }
            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobSocialUtil.getBlogLikesByID(blog_ID);


//        watchIcon.setSelected(isLiked);
//        praiseIcon.setSelected(false);
        commentNum.setText(blog.getCommentNums() + "");
//        praiseNum.setText(blog.getPraiseNums() + "");


        //当无图片需要显示时  直接返回
        if (blog.getImageURLs() == null || blog.getImageURLs().size() == 0) {

            return;
        }

        List<Object> imageUrls = new ArrayList<Object>();
        imageUrls.addAll(blog.getImageURLs());

        imageRecyclerView.setFocusable(false);

        NineImageRecyAdapter adapter = new NineImageRecyAdapter(this, imageUrls);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(BlogActivity.this, ZoomImageActivity.class);

                intent.putStringArrayListExtra("imageURLs", (ArrayList<String>) blog.getImageURLs());
                intent.putExtra("position", position);

                startActivity(intent);

                overridePendingTransition(R.anim.in_zoom, R.anim.none);

            }
        });
        imageRecyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager;
        if (blog.getImageURLs().size() == 1) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 3);
        }
        imageRecyclerView.setLayoutManager(layoutManager);

    }


    private void initEvents() {


        back.setOnClickListener(this);

        newCommentSend.setOnClickListener(this);

        commentClick.setOnClickListener(this);
        praiseClick.setOnClickListener(this);
        watchIcon.setOnClickListener(this);

    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {


            case R.id.blog_a_back:
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);
                break;
            case R.id.blog_comment_click:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;

            case R.id.blog_watch_icon:

                boolean isWatched=watchIcon.isSelected();
                watchStateChange(isWatched);

                break;
            case R.id.blog_praise_click:
                v.setClickable(false);

                if (blog.getLiked()) {
                    //取消点赞  先给反应，网络返回再做修正
                    praiseIcon.setSelected(false);
                    praiseNum.setText(blog.getLikeNum() - 1 + "");
                    BmobSocialUtil bmobsocialUtil = BmobSocialUtil.getInstance(this);
                    bmobsocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                        @Override
                        public void onSuccess(Object Obj) {
                            int likeNum = (int) Obj;
                            MuseumDB museumDB = MuseumDB.getInstance(BlogActivity.this);
                            museumDB.deleteLikedBlog(blog);


                            //取消点赞
                            praiseIcon.setSelected(false);
                            praiseNum.setText(likeNum + "");

                            blog.setLiked(false);
                            blog.setLikeNum(likeNum);

                            v.setClickable(true);
                        }

                        @Override
                        public void onFail(Object Obj) {

                            v.setClickable(true);
                        }
                    });
                    bmobsocialUtil.cancelLikeBlog(blog.getObjectId());

                } else {
                    //点赞  先给反应，网络返回再做修正

                    praiseIcon.setSelected(true);
                    praiseNum.setText(blog.getLikeNum() + 1 + "");

                    BmobSocialUtil bmobsocialUtil = BmobSocialUtil.getInstance(this);
                    bmobsocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                        @Override
                        public void onSuccess(Object Obj) {
                            int likeNum = (int) Obj;
                            praiseIcon.setSelected(true);
                            praiseNum.setText(likeNum + "");

                            blog.setLiked(true);
                            blog.setLikeNum(likeNum);

                            MuseumDB museumDB = MuseumDB.getInstance(BlogActivity.this);
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

                break;

            case R.id.blog_a_comment_commit:
                loadToast = ToastUtils.getLoadingToast(this);
                loadToast.show();
                String content = newCommentText.getText().toString();
                BmobComment bmobComment = BmobComment.getInstance(BlogActivity.this);
                bmobComment.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                    @Override
                    public void onSuccess(Object Obj) {

                        loadToast.success();
                        int commentCount = (int) Obj;

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


                        newCommentText.setText("");
                        newCommentText.clearFocus();
                        commentNum.setText(commentCount + "");
                        getCommentsFromServer(blog_ID);
                    }

                    @Override
                    public void onFail(Object Obj) {

                        loadToast.error();
                    }
                });
                bmobComment.postComment(COMMENT_TO_BLOG, blog_ID, content);

                break;

            default:
                break;
        }


    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none, R.anim.out_to_right);

    }

    //获取Blog的详细信息
    public void getBlogFromServer(final String blogID) {

        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(BlogActivity.this);
        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                blog = (Blog) Obj;
                blog.setLiked(isLiked);
                user=blog.getAuthor();
                setData();
                checkIfWatch(user.getObjectId());
            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobSocialUtil.getBlogByID(blogID);


    }

    //获取Blog下的所有评论
    public void getCommentsFromServer(String blogID) {

        BmobComment bmobComment = BmobComment.getInstance(BlogActivity.this);
        bmobComment.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Comments> commentses = (List<Comments>) Obj;
                commentList.clear();
                commentList.addAll(commentses);
                commentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobComment.getCommentToBlog(blogID);
    }

    public void checkIfWatch(final String userID) {

        if (userID.equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
            watchIcon.setVisibility(View.GONE);
        }

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Boolean isWatched = MuseumDB.getInstance(BlogActivity.this).getIfFollwed(userID);

                return isWatched;

            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                watchIcon.setSelected((Boolean) o);

            }
        };

        asyncTask.execute();


    }
    public void watchStateChange(final boolean isWatched) {


        BmobUserRelation bmobUserRelation = BmobUserRelation.getInstance(this);
        bmobUserRelation.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                if (isWatched) {
                    watchIcon.setSelected(false);
                    ToastUtils.toast(BlogActivity.this, "已取消关注");
                } else {
                    watchIcon.setSelected(true);
                    ToastUtils.toast(BlogActivity.this, "关注成功");
                }
            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        if (isWatched) {
            bmobUserRelation.unWatchUsers(BmobUser.getCurrentUser(User.class), user);

        } else {
            bmobUserRelation.watchUsers(BmobUser.getCurrentUser(User.class), user);

        }


    }

}

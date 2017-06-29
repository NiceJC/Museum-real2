package jintong.museum2;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobComment;
import BmobUtils.BmobSocialUtil;
import MyView.GlideCircleTransform;
import MyView.GridImageView;
import adapter.CommentRecyclerAdapter;
import adapter.ImageGridViewAdapter;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import entity.Blog;
import entity.Comments;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import util.DateUtils;
import util.SysUtils;
import util.ToastUtils;

import static entity.Comments.COMMENT_TO_BLOG;
import static util.ParameterBase.BLOG_ID;

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
    private GridImageView gridImageView; // 九宫图

    private ImageView watchIcon; //关注图标
    private ImageView praiseIcon; //点赞图标
    private TextView praiseNum; //点赞数
    private ImageView commentIcon; // 评论图标
    private TextView commentNum; // 评论数

    private ImageGridViewAdapter adapter;
    private CommentRecyclerAdapter commentListAdapter;
    private Blog blog;
    private String blog_ID;
    private List<Comments> commentList = new ArrayList<Comments>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_blog);
        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();
        initEvents();


    }


    private void initGridViewWidth(Blog blog) {


        switch (blog.getImageURLs().size()) {
            case 1:
                gridViewWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
                gridImageView.setNumColumns(1);
                break;
            case 2:
            case 4:
                gridViewWidth = everyImageWidth * 2 + SysUtils.DpToPx(this, 5);
                gridImageView.setNumColumns(2);
                break;
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:

                gridViewWidth = everyImageWidth * 3 + SysUtils.DpToPx(this, 10);

                gridImageView.setNumColumns(3);
                break;

            default:
                break;

        }


    }

    private void initView() {


        userIcon = (ImageView) findViewById(R.id.blog_user_icon);
        userName = (TextView) findViewById(R.id.blog_username);
        time = (TextView) findViewById(R.id.blog_time);
        nameAndTime = (LinearLayout) findViewById(R.id.blog_user_nameAndTime);

        content = (TextView) findViewById(R.id.blog_content_text);
        gridImageView = (GridImageView) findViewById(R.id.blog_image_grid_view);

        watchIcon = (ImageView) findViewById(R.id.blog_watch_icon);

        praiseIcon = (ImageView) findViewById(R.id.blog_praise_icon);
        praiseNum = (TextView) findViewById(R.id.blog_praise_num);

        commentIcon = (ImageView) findViewById(R.id.blog_comment_icon);
        commentNum = (TextView) findViewById(R.id.blog_comment_num);


        back = (ImageView) findViewById(R.id.blog_a_back);
        recyclerView = (RecyclerView) findViewById(R.id.blog_a_comment_recyclerView);
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



        BmobSocialUtil bmobSocialUtil=BmobSocialUtil.getInstance(BlogActivity.this);
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

                   praiseIcon.setSelected(isLiked);

                   praiseNum.setText( userList.size()+ "");

               }else{
                   praiseIcon.setSelected(false);

                   praiseNum.setText(0);
               }
            }
            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobSocialUtil.getBlogLikesByID(blog_ID);



        watchIcon.setSelected(true);
//        praiseIcon.setSelected(false);
        commentNum.setText(blog.getCommentNums() + "");
//        praiseNum.setText(blog.getPraiseNums() + "");


        //当无图片需要显示时  直接返回
        if (blog.getImageURLs() == null || blog.getImageURLs().size() == 0) {

            gridImageView.setAdapter(null);
            return;
        }

        everyImageWidth = (SysUtils.getScreenWidth(this) - SysUtils.DpToPx(this, 30)) / 3;

        initGridViewWidth(blog);


        gridImageView.setLayoutParams(new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

        adapter = new ImageGridViewAdapter(this, blog.getImageURLs());
        gridImageView.setAdapter(adapter);


    }

    private void initEvents() {


        back.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        watchIcon.setOnClickListener(this);
        newCommentSend.setOnClickListener(this);
        setListenerToRootView();
        gridImageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BlogActivity.this, ZoomImageActivity.class);

                intent.putStringArrayListExtra("imageURLs", (ArrayList<String>) blog.getImageURLs());
                intent.putExtra("position", position);

                startActivity(intent);

                overridePendingTransition(R.anim.in_zoom, R.anim.none);


            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blog_a_back:
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);
                break;
            case R.id.blog_watch_icon:
                break;
            case R.id.blog_user_icon:
                break;

            case R.id.blog_a_comment_commit:
                String content = newCommentText.getText().toString();
                BmobComment bmobComment = BmobComment.getInstance(BlogActivity.this);
                bmobComment.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                    @Override
                    public void onSuccess(Object Obj) {

                        ToastUtils.toast(BlogActivity.this, "评论成功");

                        getCommentsFromServer(blog_ID);
                    }

                    @Override
                    public void onFail(Object Obj) {

                    }
                });
                bmobComment.postComment(COMMENT_TO_BLOG, blog_ID, content);

                break;

            default:
                break;
        }


    }


    /**
     * 得到的Rect就是根布局的可视区域，而rootView.bottom是其本应的底部坐标值，
     * 如果差值大于我们预设的值，就可以认定键盘弹起了。这个预设值是键盘的高度的最小值。
     * 这个rootView实际上就是DectorView，通过任意一个View再getRootView就能获得。
     */
    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int heightDiff = rootView.getBottom() - r.bottom;
        height = heightDiff;

        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private void setListenerToRootView() {

        back.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOpen = isKeyboardShown(back.getRootView());


                if (isOpen) {

                    ObjectAnimator.ofFloat(editBar, "translationY", 0, -height).setDuration(100).start();
                    haveChanged = true;

                } else {
                    if (haveChanged) {
                        ObjectAnimator.ofFloat(editBar, "translationY", -height, 0).setDuration(100).start();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none, R.anim.out_to_right);

    }

    //获取Blog的详细信息
    public void getBlogFromServer(String blogID) {

        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(BlogActivity.this);
        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                blog = (Blog) Obj;
                setData();
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


}

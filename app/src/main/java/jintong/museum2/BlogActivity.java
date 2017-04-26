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

import MyView.GlideCircleTransform;
import MyView.GridImageView;
import adapter.CommentRecyclerAdapter;
import adapter.ImageGridViewAdapter;
import entity.Blog;
import entity.Comments;
import interfaces.OnItemClickListener;
import util.SysUtils;

/**
 * 发布状态的详情页
 * Created by wjc on 2017/3/15.
 */

public class BlogActivity extends BaseActivity implements View.OnClickListener {

    private Blog blog;

    private List<Comments> mComments;


    private LayoutInflater mInflater;


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_blog);
        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();

        setData();
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


    }

    private void initData() {

        List<String> usrls=new ArrayList<String>();
        String imgs1 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/11/29/118ec3614022872d80d04add7d795346.jpg";
        String imgs2 = "http://t10.baidu.com/it/u=2565424359,3856609610&fm=58";
        String imgs3 = "http://t10.baidu.com/it/u=374721516,1427740298&fm=58";
        String imgs4 = "http://t11.baidu.com/it/u=3158457091,3429860559&fm=58";
        String imgs5 = "http://t12.baidu.com/it/u=732128477,3149312025&fm=58";
        String imgs6 = "http://t11.baidu.com/it/u=2722915642,3232472693&fm=58";
        usrls.add(imgs1);
        usrls.add(imgs2);
        usrls.add(imgs3);
        usrls.add(imgs4);
        usrls.add(imgs5);
        usrls.add(imgs6);

        blog = new Blog();



        blog.setIconURL("http://bmob-cdn-4183.b0.upaiyun.com/2017/02/20/2dcd5037401d841b8026fb38b4847ac4.jpg");
        blog.setCommentNums(66);
        blog.setContentText("hahhah哈哈哈  好开心呀 嘿嘿嘿嘿嘿 ");
        blog.setPraised(false);
        blog.setWatched(true);
        blog.setPraiseNums(99);
        blog.setUserName("阿超");
        blog.setTime("60年前");
        blog.setImageURLs(usrls);






        mComments = new ArrayList<Comments>();

        for (int i = 0; i < 5; i++) {
            Comments comment = new Comments();
            comment.setAuthorIconURL("http://bmob-cdn-4183.b0.upaiyun.com/2017/02/20/2dcd5037401d841b8026fb38b4847ac4.jpg");

            comment.setCommentID(666);
            comment.setAuthorName("阿喂大帅比");
            comment.setCommentText("嗯，这个宝贝很漂脸阿  年代嗨非常救援，一看就很之前阿 ，很值钱  哈哈哈哈哈");

            comment.setCommentTime(9080);
            mComments.add(comment);


        }



        requestManager=Glide.with(this);
    }

    private void setData() {


        requestManager
                .load(blog.getIconURL())
                .transform(new GlideCircleTransform(this))
                .bitmapTransform(new GlideCircleTransform(this))
                .into(userIcon);

        time.setText(blog.getTime());

        userName.setText(blog.getUserName());
        content.setText(blog.getContentText());

        watchIcon.setSelected(blog.isWatched());
        praiseIcon.setSelected(blog.isPraised());
        commentNum.setText(blog.getCommentNums() + "");
        praiseNum.setText(blog.getPraiseNums() + "");


        recyclerView.setAdapter(new CommentRecyclerAdapter(this, mComments));
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


        //当无图片需要显示时  直接返回
        if (blog.getImageURLs() == null || blog.getImageURLs().size() == 0) {

            gridImageView.setAdapter(null);
            return;
        }

        everyImageWidth = (SysUtils.getScreenWidth(this) - SysUtils.DpToPx(this, 30)) / 3;

        initGridViewWidth(blog);


        gridImageView.setLayoutParams(new LinearLayout.LayoutParams(gridViewWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

        gridImageView.setAdapter(new ImageGridViewAdapter(this, blog.getImageURLs()));


    }

    private void initEvents() {


        back.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        watchIcon.setOnClickListener(this);
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
}

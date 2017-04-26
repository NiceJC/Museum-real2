package jintong.museum2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.CommentRecyclerAdapter;
import entity.Comments;

/**
 * 展示评论
 * Created by wjc on 2017/3/15.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private RecyclerView recyclerView;
    private EditText newCommentText;
    private ImageView newCommentSend;

    private int coltID; //从Intent中读取到的博物馆的ID
    private int commentType; //从Intent中读取到的评论主体的类型


    private int height; //保存测量得到的软键盘高度
    private boolean haveChanged=false;


    private LinearLayout editBar;

    private LinearLayout contentView;

    private List<Comments> mComments;



    private void setListenerToRootView() {

        back.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOpen = isKeyboardShown(back.getRootView());


                if(isOpen){

                    ObjectAnimator.ofFloat(editBar,"translationY",0,-height).setDuration(100).start();
                    haveChanged=true;

                }else{
                    if(haveChanged) {
                        ObjectAnimator.ofFloat(editBar, "translationY", -height, 0).setDuration(100).start();
                    }
                }
            }
        });

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//        requestWindowFeature()


        initView();
        initData();

        setData();
        initEvents();

        recyclerView.setAdapter(new CommentRecyclerAdapter(this, mComments));
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


    }


    private void initView() {

        contentView = (LinearLayout) findViewById(R.id.contentView);

        back = (ImageView) findViewById(R.id.comment_back);
        recyclerView = (RecyclerView) findViewById(R.id.comment_recyclerView);
        newCommentText = (EditText) findViewById(R.id.new_comment_text);

        newCommentSend = (ImageView) findViewById(R.id.new_comment_commit);
        editBar = (LinearLayout) findViewById(R.id.comment_editText_bar);
        coltID = getIntent().getIntExtra("coltID", 0);
        commentType = getIntent().getIntExtra("commentType", 0);

    }

    /**
     * 通过评论主体的ID 以及评论主体的类型查询得到
     */
    private void initData() {
        mComments = new ArrayList<Comments>();
        Log.e("comment  type=", commentType + "");

        for (int i = 0; i < 5; i++) {
            Comments comment = new Comments();
            comment.setAuthorIconURL("http://bmob-cdn-4183.b0.upaiyun.com/2017/02/20/2dcd5037401d841b8026fb38b4847ac4.jpg");

            comment.setCommentID(666);
            comment.setAuthorName("阿喂大帅比");
            comment.setCommentText("嗯，这个宝贝很漂脸阿  年代嗨非常救援，一看就很之前阿 ，很值钱  哈哈哈哈哈");

            comment.setCommentTime(9080);
            mComments.add(comment);


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void setData() {

    }

    private void initEvents() {

        back.setOnClickListener(this);
        newCommentText.setOnClickListener(this);

        newCommentSend.setOnClickListener(this);


        editBar.getRootView();

        setListenerToRootView();

    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none, R.anim.out_to_right);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.comment_back:
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);

                break;
            case R.id.new_comment_text:

                break;

            case R.id.new_comment_commit:
                boolean isOpen = isKeyboardShown(editBar.getRootView());
//                Log.e("measure","");
                Toast.makeText(CommentActivity.this, isOpen + "", Toast.LENGTH_SHORT).show();
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
        height=heightDiff;

        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        return heightDiff > softKeyboardHeight * dm.density;
    }




}

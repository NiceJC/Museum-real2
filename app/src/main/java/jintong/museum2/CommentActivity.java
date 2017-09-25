package jintong.museum2;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobComment;
import adapter.CommentRecyclerAdapter;
import model.Comments;
import interfaces.OnBmobReturnWithObj;

import static model.Comments.COMMENT_TO_BLOG;
import static model.Comments.COMMENT_TO_COLLECTION;
import static model.Comments.COMMENT_TO_EXHIBITION;
import static model.Comments.COMMENT_TO_MUSEUM;

/**
 * 展示评论
 * Created by wjc on 2017/3/15.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private RecyclerView recyclerView;
    private EditText newCommentText;
    private ImageView newCommentSend;

    private String belongToID; //从Intent中读取到的博物馆的ID
    private int commentType; //从Intent中读取到的评论主体的类型


    private int height; //保存测量得到的软键盘高度
    private boolean haveChanged = false;


    private CommentRecyclerAdapter adapter;

    private LinearLayout editBar;

    private LinearLayout contentView;

    private List<Comments> mComments = new ArrayList<Comments>();


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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment);


        initView();
        initData();

        setData();
        initEvents();


    }


    private void initView() {

        contentView = (LinearLayout) findViewById(R.id.contentView);

        back = (ImageView) findViewById(R.id.comment_back);
        recyclerView = (RecyclerView) findViewById(R.id.comment_recyclerView);
        newCommentText = (EditText) findViewById(R.id.new_comment_text);

        newCommentSend = (ImageView) findViewById(R.id.new_comment_commit);
        editBar = (LinearLayout) findViewById(R.id.comment_editText_bar);
        //从Intent中获取参数信息
        belongToID = getIntent().getStringExtra(Comments.COMMENT_BELONG_ID);
        commentType = getIntent().getIntExtra(Comments.COMMENT_TYPE, 0);

        adapter=new CommentRecyclerAdapter(this, mComments);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

    }

    /**
     * 通过评论主体的ID 以及评论主体的类型查询得到
     */
    private void initData() {
        BmobComment bmobComment=BmobComment.getInstance(this);
        bmobComment.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                mComments= (List<Comments>) Obj;

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        switch (commentType) {

            case COMMENT_TO_MUSEUM:

                bmobComment.getCommentToMuseum(belongToID);

                break;
            case COMMENT_TO_EXHIBITION:

                bmobComment.getCommentToExhibition(belongToID);
                break;
            case COMMENT_TO_COLLECTION:
                bmobComment.getCommentToColt(belongToID);
                break;

            case COMMENT_TO_BLOG:
                bmobComment.getCommentToBlog(belongToID);
                break;
            default:
                break;

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
        height = heightDiff;

        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        return heightDiff > softKeyboardHeight * dm.density;
    }



}

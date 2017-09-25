package jintong.museum2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobColt;
import bmobUtils.BmobComment;
import adapter.CommentRecyclerAdapter;
import cn.bmob.v3.BmobUser;
import model.Collection;
import model.Comments;
import model.User;
import interfaces.OnBmobReturnWithObj;
import util.SysUtils;
import util.ToastUtils;

import static model.Comments.COMMENT_TO_COLLECTION;
import static util.ParameterBase.COLT_ID;
import static util.ParameterBase.IMAGE_URLS;


/**
 *
 *
 * Created by wjc on 2017/3/7.
 */

public class CollectionActivity extends BaseActivity {
    private ImageView coltImage; //藏品图片

    private LinearLayout likeClick; //喜欢的点击

    private ImageView likeIcon; //喜欢的图标

    private TextView likeNum; //喜欢数量

    private TextView name; //藏品名称

    private TextView size; //尺寸

    private TextView dynasty; //朝代

    private TextView introduction; //详情介绍

    private ImageView likeMove; //用作点赞的动画





    private ImageView back,commit;

    private EditText editText;

    private Collection collection;

    private RequestManager requestManager;

    private String colt_ID;

    private List<Comments> comments=new ArrayList<Comments>(); //所有评论

    private RecyclerView recyclerView;
    private TextView whenNoData;
    private CommentRecyclerAdapter adapter;

    private int height; //保存测量得到的软键盘高度
    private boolean haveChanged = false;
    private LinearLayout editBar;

    private boolean isLiked=false;
    private int likecount;

    private LoadToast lt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_collection);


        initView();
        initData();
        initEvents();
    }

    private void initView() {



        lt=ToastUtils.getLoadingToast(this);

        coltImage = (ImageView) findViewById(R.id.museumRoom_item_image);
        likeClick = (LinearLayout) findViewById(R.id.likeClick_museumRoom_item);
        likeIcon = (ImageView) findViewById(R.id.likeIcon_museumRoom_item);
        likeNum = (TextView) findViewById(R.id.coltLikeNum_museumRoom_item);

        name = (TextView) findViewById(R.id.museumRoom_item_name);
        size = (TextView) findViewById(R.id.museumRoom_item_size);
        dynasty = (TextView) findViewById(R.id.museumRoom_item_dynasty);
        introduction = (TextView) findViewById(R.id.museumRoom_item_intro);

        likeMove = (ImageView) findViewById(R.id.move_like);

        back= (ImageView) findViewById(R.id.activity_colt_back);

        colt_ID= getIntent().getStringExtra(COLT_ID);
        requestManager= Glide.with(this);

        commit= (ImageView) findViewById(R.id.blog_a_comment_commit);
        editText= (EditText) findViewById(R.id.blog_a_comment_text);

        editBar= (LinearLayout) findViewById(R.id.blog_a_editText_bar);

        recyclerView= (RecyclerView) findViewById(R.id.activity_colt_comments);
        whenNoData= (TextView) findViewById(R.id.when_no_data);
        adapter=new CommentRecyclerAdapter(this,comments);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



    }

    private void initData() {

        getCollectionInfo(colt_ID);

        getAllComments(colt_ID);
    }

    private void setData() {
        getLikedUser(colt_ID);
        incrementHotvalue();
        requestManager.load(collection.getImage1().getFileUrl()+ "!/fxfn/1080x500").into(coltImage);

        ObjectAnimator.ofFloat(likeMove, "alpha", 1, 0).setDuration(0).start();

        name.setText(collection.getColtName());
        size.setText(collection.getColtSize());
        dynasty.setText(collection.getColtDynasty());

        likecount=collection.getColtLikeNum();
        likeNum.setText(likecount+"");
        if(collection.getColtIntru()==null||collection.getColtIntru().equals("")){
            introduction.setText("暂无资料");

        }
        introduction.setText(collection.getColtIntru());

        }




    private void initEvents() {

//        setListenerToRootView();
        coltImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(CollectionActivity.this,ZoomImageActivity.class);


                intent.putStringArrayListExtra(IMAGE_URLS, SysUtils.getURLs(collection));
                intent.putExtra("position",0);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.none);


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none,R.anim.out_to_right);
            }
        });

        //点击你就喜欢上了他  嗯 这是个腊鸡动画 爱看不看
        likeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                likeMove.getBackground().setAlpha(155);

                AnimatorSet set = new AnimatorSet();
                set.playTogether(


                        ObjectAnimator.ofFloat(likeMove, "scaleX", 1, 5),
                        ObjectAnimator.ofFloat(likeMove, "scaleY", 1, 5),

                        ObjectAnimator.ofFloat(likeMove, "translationX", 0, 30, -30, 0),
                        ObjectAnimator.ofFloat(likeMove, "translationY", 0, -200),
                        ObjectAnimator.ofFloat(likeMove, "alpha", 1, 0.7f, 0)
                );
                set.setDuration(1500).start();


                if(isLiked){
                   removeColtToLike();
                }else{
                    addColtToLike();
                }
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                lt.show();
                final String content = editText.getText().toString();
                BmobComment bmobComment = BmobComment.getInstance(CollectionActivity.this);
                bmobComment.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                    @Override
                    public void onSuccess(Object Obj) {

                        lt.success();
                        hideSoftInput();
                        editText.setText("");

                        whenNoData.setVisibility(View.GONE);
                        getAllComments(colt_ID);
                    }

                    @Override
                    public void onFail(Object Obj) {

                        lt.error();
                    }
                });
                bmobComment.postComment(COMMENT_TO_COLLECTION, colt_ID, content);

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none,R.anim.out_to_right);
    }

    public void getCollectionInfo( String coltID){
        BmobColt bmobColt=BmobColt.getInstance(this);
        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                collection= (Collection) Obj;
                setData();
            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobColt.getByColtID(coltID);

    }

    public void getAllComments(String coltID){

        BmobComment bmobComment=BmobComment.getInstance(this);
        bmobComment.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Comments> list= (List<Comments>) Obj;
                if(list==null||list.size()==0){
                    whenNoData.setVisibility(View.VISIBLE);
                }else{

                    comments.clear();
                    comments.addAll(list);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobComment.getCommentToColt(coltID);

    }



    //获取喜欢当前展品的所有用户
    public void getLikedUser(String colt_ID){
        BmobColt bmobColt=BmobColt.getInstance(this);
        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<User> userList= (List<User>) Obj;
                if(userList!=null&&userList.size()!=0){

                    for (User user :userList
                            ) {
                        if(user.getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                            isLiked = true;
                            break;
                        }
                    }
                    likeIcon.setSelected(isLiked);

                }else{
                    likeIcon.setSelected(false);

                }
            }
            @Override
            public void onFail(Object Obj) {
            }
        });
        bmobColt.getLikedUsers(colt_ID);
    }


    //添加喜欢
    private void addColtToLike(){

        BmobColt bmobColt=BmobColt.getInstance(this);


        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                isLiked=true;
                likecount++;
                likeIcon.setSelected(isLiked);
                likeNum.setText(likecount+"");
            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobColt.likeColt(colt_ID);
    }

    //移除喜欢
    private void removeColtToLike(){

        BmobColt bmobColt=BmobColt.getInstance(this);
        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {


                isLiked=false;
                likecount--;
                likeIcon.setSelected(isLiked);
                likeNum.setText(likecount+"");

            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobColt.cancellikeColt(colt_ID);
    }

    private void incrementHotvalue(){
        BmobColt.getInstance(this).incrementHotValue(colt_ID);

    }

    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0) ;
    }



//
//    /**
//     * 得到的Rect就是根布局的可视区域，而rootView.bottom是其本应的底部坐标值，
//     * 如果差值大于我们预设的值，就可以认定键盘弹起了。这个预设值是键盘的高度的最小值。
//     * 这个rootView实际上就是DectorView，通过任意一个View再getRootView就能获得。
//     */
//    private boolean isKeyboardShown(View rootView) {
//        final int softKeyboardHeight = 100;
//        Rect r = new Rect();
//        rootView.getWindowVisibleDisplayFrame(r);
//        int heightDiff = rootView.getBottom() - r.bottom;
//        height = heightDiff;
//
//        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
//        return heightDiff > softKeyboardHeight * dm.density;
//    }
//
//    private void setListenerToRootView() {
//
//        back.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                boolean isOpen = isKeyboardShown(back.getRootView());
//
//
//                if (isOpen) {
//
//                    ObjectAnimator.ofFloat(editBar, "translationY", 0, -height).setDuration(100).start();
//                    haveChanged = true;
//
//                } else {
//                    if (haveChanged) {
//                        ObjectAnimator.ofFloat(editBar, "translationY", -height, 0).setDuration(100).start();
//                    }
//                }
//            }
//        });
//
//    }



}

package jintong.museum2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobColt;
import entity.Collection;
import entity.Comments;
import interfaces.OnBmobReturnWithObj;
import util.SysUtils;

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

    private LinearLayout commentClick;//评论的点击

    private TextView commentNum;//评论的数量

    private ImageView back;

    private Collection collection;

    private RequestManager requestManager;

    private String colt_ID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_collection);
        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();


        initEvents();
    }

    private void initView() {


        coltImage = (ImageView) findViewById(R.id.museumRoom_item_image);
        likeClick = (LinearLayout) findViewById(R.id.likeClick_museumRoom_item);
        likeIcon = (ImageView) findViewById(R.id.likeIcon_museumRoom_item);
        likeNum = (TextView) findViewById(R.id.coltLikeNum_museumRoom_item);

        name = (TextView) findViewById(R.id.museumRoom_item_name);
        size = (TextView) findViewById(R.id.museumRoom_item_size);
        dynasty = (TextView) findViewById(R.id.museumRoom_item_dynasty);
        introduction = (TextView) findViewById(R.id.museumRoom_item_intro);

        likeMove = (ImageView) findViewById(R.id.move_like);

        commentClick = (LinearLayout) findViewById(R.id.commentClick_museumRoom_item);
        commentNum = (TextView) findViewById(R.id.coltCommentNum_museumRoom_item);

        back= (ImageView) findViewById(R.id.activity_colt_back);

        colt_ID= getIntent().getStringExtra(COLT_ID);
        requestManager= Glide.with(this);

    }

    private void initData() {

        getDataFromServer(colt_ID);

    }

    private void setData() {






        requestManager.load(collection.getImage1().getFileUrl()+ "!/fxfn/1080x500").into(coltImage);
        likeNum.setText(collection.getColtLikeNum() + "");
        commentNum.setText(collection.getColtCommentNum()+"");
        ObjectAnimator.ofFloat(likeMove, "alpha", 1, 0).setDuration(0).start();

        name.setText(collection.getColtName());
        size.setText(collection.getColtSize());
        dynasty.setText(collection.getColtDynasty());
        introduction.setText(collection.getColtIntru());



        }




    private void initEvents() {
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
        /**
         * 显示评论的数量
         * 点击后进入评论的详情页
         */

        commentClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CollectionActivity.this, CommentActivity.class);

                intent.putExtra("coltID",collection.getObjectId());
//                intent.putExtra("commentType", Comments.COMMENT_TO_COLLECTION);
                startActivity(intent);

                overridePendingTransition(R.anim.in_from_right, R.anim.none);

            }
        });


        //点击你就喜欢上了他  嗯 这是个腊鸡动画 爱看不看
        likeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                likeMove.getBackground().setAlpha(155);
                Toast.makeText(CollectionActivity.this, "I like" , Toast.LENGTH_SHORT).show();

                AnimatorSet set = new AnimatorSet();
                set.playTogether(


                        ObjectAnimator.ofFloat(likeMove, "scaleX", 1, 5),
                        ObjectAnimator.ofFloat(likeMove, "scaleY", 1, 5),

                        ObjectAnimator.ofFloat(likeMove, "translationX", 0, 30, -30, 0),
                        ObjectAnimator.ofFloat(likeMove, "translationY", 0, -200),
                        ObjectAnimator.ofFloat(likeMove, "alpha", 1, 0.7f, 0)


                );
                set.setDuration(1500).start();


            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none,R.anim.out_to_right);
    }

    public void getDataFromServer( String coltID){
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


}

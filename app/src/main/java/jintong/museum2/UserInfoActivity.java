package jintong.museum2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 *
 * 查看其他用户的基本信息
 * Created by wjc on 2017/3/14.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {


    private ImageView back;
    private ImageView icon;
    private TextView userName;
    private TextView watchNum;
    private TextView fansNum;
    private TextView noBlogText;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();
        initEvents();




    }

    private void initView() {
        back= (ImageView) findViewById(R.id.userInfo_back);

        icon= (ImageView) findViewById(R.id.userInfo_icon);
        userName= (TextView) findViewById(R.id.userInfo_username);
        watchNum= (TextView) findViewById(R.id.userInfo_watchNun);
        fansNum= (TextView) findViewById(R.id.userInfo_fansNun);
        noBlogText= (TextView) findViewById(R.id.userInfo_noBlogText);
        recyclerView= (RecyclerView) findViewById(R.id.userInfo_blogs);

    }

    private void initData() {



    }

    private void initEvents() {

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.userInfo_back:
                overridePendingTransition(R.anim.in_from_right,R.anim.none);
                finish();
                break;
            default:
                break;


        }



    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.in_from_right,R.anim.none);
    }
}

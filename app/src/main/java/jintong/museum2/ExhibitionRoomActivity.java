package jintong.museum2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.ColtListAdapter;
import entity.Collection;
import entity.ExhibitRoom;
import interfaces.OnItemClickListener;

/**
 * 展厅展示页面
 * Created by wjc on 2017/3/7.
 */

public class ExhibitionRoomActivity extends BaseActivity implements View.OnClickListener{


    private RecyclerView recyclerView;

    private ImageView back;

    private TextView roomName;

    private List<Collection> datas;

    private ExhibitRoom exhibitRoomData;

    private TextView introduction;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_exbitroom);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();
        setData();
        initEvents();


        ColtListAdapter adapter = new ColtListAdapter(this, datas);

        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent(ExhibitionRoomActivity.this,ZoomImageActivity.class);

                List<String> URLs=new ArrayList<String>();
                URLs.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/303ec10a40273f38802f9cf04fd03203.jpg");
                URLs.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/50ffdf4140281d96809f8eefdc2a47f6.jpg");
                URLs.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/98eec22c406f692780ca9bf7da9a8cf5.jpg");
                URLs.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/4e49f0f2400e93608052ba97a9928b3c.jpg");

                intent.putStringArrayListExtra("imageURLs", (ArrayList<String>) URLs);
                intent.putExtra("position",0);
                startActivity(intent);
                overridePendingTransition(R.anim.in_zoom,R.anim.none);



            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });


        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


    }

    private void setData() {

        roomName.setText(exhibitRoomData.getName());
        introduction.setText(exhibitRoomData.getIntroduction());


    }

    private void initView() {

        back= (ImageView) findViewById(R.id.museum_room_back);
        roomName= (TextView) findViewById(R.id.museum_room_name);
        recyclerView = (RecyclerView) findViewById(R.id.exhibitRoom_recyclerView);

        introduction= (TextView) findViewById(R.id.room_introduction);


    }


    private void initData() {

        exhibitRoomData=new ExhibitRoom();
        exhibitRoomData.setName("于志学展厅");
        exhibitRoomData.setIntroduction("于志学，笔名问津、干城，1935年生于黑龙江肇东市。系冰雪山水画创始人，现任黑龙江省画院名誉院长、第九届全国政协委员、中国美协理事、中国艺术研究院美术创作院创作研究员、中国国际书画艺术研究会副会长，美国国际传记研究院传记协会副理事长，英国剑桥大学国际传记中心研究员，第九届全国政协委员，第五届中国美协理事，一级美术师.");


        datas=new ArrayList<Collection>();
        for (int i=0;i<4;i++){
            Collection collection=new Collection();
            List<String> urls=new ArrayList<String>();
            urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/38ef58db401620a38093b48211c1a027.jpg");
            collection.setColtLikeNum(9955);
            collection.setColtImageURLs(urls);
            collection.setColtName("鸟纹包月瓶");
            collection.setColtDynasty("清朝");
            collection.setColtSize("高十米，宽十米");
            collection.setColtIntru("清末民国时期使用的称量粮食的工具，一面书“㕠聚号记”，一面书“校准市斗”。");

            collection.setColtCommentNum(996);
            datas.add(collection);



        }



    }

    private void initEvents() {

        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.museum_room_back:
                finish();
                overridePendingTransition(R.anim.none,R.anim.out_to_right);
                break;
            default:
                break;




        }


    }
}

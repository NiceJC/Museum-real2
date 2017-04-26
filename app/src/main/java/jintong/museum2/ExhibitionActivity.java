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

import java.util.ArrayList;
import java.util.List;

import adapter.ColtListAdapter;
import entity.Collection;
import entity.Exhibition;
import interfaces.OnItemClickListener;

/**
 *
 *
 * 展览 详情页
 * Created by wjc on 2017/3/23.
 */

public class ExhibitionActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;

    private ImageView back;

    private TextView roomName;

    private List<Collection> datas;

    private Exhibition exhibition;

    private  TextView introduction;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exhibition);

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

                Intent intent=new Intent(ExhibitionActivity.this,ZoomImageActivity.class);

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

        recyclerView.setFocusable(false);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(manager);




    }


    private void setData() {

        introduction.setText(exhibition.getExhibitIntru());
        roomName.setText(exhibition.getExhibitName());

    }

    private void initView() {

        introduction= (TextView) findViewById(R.id.room_introduction);
        back= (ImageView) findViewById(R.id.museum_room_back);
        roomName= (TextView) findViewById(R.id.museum_room_name);
        recyclerView = (RecyclerView) findViewById(R.id.exhibitRoom_recyclerView);



    }


    private void initData() {

        exhibition =new Exhibition();
        exhibition.setExhibitName("文艺复兴主题展览");
        exhibition.setExhibitIntru("文艺复兴最先在意大利各城市兴起，以后扩展到西欧各国，于16世纪达到顶峰，带来一段科学与艺术革命时期，揭开了近代欧洲历史的序幕，被认为是中古时代和近代的分界。文艺复兴是西欧近代三大思想解放运动（文艺复兴、宗教改革与启蒙运动）之一。");


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

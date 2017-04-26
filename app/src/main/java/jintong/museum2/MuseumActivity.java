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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import MyView.GlideCircleTransform;
import adapter.ExhibitRoomListAdapter;
import entity.ExhibitRoom;
import entity.Museum;
import interfaces.OnItemClickListener;

import static com.bumptech.glide.Glide.with;

/**
 *
 *
 * Created by wjc on 2017/3/6.
 */

public class MuseumActivity extends BaseActivity implements View.OnClickListener {


    private TextView museumName; //博物馆名称

    private ImageView back; //返回键

    private ImageView imageView; //博物馆大图

    private ImageView museumIcon;//博物馆小头像

    private TextView museumWatchNum;//关注人数

    private ImageView museumWatch;//关注图标，点击切换关注状态

    private TextView museumIntru; //博物馆简短介绍

    private TextView address; // 详细地址
    private TextView time; //开馆时间
    private TextView cost; //游览门票
    private RecyclerView recyclerView; //展厅列表


    private Museum museum;
    private List<ExhibitRoom> rooms;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_museum);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();
        setData();
        initEvents();


    }

    private void setData() {

        RequestManager requestManager = Glide.with(this);

        museumName.setText(museum.getMuseumName());
        requestManager.load(museum.getImageURLs().get(0)).into(imageView);
        museumIntru.setText(museum.getMuseumInfo());

        Glide.with(this).load(museum.getIconURL())
                .transform(new GlideCircleTransform(this))
                .into(museumIcon);

        museumWatch.setSelected(museum.isWatched());
        museumWatchNum.setText(museum.getWatchNums()+"");


        address.setText(museum.getMuseumAddress());
        time.setText(museum.getOpening_time());
        cost.setText(museum.getCost());

        ExhibitRoomListAdapter adapter = new ExhibitRoomListAdapter(this, rooms);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(MuseumActivity.this,ExhibitionRoomActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.none);


            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


    }

    private void initView() {

        museumName = (TextView) findViewById(R.id.museum_detail_name);
        back = (ImageView) findViewById(R.id.museum_detail_back);

        museumIcon = (ImageView) findViewById(R.id.museum_detail_icon);
        museumWatchNum = (TextView) findViewById(R.id.museum_detail_watchNum);
        museumWatch = (ImageView) findViewById(R.id.museum_detail_watch);


        imageView = (ImageView) findViewById(R.id.museum_detail_image);
        museumIntru = (TextView) findViewById(R.id.museum_detail_intru);
        address = (TextView) findViewById(R.id.museum_detail_address);
        time = (TextView) findViewById(R.id.museum_detail_time);
        cost = (TextView) findViewById(R.id.museum_detail_cost);
        recyclerView = (RecyclerView) findViewById(R.id.museum_detail_recyclerView);

    }

    private void initData() {
//        getIntent().getExtras()

        List<String> urls = new ArrayList<String>();

        urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/09/20/c853028540e8321d80413072af122618.jpg");
        museum = new Museum();
        museum.setImageURLs(urls);
        museum.setMuseumName("首都博物馆");
        museum.setMuseumInfo("首都博物馆是北京地区大型综合型博物馆，以展示北京各时期的历史以及其文化遗存为主，是北京的记忆宫首都博物馆是北京地区大型综合型博物馆，以展示北京各时期的历史以及其文化遗存为主，是北京的记忆宫");
        museum.setCost("免费");
        museum.setMuseumAddress("浙江省杭州市下城区沈家路319，创新中国产业园");
        museum.setOpening_time("09:00-17:00  周一闭馆(法定节假日除外)");

        museum.setWatchNums(999);
        museum.setWatched(true);

        museum.setIconURL("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/10/ca2831e7400e37be8040039ded3dd21e.jpg");

        rooms = new ArrayList<ExhibitRoom>();
        for (int i = 0; i < 4; i++) {
            ExhibitRoom room = new ExhibitRoom();
            room.setName("沧海一粟展厅");
            room.setCollectionNum(10);
            room.setImageURL("http://bmob-cdn-4183.b0.upaiyun.com/2016/09/20/f3cf5f98404de1f3807cd867eb5ff5ef.JPG");

            rooms.add(room);
        }


    }

    private void initEvents() {

        back.setOnClickListener(this);
        museumWatch.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.museum_detail_back:
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);
                break;
            case R.id.museum_detail_watch:

                break;



            default:
                break;


        }


    }
}

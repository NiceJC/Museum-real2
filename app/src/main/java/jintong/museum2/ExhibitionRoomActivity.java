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

import BmobUtils.BmobColt;
import BmobUtils.BmobExhibitRoom;
import BmobUtils.BmobExhibition;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.ColtListAdapter;
import entity.Collection;
import entity.ExhibitRoom;
import entity.Exhibition;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import util.ToastUtils;

import static util.ParameterBase.EXHIBIROOM_ID;
import static util.ParameterBase.EXHIBITION_ID;

/**
 * 展厅展示页面
 * Created by wjc on 2017/3/7.
 */

public class ExhibitionRoomActivity extends BaseActivity implements adapter.BaseAdapter.OnItemClickListener,
        adapter.BaseAdapter.OnItemLongClickListener, adapter.BaseAdapter.OnViewClickListener,
        PullBaseView.OnRefreshListener{


    private PullRecyclerView recyclerView;

    private ImageView back;

    private TextView roomName;

    private List<Object> datas=new ArrayList<>();

    private ExhibitRoom exhibitRoom;

    private TextView introduction;

    private ColtListAdapter adapter;

    private int currentPage=0;

    private String exhitiRoomID;




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





    }

    private void setData() {

        roomName.setText(exhibitRoom.getName());
        introduction.setText(exhibitRoom.getIntroduction());


    }

    private void initView() {

        back= (ImageView) findViewById(R.id.museum_room_back);
        roomName= (TextView) findViewById(R.id.museum_room_name);
        recyclerView = (PullRecyclerView) findViewById(R.id.exhibitRoom_recyclerView);
        recyclerView.setFocusable(false);

        adapter=new ColtListAdapter(ExhibitionRoomActivity.this,datas,this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        introduction= (TextView) findViewById(R.id.room_introduction);


    }


    private void initData() {

        exhitiRoomID=getIntent().getStringExtra(EXHIBIROOM_ID);

        if(datas.size()!=0){
            return;
        }

        getExhibitRoomInfo(exhitiRoomID);
        pullMoreFromServer(exhitiRoomID,0);


    }

    private void initEvents() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none,R.anim.out_to_right);

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none,R.anim.out_to_right);

    }

    @Override
    public void onHeaderRefresh(PullBaseView view) {

    }

    @Override
    public void onFooterRefresh(PullBaseView view) {

        pullMoreFromServer(exhitiRoomID,currentPage);


    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onViewClick(int position, int viewtype) {

        switch (viewtype){
            //评论
            case 1:
                break;

            //点赞
            case 2:
                break;

            //图片
            case 3:

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

                break;
            default:
                break;


        }



    }


    //获取当前的展厅信息
    public void getExhibitRoomInfo(String exhitiRoomID){
        BmobExhibitRoom bmobExhibitRoom=BmobExhibitRoom.getInstance(this);
        bmobExhibitRoom.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                exhibitRoom= (ExhibitRoom) Obj;
                setData();

            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobExhibitRoom.getExhibitRoomByID(exhitiRoomID);



    }


    //上拉加载更多
    public void pullMoreFromServer(String exhitiRoomID, int curPage) {

        BmobColt bmobColt = BmobColt.getInstance(this);

        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Collection> collectionList = (List<Collection>) Obj;

                if (collectionList == null || collectionList.size() == 0) {
                    ToastUtils.toast(ExhibitionRoomActivity.this, "没有更多内容啦");

                } else {
                    for (Collection collection : collectionList) {
                        datas.add(collection);
                    }
                    adapter.notifyDataSetChanged();
                    currentPage++;
                }
                recyclerView.onFooterRefreshComplete();


            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobColt.getByBelongID(exhitiRoomID, EXHIBIROOM_ID, curPage);

    }

}

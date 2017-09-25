package jintong.museum2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobColt;
import bmobUtils.BmobExhibitRoom;
import adapter.BaseAdapter;
import adapter.ColtListAdapter;
import model.Collection;
import model.ExhibitRoom;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static util.ParameterBase.COLT_ID;
import static util.ParameterBase.EXHIBIROOM_ID;

/**
 * 展厅展示页面
 * Created by wjc on 2017/3/7.
 */

public class ExhibitionRoomActivity extends BaseActivity implements BaseAdapter.OnItemClickListener, XRecyclerView.LoadingListener

{


    private XRecyclerView recyclerView;

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


        initView();
        initData();

        initEvents();





    }



    private void initView() {

        back= (ImageView) findViewById(R.id.museum_room_back);
        roomName= (TextView) findViewById(R.id.museum_room_name);
        introduction= (TextView) findViewById(R.id.room_introduction);

        recyclerView = (XRecyclerView) findViewById(R.id.exhibitRoom_recyclerView);
        recyclerView.setFocusable(false);

        adapter=new ColtListAdapter(ExhibitionRoomActivity.this,datas);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter.setOnItemClickListener(this);
        recyclerView.setLoadingListener(this);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);


    }


    private void initData() {

        exhitiRoomID=getIntent().getStringExtra(EXHIBIROOM_ID);

        if(datas.size()!=0){
            return;
        }

        getExhibitRoomInfo(exhitiRoomID);
        recyclerView.refresh();


    }
         private void setData() {

             roomName.setText(exhibitRoom.getName());
             String intro=exhibitRoom.getIntroduction();
             if(intro==null||intro.equals("")){
                 intro="暂无资料";
             }
             introduction.setText(intro);

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
    public void onItemClick(int position) {


        Intent intent = new Intent(this, CollectionActivity.class);
        Collection collection = (Collection) datas.get(position);
        intent.putExtra(COLT_ID, collection.getObjectId());
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.none);

    }
    @Override
    public void onRefresh() {

        refreshFromServer(exhitiRoomID);
    }

    @Override
    public void onLoadMore() {

        pullMoreFromServer(exhitiRoomID,currentPage);
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
                   recyclerView.setNoMore(true);

                } else {
                    datas.addAll(collectionList);
                    adapter.notifyDataSetChanged();
                    currentPage++;
                }
                recyclerView.loadMoreComplete();



            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobColt.getByBelongID(exhitiRoomID, EXHIBIROOM_ID, curPage);

    }


    //刷新数据
    public void refreshFromServer(String exhitiRoomID) {

        BmobColt bmobColt = BmobColt.getInstance(this);

        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Collection> collectionList = (List<Collection>) Obj;

                if (collectionList == null || collectionList.size() == 0) {
                    ToastUtils.toast(ExhibitionRoomActivity.this, "暂无数据");

                    recyclerView.setNoMore(true);

                } else {
                    datas.clear();
                    datas.addAll(collectionList);
                    adapter.notifyDataSetChanged();
                    currentPage = 1;
                }
                recyclerView.reset();

            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobColt.getByBelongID(exhitiRoomID, EXHIBIROOM_ID, 0);
    }
}

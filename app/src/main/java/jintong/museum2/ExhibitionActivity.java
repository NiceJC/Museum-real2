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

import BmobUtils.BmobColt;
import BmobUtils.BmobExhibition;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.ColtListAdapter;
import cn.bmob.v3.Bmob;
import entity.Collection;
import entity.Exhibition;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import util.ToastUtils;

import static util.ParameterBase.EXHIBITION_ID;

/**
 * 展览 详情页
 * Created by wjc on 2017/3/23.
 */

public class ExhibitionActivity extends BaseActivity implements adapter.BaseAdapter.OnItemClickListener,
        adapter.BaseAdapter.OnItemLongClickListener, adapter.BaseAdapter.OnViewClickListener,
        PullBaseView.OnRefreshListener {
    private PullRecyclerView recyclerView;
    private ImageView back;
    private TextView roomName;
    private List<Object> datas = new ArrayList<>();
    private LinearLayoutManager manager;
    private Exhibition exhibition;
    private ColtListAdapter adapter;
    private int currentPage = 0;
    private TextView introduction;


    private String exhibitID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exhibition);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();

        initEvents();





    }


    private void setData() {

        introduction.setText(exhibition.getExhibitIntru());
        roomName.setText(exhibition.getExhibitName());

    }

    private void initView() {

        introduction = (TextView) findViewById(R.id.room_introduction);
        back = (ImageView) findViewById(R.id.museum_room_back);
        roomName = (TextView) findViewById(R.id.museum_room_name);
        recyclerView = (PullRecyclerView) findViewById(R.id.exhibitRoom_recyclerView);


        recyclerView.setFocusable(false);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);

        recyclerView.setOnRefreshListener(this);

        adapter = new ColtListAdapter(ExhibitionActivity.this, datas, this);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);


        recyclerView.setCanPullDown(false); //不用下拉
        recyclerView.setAdapter(adapter);




    }


    private void initData() {

        exhibitID = getIntent().getStringExtra(EXHIBITION_ID);
         if(datas.size()!=0){
             return;
         }

        getExhibitionInfo(exhibitID);
        pullMoreFromServer(exhibitID,0);


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

        pullMoreFromServer(exhibitID,currentPage);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onViewClick(int position, int viewtype) {

    }


    public void getExhibitionInfo(String exhibitID){
        BmobExhibition bmobExhibition=BmobExhibition.getInstance(this);
        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                 exhibition= (Exhibition) Obj;
                setData();

            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobExhibition.getExhibitionByID(exhibitID);



    }


    //上拉加载更多
    public void pullMoreFromServer(String exhibitionID, int curPage) {

        BmobColt bmobColt = BmobColt.getInstance(this);

        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Collection> collectionList = (List<Collection>) Obj;

                if (collectionList == null || collectionList.size() == 0) {
                    ToastUtils.toast(ExhibitionActivity.this, "没有更多内容啦");

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
        bmobColt.getByBelongID(exhibitionID, EXHIBITION_ID, curPage);

    }


}

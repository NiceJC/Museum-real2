package jintong.museum2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import BmobUtils.BmobMuseum;
import MyView.ExpandableTextView;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.ColtListAdapter;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import entity.Collection;
import entity.Exhibition;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import util.ToastUtils;

import static util.ParameterBase.COLT_ID;
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



    private int watchNum;
    private boolean isWatched;

    private TextView exhibitWatchNum,address,time,cost;//关注人数

    private ExpandableTextView intro;
    private ImageView exhibitWatch;//关注图标，点击切换关注状态





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
        setWatchInfo();
        roomName.setText(exhibition.getExhibitName());
        address.setText(exhibition.getToMuseum().getMuseumName());
        cost.setText(exhibition.getCost());
        time.setText(exhibition.getTime());
        intro.setText(exhibition.getExhibitIntru());


    }

    private void initView() {



        back = (ImageView) findViewById(R.id.museum_room_back);
        roomName = (TextView) findViewById(R.id.museum_room_name);
        recyclerView = (PullRecyclerView) findViewById(R.id.exhibitRoom_recyclerView);

        exhibitWatchNum= (TextView) findViewById(R.id.exhibit_detail_watchNum);

        exhibitWatch= (ImageView) findViewById(R.id.exhibit_detail_watch);

        address= (TextView) findViewById(R.id.exhibit_detail_address);
        time= (TextView) findViewById(R.id.exhibit_detail_time);
        cost= (TextView) findViewById(R.id.exhibit_detail_cost);
        intro = (ExpandableTextView) findViewById(R.id.expand_text_view);

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

        currentPage=1;


    }

    private void initEvents() {


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none,R.anim.out_to_right);

            }
        });
        exhibitWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickWatchExhibit();
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

        Intent intent=new Intent(this,CollectionActivity.class);
        Collection collection= (Collection) datas.get(position);
        intent.putExtra(COLT_ID,collection.getObjectId());
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right,R.anim.none);


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
    //设置用户是否已经关注博物馆，以及关注的总人数
    public void setWatchInfo(){
        BmobExhibition bmobExhibition=BmobExhibition.getInstance(this);
        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<User> list= (List<User>) Obj;

                isWatched=false;
                watchNum=list.size();

                for (User user:list
                        ) {

                    if(user.getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())){
                        isWatched=true;
                        break;
                    }
                }


                exhibitWatch.setSelected(isWatched);
                exhibitWatchNum.setText(watchNum+"");
            }
            @Override
            public void onFail(Object Obj) {
            }
        });
        bmobExhibition.getFansOfExhibition(exhibitID);
    }

    //关注或者取消关注 当前的博物馆
    public void clickWatchExhibit(){

        if(isWatched){ //已经关注  取消关注
            new AlertDialog.Builder(this).setTitle("取消关注").setMessage("确定取消关注？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            BmobExhibition bmobExhibition=BmobExhibition.getInstance(ExhibitionActivity.this);
                            bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                                @Override
                                public void onSuccess(Object Obj) {
                                    ToastUtils.toast(ExhibitionActivity.this,"已取消关注");

                                    isWatched=false;
                                    watchNum=watchNum-1;
                                    exhibitWatch.setSelected(isWatched);
                                    exhibitWatchNum.setText(watchNum+"");

                                }
                                @Override
                                public void onFail(Object Obj) {
                                }
                            });
                            bmobExhibition.cancelWatchExhibit(exhibitID);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }else{//未关注  点击关注
            BmobExhibition bmobExhibition=BmobExhibition.getInstance(this);
            bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                @Override
                public void onSuccess(Object Obj) {

                    ToastUtils.toast(ExhibitionActivity.this,"已成功关注");

                    exhibitWatch.setSelected(true);

                    isWatched=true;
                    watchNum=watchNum+1;
                    exhibitWatch.setSelected(isWatched);
                    exhibitWatchNum.setText(watchNum+"");

                }

                @Override
                public void onFail(Object Obj) {

                }
            });
            bmobExhibition.watchExhibit(exhibitID);
        }
    }

}

package jintong.museum2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobColt;
import bmobUtils.BmobExhibition;
import myView.ExpandableTextView;
import adapter.BaseAdapter;
import adapter.ColtListAdapter;
import cn.bmob.v3.BmobUser;
import model.Collection;
import model.Exhibition;
import model.User;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static util.ParameterBase.COLT_ID;
import static util.ParameterBase.EXHIBITION_ID;

/**
 * 展览 详情页
 * Created by wjc on 2017/3/23.
 */

public class ExhibitionActivity extends BaseActivity implements BaseAdapter.OnItemClickListener,
         XRecyclerView.LoadingListener {
    private XRecyclerView recyclerView;
    private ImageView back;
    private TextView roomName;
    private List<Object> datas = new ArrayList<>();
    private LinearLayoutManager manager;
    private Exhibition exhibition;
    private ColtListAdapter adapter;
    private int currentPage = 0;


    private int watchNum;
    private boolean isWatched;

    private TextView exhibitWatchNum, address, time, cost;//关注人数

    private ExpandableTextView intro;
    private ImageView exhibitWatch;//关注图标，点击切换关注状态


    private String exhibitID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exhibition);


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
        recyclerView = (XRecyclerView) findViewById(R.id.exhibitRoom_recyclerView);

        exhibitWatchNum = (TextView) findViewById(R.id.exhibit_detail_watchNum);

        exhibitWatch = (ImageView) findViewById(R.id.exhibit_detail_watch);

        address = (TextView) findViewById(R.id.exhibit_detail_address);
        time = (TextView) findViewById(R.id.exhibit_detail_time);
        cost = (TextView) findViewById(R.id.exhibit_detail_cost);
        intro = (ExpandableTextView) findViewById(R.id.expand_text_view);

//        recyclerView.setFocusable(false);
//        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new ColtListAdapter(ExhibitionActivity.this, datas);
        adapter.setOnItemClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLoadingListener(this);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);


    }


    private void initData() {

        exhibitID = getIntent().getStringExtra(EXHIBITION_ID);
        if (datas.size() != 0) {
            return;
        }

        getExhibitionInfo(exhibitID);

        recyclerView.refresh();


    }

    private void initEvents() {


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);

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
        overridePendingTransition(R.anim.none, R.anim.out_to_right);

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

        refreshFromServer(exhibitID);
    }

    @Override
    public void onLoadMore() {

        pullMoreFromServer(exhibitID, currentPage);
    }

    public void getExhibitionInfo(String exhibitID) {
        BmobExhibition bmobExhibition = BmobExhibition.getInstance(this);
        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                exhibition = (Exhibition) Obj;
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
                recyclerView.loadMoreComplete();
            }
        });
        bmobColt.getByBelongID(exhibitionID, EXHIBITION_ID, curPage);
    }

    //刷新数据
    public void refreshFromServer(String exhibitionID) {
        BmobColt bmobColt = BmobColt.getInstance(this);
        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Collection> collectionList = (List<Collection>) Obj;
                if (collectionList == null || collectionList.size() == 0) {
                    ToastUtils.toast(ExhibitionActivity.this, "暂无数据");
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
                recyclerView.reset();
            }
        });
        bmobColt.getByBelongID(exhibitionID, EXHIBITION_ID, 0);
    }


    //设置用户是否已经关注博物馆，以及关注的总人数
    public void setWatchInfo() {
        BmobExhibition bmobExhibition = BmobExhibition.getInstance(this);
        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<User> list = (List<User>) Obj;

                isWatched = false;
                watchNum = list.size();

                for (User user : list
                        ) {

                    if (user.getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                        isWatched = true;
                        break;
                    }
                }
                exhibitWatch.setSelected(isWatched);
                exhibitWatchNum.setText(watchNum + "");
            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobExhibition.getFansOfExhibition(exhibitID);
    }

    //关注或者取消关注 当前的博物馆
    public void clickWatchExhibit() {

        if (isWatched) { //已经关注  取消关注
            new AlertDialog.Builder(this).setTitle("取消关注").setMessage("确定取消关注？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            BmobExhibition bmobExhibition = BmobExhibition.getInstance(ExhibitionActivity.this);
                            bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                                @Override
                                public void onSuccess(Object Obj) {
                                    ToastUtils.toast(ExhibitionActivity.this, "已取消关注");

                                    isWatched = false;
                                    watchNum = watchNum - 1;
                                    exhibitWatch.setSelected(isWatched);
                                    exhibitWatchNum.setText(watchNum + "");

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
        } else {//未关注  点击关注
            BmobExhibition bmobExhibition = BmobExhibition.getInstance(this);
            bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                @Override
                public void onSuccess(Object Obj) {

                    ToastUtils.toast(ExhibitionActivity.this, "已成功关注");

                    exhibitWatch.setSelected(true);

                    isWatched = true;
                    watchNum = watchNum + 1;
                    exhibitWatch.setSelected(isWatched);
                    exhibitWatchNum.setText(watchNum + "");

                }

                @Override
                public void onFail(Object Obj) {

                }
            });
            bmobExhibition.watchExhibit(exhibitID);
        }
    }


}

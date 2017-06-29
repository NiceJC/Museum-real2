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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import BmobUtils.BmobColt;
import BmobUtils.BmobExhibitRoom;
import BmobUtils.BmobMuseum;
import MyView.ExpandableTextView;
import MyView.GlideCircleTransform;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.ExhibitRoomListAdapter;
import cn.bmob.v3.BmobUser;
import entity.Collection;
import entity.ExhibitRoom;
import entity.Museum;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import util.ToastUtils;

import static com.bumptech.glide.Glide.with;
import static util.ParameterBase.EXHIBIROOM_ID;
import static util.ParameterBase.MUSEUM_ID;

/**
 *
 *
 * Created by wjc on 2017/3/6.
 */

public class MuseumActivity extends BaseActivity implements View.OnClickListener,adapter.BaseAdapter.OnItemClickListener,adapter.BaseAdapter.OnViewClickListener, PullBaseView.OnRefreshListener{


    private TextView museumName; //博物馆名称

    private ImageView back; //返回键

    private ImageView imageView; //博物馆大图

    private ImageView museumIcon;//博物馆小头像

    private TextView museumWatchNum;//关注人数

    private ImageView museumWatch;//关注图标，点击切换关注状态

    private ExpandableTextView museumIntru; //博物馆简短介绍

    private TextView address; // 详细地址
    private TextView time; //开馆时间
    private TextView cost; //游览门票
    private PullRecyclerView recyclerView; //展厅列表


    private ExhibitRoomListAdapter adapter;
    private String museumID;
    private int currentPage=0;

    private Museum museum;
    private List<Object> rooms=new ArrayList<Object>();


    private int watchNum;  //博物馆的粉丝数
    private boolean isWatched; //是否已经关注当前博物馆


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_museum);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();

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


setWatchInfo();
        address.setText(museum.getMuseumAddress());
        time.setText(museum.getOpening_time());
        cost.setText(museum.getCost());

    }




    private void initView() {

        museumName = (TextView) findViewById(R.id.museum_detail_name);
        back = (ImageView) findViewById(R.id.museum_detail_back);

        museumIcon = (ImageView) findViewById(R.id.museum_detail_icon);
        museumWatchNum = (TextView) findViewById(R.id.museum_detail_watchNum);
        museumWatch = (ImageView) findViewById(R.id.museum_detail_watch);


        imageView = (ImageView) findViewById(R.id.museum_detail_image);
        museumIntru = (ExpandableTextView) findViewById(R.id.expand_text_view);
        address = (TextView) findViewById(R.id.museum_detail_address);
        time = (TextView) findViewById(R.id.museum_detail_time);
        cost = (TextView) findViewById(R.id.museum_detail_cost);
        recyclerView = (PullRecyclerView) findViewById(R.id.museum_detail_recyclerView);


        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter=new ExhibitRoomListAdapter(MuseumActivity.this,rooms,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setCanPullUp(false);
        recyclerView.setCanPullDown(false);






    }

    private void initData() {
        museumID=getIntent().getStringExtra(MUSEUM_ID);

       if(rooms.size()!=0){
           return;
       }

        getMuseumInfo(museumID);
        pullMoreFromServer(museumID,0);
        currentPage=1;

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


                clickWatchMuseum();
                break;



            default:
                break;


        }


    }



    @Override
    public void onHeaderRefresh(PullBaseView view) {


    }

    @Override
    public void onFooterRefresh(PullBaseView view) {

    }

    @Override
    public void onItemClick(int position) {

        Intent intent=new Intent(MuseumActivity.this,ExhibitionRoomActivity.class);
        ExhibitRoom exhibitRoom= (ExhibitRoom) rooms.get(position);
        intent.putExtra(EXHIBIROOM_ID,exhibitRoom.getObjectId());
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right,R.anim.none);

    }

    @Override
    public void onViewClick(int position, int viewtype) {

    }



    //获取当前的博物馆信息
    public void getMuseumInfo(String museumID){
        BmobMuseum bmobMuseum=BmobMuseum.getInstance(this);
        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                museum= (Museum) Obj;
                setData();

            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobMuseum.getMuseumByID(museumID);



    }


    //加载展厅
    public void pullMoreFromServer(String museumID, int curPage) {

        BmobExhibitRoom bmobExhibitRoom = BmobExhibitRoom.getInstance(this);

        bmobExhibitRoom.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<ExhibitRoom> exhibitRoomList = (List<ExhibitRoom>) Obj;

                if (exhibitRoomList == null || exhibitRoomList.size() == 0) {
                    ToastUtils.toast(MuseumActivity.this, "没有更多内容啦");

                } else {
                    rooms.clear();
                    for ( ExhibitRoom exhibitRoom : exhibitRoomList) {


                        rooms.add(exhibitRoom);
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
        bmobExhibitRoom.getExhibitRoomsByMuseum(museumID, curPage);

    }
    //设置用户是否已经关注博物馆，以及关注的总人数
    public void setWatchInfo(){
        BmobMuseum bmobMuseum=BmobMuseum.getInstance(this);
        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
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


                museumWatch.setSelected(isWatched);
                museumWatchNum.setText(watchNum+"");
            }
            @Override
            public void onFail(Object Obj) {
            }
        });
        bmobMuseum.getFansOfMuseum(museumID);
    }

    //关注或者取消关注 当前的博物馆
    public void clickWatchMuseum(){

        if(isWatched){ //已经关注  取消关注
            new AlertDialog.Builder(this).setTitle("取消关注").setMessage("确定取消关注？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            BmobMuseum bmobmusem=BmobMuseum.getInstance(MuseumActivity.this);
                            bmobmusem.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                                @Override
                                public void onSuccess(Object Obj) {
                                    ToastUtils.toast(MuseumActivity.this,"已取消关注");

                                    isWatched=false;
                                    watchNum=watchNum-1;
                                    museumWatch.setSelected(isWatched);
                                    museumWatchNum.setText(watchNum+"");

                                }
                                @Override
                                public void onFail(Object Obj) {
                                }
                            });
                            bmobmusem.cancelWatchMuseum(museumID);
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
            BmobMuseum bmobmusem=BmobMuseum.getInstance(this);
            bmobmusem.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                @Override
                public void onSuccess(Object Obj) {

                    ToastUtils.toast(MuseumActivity.this,"已成功关注");

                    museumWatch.setSelected(true);

                    isWatched=true;
                    watchNum=watchNum+1;
                    museumWatch.setSelected(isWatched);
                    museumWatchNum.setText(watchNum+"");

                }

                @Override
                public void onFail(Object Obj) {

                }
            });
            bmobmusem.watchMuseum(museumID);
        }
    }


}

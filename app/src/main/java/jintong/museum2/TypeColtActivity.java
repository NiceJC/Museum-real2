package jintong.museum2;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.TypeColtAdapter;
import entity.Collection;

/**
 * Created by wjc on 2017/3/24.
 */

public class TypeColtActivity extends BaseActivity {

    private ImageView back;
    private RecyclerView recyclerView;
    private List<String> URLs;
    private List<Collection> collectionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colt_by_type);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();
        setData();


    }

    private void initView() {
        back = (ImageView) findViewById(R.id.activity_colt_back);
        recyclerView = (RecyclerView) findViewById(R.id.type_colt_recyclerView);


    }

    private void initData() {
        String s1 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/303ec10a40273f38802f9cf04fd03203.jpg";
        String s2 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/50ffdf4140281d96809f8eefdc2a47f6.jpg";
        String s3 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/98eec22c406f692780ca9bf7da9a8cf5.jpg";
        String s4 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/4e49f0f2400e93608052ba97a9928b3c.jpg";
        String s5 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/98eec22c406f692780ca9bf7da9a8cf5.jpg";
        String s6 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/732d7af140b97db98025eab1b953fe1b.jpg";
        String s7 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/a220dd564081640d809bf930eef6f732.jpg";
        String s8 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/8d01415b408f0b3c80f92f26e02a4d93.jpg";
        String s9 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/fa83b278409978e3800d2ca2cf921cd1.jpg";
        String s10 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/2e2b4b76408ce0da80c2b03adba455bb.jpg";
        String s11 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/38ef58db401620a38093b48211c1a027.jpg";
        String s12 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/50ffdf4140281d96809f8eefdc2a47f6.jpg";
        String s13 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/cd37af4740e17a4580f1d00cc919a639.jpg";
        String s14 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/c4f89b0540d7cf3d80a77d13ca4e04b2.jpg";
        String s15 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/fa2ffcfb4023dbd080f76045c5f8e068.jpg";
        String s16 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/ed1a5ec940d050cd80863eebc9a668f4.jpg";


        URLs = new ArrayList<String>();
        URLs.add(s1);
        URLs.add(s2);
        URLs.add(s3);
        URLs.add(s4);
        URLs.add(s5);
        URLs.add(s6);
        URLs.add(s7);
        URLs.add(s8);
        URLs.add(s9);
        URLs.add(s10);
        URLs.add(s11);
        URLs.add(s12);
        URLs.add(s13);
        URLs.add(s14);
        URLs.add(s15);
        URLs.add(s16);

        int type= getIntent().getIntExtra("type",0);
        Toast.makeText(this,type+"",Toast.LENGTH_SHORT).show();



    }

    private void setData() {


        collectionList=new ArrayList<Collection>();
        for (int i=0;i<16;i++){

            List<String> imageURLs=new ArrayList<String>();
            imageURLs.add(URLs.get(i));
            Collection collection=new Collection();
            collection.setColtID(i);
            collection.setColtImageURLs(imageURLs);
            collection.setColtName("藏品 "+i+" 号");

            collectionList.add(collection);
        }


        TypeColtAdapter adapter = new TypeColtAdapter(TypeColtActivity.this, collectionList);

        recyclerView.setAdapter(adapter);
        final StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
//            }
//        });

        recyclerView.setLayoutManager(layoutManager);


    }
}

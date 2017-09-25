package jintong.museum2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobColt;
import myView.PullBaseView;
import myView.PullRecyclerView;
import adapter.BaseAdapter;
import adapter.TypeColtAdapter;
import model.Collection;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static util.ParameterBase.COLT_ID;
import static util.ParameterBase.COLT_TYPE;

/**
 * 用瀑布流展示
 * Created by wjc on 2017/3/24.
 */

public class TypeColtActivity extends BaseActivity implements BaseAdapter.OnItemClickListener,
        BaseAdapter.OnItemLongClickListener, BaseAdapter.OnViewClickListener,
        PullBaseView.OnRefreshListener {

    private ImageView back;
    private TextView typeName;
    private PullRecyclerView recyclerView;
    private List<Object> datas = new ArrayList<>();
    private TypeColtAdapter adapter;
    private int currentPage = 0;
    private int type;
    private StaggeredGridLayoutManager manager;
    private String typeNameString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colt_by_type);


        initView();
        initData();
        setData();


    }

    private void initView() {
        type = getIntent().getIntExtra(COLT_TYPE, 0);
        typeNameString=typeNumToName(type);

        back = (ImageView) findViewById(R.id.activity_colt_back);
        recyclerView = (PullRecyclerView) findViewById(R.id.type_colt_recyclerView);

        typeName = (TextView) findViewById(R.id.activity_colt_type_name);

        adapter = new TypeColtAdapter(TypeColtActivity.this, datas, this);
        recyclerView.setAdapter(adapter);
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(manager);
        adapter.setOnItemClickListener(this);

        recyclerView.setOnRefreshListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none,R.anim.out_to_right);
            }
        });
    }

    private void initData() {



        if (datas.size() != 0) {
            return;
        }

        pullDataFromServer(type, 0);


    }

    private void setData() {

        typeName.setText(typeNameString);



    }


    @Override
    public void onHeaderRefresh(PullBaseView view) {

    }

    @Override
    public void onFooterRefresh(PullBaseView view) {

        pullDataFromServer(type, currentPage);


    }

    @Override
    public void onItemClick(int position) {

        Collection collection = (Collection) datas.get(position);
        Intent intent = new Intent(TypeColtActivity.this, CollectionActivity.class);
        intent.putExtra(COLT_ID, collection.getObjectId());
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.none);


    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onViewClick(int position, int viewtype) {

    }


    //上拉加载更多
    public void pullDataFromServer(Integer coltType, final int curPage) {

        BmobColt bmobColt = BmobColt.getInstance(this);

        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Collection> collectionList = (List<Collection>) Obj;

                if (collectionList == null || collectionList.size() == 0) {
                    ToastUtils.toast(TypeColtActivity.this, "没有更多内容啦");

                } else {

                    if (curPage == 0) {
                        datas.clear();
                    }

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
                recyclerView.onFooterRefreshComplete();

            }
        });

        bmobColt.getByType(coltType, curPage);

    }


    @Override
    public void onBackPressed() {

                finish();
                overridePendingTransition(R.anim.none,R.anim.out_to_right);

    }

    public String typeNumToName(int typeNum) {
        String typeName = "";
        switch (typeNum) {
            case Collection.TYPE_BRONSE:
                typeName = "青铜器";
                break;
            case Collection.TYPE_CHINA:
                typeName = "陶瓷";
                break;
            case R.id.museum_fragment_jade:
                typeName = "玉石";
                break;
            case Collection.TYPE_LACQUER:
                typeName = "漆器";
                break;
            case Collection.TYPE_PAINT:
                typeName = "书画";
                break;
            case Collection.TYPE_OTHERS:
                typeName = "其他";
                break;
            default:
                break;


        }
        return typeName;

    }
}


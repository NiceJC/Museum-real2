package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobColt;
import adapter.BaseAdapter;
import adapter.CollectionGridAdapter;
import jintong.museum2.SearchingActivity;
import model.Collection;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.CollectionActivity;
import jintong.museum2.R;
import jintong.museum2.TypeColtActivity;
import util.ToastUtils;

import static util.ParameterBase.COLT_ID;
import static util.ParameterBase.COLT_TYPE;

/**
 * 宝库页面
 * Created by wjc on 2017/2/9.
 */
public class MuseumFragment extends Fragment implements View.OnClickListener, BaseAdapter.OnItemClickListener, XRecyclerView.LoadingListener{
    private View view;
    private XRecyclerView recyclerView;
    private List<Object> datas = new ArrayList<Object>();
    private CollectionGridAdapter adapter;

    private FloatingActionButton floatingActionButton;

    private int theType;

    private int currentPage = 0;
    private ImageView typeBronze; //青铜器
    private ImageView typeChina;//瓷器
    private ImageView typeJade;//玉石器
    private ImageView typePaint;//书画
    private ImageView typeLacquer;//漆器
    private ImageView typeOthers;//其他


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.museum_fragment, container, false);


        initViews();
        initDatas();

        setEvents();

        return view;

    }

    private void setEvents() {
        typeBronze.setOnClickListener(this);
        typeChina.setOnClickListener(this);
        typeJade.setOnClickListener(this);
        typePaint.setOnClickListener(this);
        typeLacquer.setOnClickListener(this);
        typeOthers.setOnClickListener(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchingActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.none);
            }
        });

    }


    private void initDatas() {

        if (datas.size() != 0) {
            return;
        }

       recyclerView.refresh();


    }

    private void initViews() {
        recyclerView = (XRecyclerView) view.findViewById(R.id.colt_recyclerView);


        typeBronze = (ImageView) view.findViewById(R.id.museum_fragment_bronze);
        typeChina = (ImageView) view.findViewById(R.id.museum_fragment_china);
        typeJade = (ImageView) view.findViewById(R.id.museum_fragment_jade);
        typePaint = (ImageView) view.findViewById(R.id.museum_fragment_paint);
        typeLacquer = (ImageView) view.findViewById(R.id.museum_fragment_lacquer);
        typeOthers = (ImageView) view.findViewById(R.id.museum_fragment_others);
        floatingActionButton= (FloatingActionButton) view.findViewById(R.id.search_more);

        adapter = new CollectionGridAdapter(getActivity(), datas);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recyclerView.setLoadingListener(this);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.museum_fragment_bronze:
                theType = Collection.TYPE_BRONSE;
                break;
            case R.id.museum_fragment_china:
                theType = Collection.TYPE_CHINA;
                break;
            case R.id.museum_fragment_jade:
                theType = Collection.TYPE_JADE;
                break;
            case R.id.museum_fragment_lacquer:
                theType = Collection.TYPE_LACQUER;
                break;
            case R.id.museum_fragment_paint:
                theType = Collection.TYPE_PAINT;
                break;
            case R.id.museum_fragment_others:
                theType = Collection.TYPE_OTHERS;
                break;
            default:
                break;

        }

        Intent intent = new Intent(getActivity(), TypeColtActivity.class);
        intent.putExtra(COLT_TYPE, theType);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);

    }

    //上拉加载更多
    public void pullMoreFromServer( int curPage) {

        BmobColt bmobColt = BmobColt.getInstance(getActivity());

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
        bmobColt.getHotColt(curPage);
    }

    //刷新数据
    public void refreshFromServer() {
        BmobColt bmobColt = BmobColt.getInstance(getActivity());
        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Collection> collectionList = (List<Collection>) Obj;
                if (collectionList == null || collectionList.size() == 0) {
                    ToastUtils.toast(getActivity(), "暂无数据");
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
        bmobColt.getHotColt(0);
    }



    @Override
    public void onItemClick(int position) {


        Collection collection = (Collection) datas.get(position);
        Intent intent = new Intent(getActivity(), CollectionActivity.class);
        intent.putExtra(COLT_ID, collection.getObjectId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);
    }



    @Override
    public void onRefresh() {
        refreshFromServer();
    }

    @Override
    public void onLoadMore() {

        pullMoreFromServer(currentPage);
    }
}

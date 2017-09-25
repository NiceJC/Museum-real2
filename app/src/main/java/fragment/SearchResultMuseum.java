package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobMuseum;
import adapter.BaseAdapter;
import adapter.MuseumListAdapter;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.MuseumActivity;
import jintong.museum2.R;
import model.Museum;
import util.ToastUtils;

import static util.ParameterBase.MUSEUM_ID;

/**
 * Created by wjc on 2017/7/15.
 */

public class SearchResultMuseum extends Fragment  implements BaseAdapter.OnItemClickListener, XRecyclerView.LoadingListener{


    private View view;
    private XRecyclerView recyclerView;
    private TextView noMoreData;

    private List<Object> datas=new ArrayList<>();
    private String keyWord;

    private int currentPage=0;

    private MuseumListAdapter adapter;


    private boolean isChecked; //标记  对当前关键字是否已经刷新查找过了
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.search_result_recy,container,false);

        initView();

        return view;

    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private void initView() {



        recyclerView=(XRecyclerView) view.findViewById(R.id.search_result_xrecyc);
        noMoreData= (TextView) view.findViewById(R.id.when_no_data);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new MuseumListAdapter(getActivity(),datas);
        recyclerView.setAdapter(adapter);

        recyclerView.setLoadingListener(this);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);


    }




    //供上一级Fragment调用
    public void initData(String keyWord) {

        this.keyWord=keyWord;
        recyclerView.refresh();

    }

    public void setKeyWord(String keyWord){
        this.keyWord=keyWord;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(keyWord!=null&&!keyWord.equals("")&&!isChecked){
            recyclerView.refresh(); //自动刷新
        }
    }

    //上拉加载更多
    public void pullMoreFromServer( int curPage) {

        BmobMuseum bmobMuseum = BmobMuseum.getInstance(getActivity());

        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Museum> museumList = (List<Museum>) Obj;

                if (museumList == null || museumList.size() == 0) {

                    recyclerView.setNoMore(true);

                } else {
                    datas.addAll(museumList);
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
        bmobMuseum.getBykeyWord(keyWord,curPage);
    }

    //刷新数据
    public void refreshFromServer() {
        BmobMuseum bmobMuseum = BmobMuseum.getInstance(getActivity());
        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Museum> museumList = (List<Museum>) Obj;
                if (museumList == null || museumList.size() == 0) {
                    ToastUtils.toast(getActivity(), "暂无数据");
                    recyclerView.setNoMore(true);
                    noMoreData.setVisibility(View.VISIBLE);

                } else {
                    datas.clear();
                    datas.addAll(museumList);
                    adapter.notifyDataSetChanged();
                    currentPage = 1;
                }
                recyclerView.reset();
                isChecked=true;
            }
            @Override
            public void onFail(Object Obj) {
                recyclerView.reset();
            }
        });
        bmobMuseum.getBykeyWord(keyWord,0);
    }



    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getActivity(), MuseumActivity.class);
        Museum museum= (Museum) datas.get(position);
        intent.putExtra(MUSEUM_ID,museum.getObjectId());
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

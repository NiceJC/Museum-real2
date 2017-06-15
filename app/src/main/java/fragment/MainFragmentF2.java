package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobMuseum;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.MuseumListAdapter;
import entity.Museum;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.MuseumActivity;
import jintong.museum2.R;
import util.ToastUtils;

import static util.ParameterBase.LIMIT;
import static util.ParameterBase.MUSEUM_ID;

/**
 * 展馆列表
 * Created by wjc on 2017/2/14.
 */
public class MainFragmentF2 extends Fragment implements adapter.BaseAdapter.OnItemClickListener,
        adapter.BaseAdapter.OnItemLongClickListener, adapter.BaseAdapter.OnViewClickListener,
        PullBaseView.OnRefreshListener{

    private View view;

    private PullRecyclerView recyclerView;

    private List<Object> datas= new ArrayList<>();

    private LinearLayoutManager manager;

    private MuseumListAdapter adapter;

    private int currentPage=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_2, container, false);


        initViews();
        initDatas();
        initEvents();
        return view;


    }


    private void initViews() {

        recyclerView = (PullRecyclerView) view.findViewById(R.id.recylerview_main_f2);


        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

//        recyclerView.setCanPullUp(false);
//        recyclerView.setCanPullDown(false);

        recyclerView.setOnRefreshListener(this);

        recyclerView.setCanPullUp(false);
        adapter = new MuseumListAdapter(getActivity(), datas, this);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    private void initDatas() {

        if (datas.size() != 0) {
            return;
        }
        pullDataFromServer();

    }

    private void initEvents() {


    }


    @Override
    public void onHeaderRefresh(PullBaseView view) {
        pullDataFromServer();
    }

    @Override
    public void onFooterRefresh(PullBaseView view) {
        pullMoreFromServer(currentPage);
    }

    @Override
    public void onItemClick(int position) {
        Museum museum= (Museum) datas.get(position);
        Intent intent = new Intent(getActivity(), MuseumActivity.class);
        intent.putExtra(MUSEUM_ID,museum.getObjectId());
        startActivity(intent);

        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onViewClick(int position, int viewtype) {

    }


    //从服务器拉取数据，拉取成功后刷新页面
    public void pullDataFromServer() {
        BmobMuseum bmobMuseum = BmobMuseum.getInstance(getActivity());

        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Museum> museumList = (List<Museum>) Obj;

                datas.clear();
                for (Museum museum:museumList){
                    datas.add(museum);
                }
                adapter.notifyDataSetChanged();
                recyclerView.onHeaderRefreshComplete();

                if(museumList.size()<LIMIT){
                    recyclerView.onNoMoreData();
                }
                currentPage=1;

            }

            @Override
            public void onFail(Object Obj) {}
        });
        bmobMuseum.refreshExhibition();

    }

    //从服务器拉取数据，拉取成功后刷新页面
    public void pullMoreFromServer(final int curPage) {
        BmobMuseum bmobMuseum = BmobMuseum.getInstance(getActivity());

        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Museum> museumList = (List<Museum>) Obj;

                //已无更多返回数据
                if(museumList==null|| museumList.size()==0){
                    ToastUtils.toast(getActivity(),"没有更多数据啦");
                }else {
                    for (Museum museum : museumList) {
                        datas.add(museum);
                    }
                    adapter.notifyDataSetChanged();

                    currentPage++;
                }
                recyclerView.onFooterRefreshComplete();

            }

            @Override
            public void onFail(Object Obj) {}
        });
        bmobMuseum.getMoreMuseum(curPage);

    }
}

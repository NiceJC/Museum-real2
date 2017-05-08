package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobExhibition;
import adapter.ExhibitionListRecyclerAdapter;
import entity.Exhibition;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import jintong.museum2.ExhibitionActivity;
import jintong.museum2.R;

/**
 * 热门展览
 * Created by wjc on 2017/2/14.
 */
public class MainFragmentF1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;

    private RecyclerView recyclerView;

    private List<Exhibition> datas = new ArrayList<Exhibition>();

    private LinearLayoutManager manager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private int lastVisibleItem;

    private ExhibitionListRecyclerAdapter adapter;

    private int curPage=0;//当前的页数

    private boolean isLoadingMore = false;
    private boolean noMoreToLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_1, container, false);



        initViews();
        initDatas();
        initEvents();



        return view;


    }


    private void initViews() {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_main_f1);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_main_f1);




        adapter = new ExhibitionListRecyclerAdapter(getContext(), datas);
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


    }

    //获取初始数据
    private void initDatas() {


        if (datas.size() != 0) {
            return;
        }



        //检测data为空后，从服务端拉取数据并刷新
        pullDataFromServer();


    }

    private void initEvents() {

        //上拉加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //加载中就返回，防止重复加载
                if (isLoadingMore) {

                    return;
                }
                if (noMoreToLoad) {

                    return;
                }

                //判断滑动到了底部，从服务器拉取数据进行加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(ExhibitionListRecyclerAdapter.LOADING_MORE);
                    isLoadingMore = true;

                    pullMoreFromServer(curPage);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });


        swipeRefreshLayout.setOnRefreshListener(this);

        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
                        .getDisplayMetrics()));

        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ExhibitionActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);

            }

            @Override
            public void OnItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "long click  " + position, Toast.LENGTH_SHORT).show();

            }

        });


    }

    @Override
    public void onRefresh() {


        pullDataFromServer();


    }

    //从服务器拉取数据，并在拉取成功后刷新页面
    public void pullDataFromServer(){
        BmobExhibition bmobExhibition=BmobExhibition.getInstance(getActivity());

        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Exhibition> exhibitionList= (List<Exhibition>) Obj;


                adapter.refreshItem(exhibitionList, ExhibitionListRecyclerAdapter.PULLUP_LOAD_MORE);
                swipeRefreshLayout.setRefreshing(false);//结束刷新状态
                noMoreToLoad = false;


            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobExhibition.refreshExhibition();

    }

    //上拉加载更多
    public void pullMoreFromServer(int curPage){

        BmobExhibition bmobExhibition=BmobExhibition.getInstance(getActivity());

        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Exhibition> exhibitionList= (List<Exhibition>) Obj;

                            if (exhibitionList.size() < 5) {
                                adapter.addMoreItem(exhibitionList, ExhibitionListRecyclerAdapter.NO_MORE_TO_LOAD);

                                noMoreToLoad = true;

                            } else {
                                adapter.addMoreItem(exhibitionList, ExhibitionListRecyclerAdapter.PULLUP_LOAD_MORE);
                            }

                            isLoadingMore = false;

            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobExhibition.getMoreExhibition(curPage);

    }


}

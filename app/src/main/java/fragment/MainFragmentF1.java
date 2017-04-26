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

import adapter.ExhibitionListRecyclerAdapter;
import entity.Exhibition;
import interfaces.OnItemClickListener;
import jintong.museum2.ExhibitionActivity;
import jintong.museum2.R;

/**
 * 热门展览
 * Created by wjc on 2017/2/14.
 *
 *
 */
public class MainFragmentF1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;

    private RecyclerView recyclerView;

    private List<Exhibition> datas=new ArrayList<Exhibition>();

    private LinearLayoutManager manager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private int lastVisibleItem;

    private ExhibitionListRecyclerAdapter adapter;

    private boolean isLoadingMore = false;
    private boolean noMoreToLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_1, container, false);


        Log.e("*****","oncreateView");
        initViews();
        initDatas();
        initEvents();


        adapter = new ExhibitionListRecyclerAdapter(getContext(), datas);

        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isLoadingMore) {

                    return;
                }
                if (noMoreToLoad) {

                    return;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(ExhibitionListRecyclerAdapter.LOADING_MORE);
                    isLoadingMore = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<Exhibition> exhibitions = new ArrayList<Exhibition>();
                            for (int i = 0; i < 4; i++) {
                                List<String> urls = new ArrayList<String>();
                                urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg");
                                Exhibition exhibition = new Exhibition();
                                exhibition.setExhibitName("吕时之艺术陈列  主题展");
                                exhibition.setImageURLs(urls);
                                exhibition.setLocateCity("浙江省杭州市");
                                exhibition.setMuseumName("杭州市博物馆");


                                exhibitions.add(exhibition);
                            }
                            if (exhibitions.size() < 6) {
                                adapter.addMoreItem(exhibitions, ExhibitionListRecyclerAdapter.NO_MORE_TO_LOAD);

                                noMoreToLoad = true;

                            } else {
                                adapter.addMoreItem(exhibitions, ExhibitionListRecyclerAdapter.PULLUP_LOAD_MORE);
                            }

                            isLoadingMore = false;
                            Toast.makeText(getActivity(), "加载完成...", Toast.LENGTH_SHORT).show();

                        }
                    }, 1000);
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

//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    swipeRefreshLayout.setRefreshing(true);
//                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
//
//                }
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = manager.findLastVisibleItemPosition();
//
//            }
//        });

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


        return view;


    }


    private void initViews() {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_main_f1);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_main_f1);


    }

    private void initDatas() {

        if(datas.size()!=0){
            return;
        }

        for (int i = 0; i < 4; i++) {
            List<String> urls = new ArrayList<String>();
            urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/a220dd564081640d809bf930eef6f732.jpg");
            Exhibition exhibition = new Exhibition();
            exhibition.setExhibitName("吕时之艺术陈列  主题展");
            exhibition.setImageURLs(urls);
            exhibition.setLocateCity("浙江省杭州市");
            exhibition.setMuseumName("杭州市博物馆");


            datas.add(exhibition);
        }


    }

    private void initEvents() {


    }

    @Override
    public void onRefresh() {

        Log.e("*****","onRefresh");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Exhibition> exhibitions = new ArrayList<Exhibition>();
                for (int i = 0; i < 4; i++) {
                    List<String> urls = new ArrayList<String>();
                    urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg");
                    Exhibition exhibition = new Exhibition();
                    exhibition.setExhibitName("吕时之艺术陈列  主题展");
                    exhibition.setImageURLs(urls);
                    exhibition.setLocateCity("浙江省杭州市");
                    exhibition.setMuseumName("杭州市博物馆");


                    exhibitions.add(exhibition);
                }
                adapter.refreshItem(exhibitions, ExhibitionListRecyclerAdapter.PULLUP_LOAD_MORE);

                swipeRefreshLayout.setRefreshing(false);
                noMoreToLoad=false;


                Toast.makeText(getActivity(), "刷新完成...", Toast.LENGTH_SHORT).show();



            }
        }, 1000);


    }
}

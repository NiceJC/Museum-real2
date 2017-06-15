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
import java.util.Objects;

import BmobUtils.BmobExhibition;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.ExhibitionListAdapter;
import entity.Exhibition;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.ExhibitionActivity;
import jintong.museum2.R;
import util.ToastUtils;

import static util.ParameterBase.EXHIBITION_ID;

/**
 * 热门展览
 * Created by wjc on 2017/2/14.
 */
public class MainFragmentF1 extends Fragment implements adapter.BaseAdapter.OnItemClickListener,
        adapter.BaseAdapter.OnItemLongClickListener, adapter.BaseAdapter.OnViewClickListener,
        PullBaseView.OnRefreshListener{

    private View view;

    private PullRecyclerView recyclerView;

    private List<Object> datas = new ArrayList<Object>();

    private LinearLayoutManager manager;

//    private SwipeRefreshLayout swipeRefreshLayout;

//    private int lastVisibleItem;

    private ExhibitionListAdapter adapter;
//
    private int currentPage = 0;//当前的页数
//
//    private boolean isLoadingMore = false;
//    private boolean noMoreToLoad = false;

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

//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_main_f1);
        recyclerView = (PullRecyclerView) view.findViewById(R.id.main_fragment_1_recy);





        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        recyclerView.setOnRefreshListener(this);

        adapter = new ExhibitionListAdapter(getActivity(), datas, this);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    //获取初始数据
    //上载数据
    private void initDatas() {
        if (datas.size() != 0) {
            return;
        }


        //检测data为空后，从服务端拉取数据并刷新
        pullDataFromServer();



    }

    private void initEvents() {

    }



    //从服务器拉取数据，并在拉取成功后刷新页面
    public void pullDataFromServer() {
        BmobExhibition bmobExhibition = BmobExhibition.getInstance(getActivity());

        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Exhibition> exhibitionList = (List<Exhibition>) Obj;
//
                datas.clear();
                for (Exhibition exhibition:exhibitionList){
                    datas.add(exhibition);
                }
                adapter.notifyDataSetChanged();
                recyclerView.onHeaderRefreshComplete();
                currentPage=1;

            }

            @Override
            public void onFail(Object Obj) {}
        });
        bmobExhibition.refreshExhibition();

    }

    //上拉加载更多
    public void pullMoreFromServer(final int curPage) {

        BmobExhibition bmobExhibition = BmobExhibition.getInstance(getActivity());

        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Exhibition> exhibitionList = (List<Exhibition>) Obj;

                if(exhibitionList==null||exhibitionList.size()==0){
                    ToastUtils.toast(getActivity(),"没有更多数据啦");

                }else{
                    if(curPage==0){
                        datas.clear();
                    }
                    for (Exhibition exhibition:exhibitionList){
                        datas.add(exhibition);
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
        bmobExhibition.getMoreExhibition(curPage);

    }


    /**
     *  RecyclerView   Item点击事件
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ExhibitionActivity.class);
        Exhibition exhibition= (Exhibition) datas.get(position);
        intent.putExtra(EXHIBITION_ID,exhibition.getObjectId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);

    }

    /**
     *  RecyclerView   Item长按事件
     * @param position
     */
    @Override
    public void onItemLongClick(int position) {

    }

    /**
     * 子View点击事件，switch区分各个子View
     * @param position item position
     * @param viewtype 点击的view的类型，调用时根据不同的view传入不同的值加以区分
     */
    @Override
    public void onViewClick(int position, int viewtype) {

        switch (viewtype){
            default:
                break;
        }
    }

    //下拉刷新
    @Override
    public void onHeaderRefresh(PullBaseView view) {

        pullDataFromServer();

    }

    //上拉加载
    @Override
    public void onFooterRefresh(PullBaseView view) {
        pullMoreFromServer(currentPage);


    }

}

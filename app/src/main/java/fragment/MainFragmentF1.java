package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobExhibition;
import adapter.BaseAdapter;
import adapter.ExhibitionListAdapter;
import model.Exhibition;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.ExhibitionActivity;
import jintong.museum2.R;
import util.ToastUtils;

import static util.ParameterBase.EXHIBITION_ID;

/**
 * 热门展览
 * Created by wjc on 2017/2/14.
 */
public class MainFragmentF1 extends Fragment implements BaseAdapter.OnItemClickListener, XRecyclerView.LoadingListener {

    private View view;



    private List<Object> datas = new ArrayList<Object>();

    private LinearLayoutManager manager;

    private XRecyclerView mRecyclerView;

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



        mRecyclerView = (XRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);


        adapter = new ExhibitionListAdapter(getActivity(), datas);
        adapter.setOnItemClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

    }

    //获取初始数据
    //上载数据
    private void initDatas() {
        if (datas.size() != 0) {
            return;
        }


        //检测data为空后，从服务端拉取数据并刷新
       autoRefresh();

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
                mRecyclerView.refreshComplete();
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
                mRecyclerView.loadMoreComplete();
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




    private void autoRefresh() {
        mRecyclerView.refresh();


}

    @Override
    public void onRefresh() {

        pullDataFromServer();
    }

    @Override
    public void onLoadMore() {

        pullMoreFromServer(currentPage);
    }
}

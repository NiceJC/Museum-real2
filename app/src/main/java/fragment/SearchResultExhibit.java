package fragment;

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

import bmobUtils.BmobExhibition;
import adapter.BaseAdapter;
import adapter.ExhibitionListAdapter;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;
import model.Exhibition;
import util.ToastUtils;

/**
 * Created by wjc on 2017/7/15.
 */

public class SearchResultExhibit extends Fragment implements BaseAdapter.OnItemClickListener, XRecyclerView.LoadingListener {


    private View view;
    private XRecyclerView recyclerView;
    private TextView noMoreData;

    private List<Object> datas=new ArrayList<>();
    private String keyWord;

    private int currentPage=0;

    private ExhibitionListAdapter adapter;


    private boolean isChecked;


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

        adapter=new ExhibitionListAdapter(getActivity(),datas);
        recyclerView.setAdapter(adapter);

        recyclerView.setLoadingListener(this);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);




    }

    public void initData(String keyWord) {

        this.keyWord=keyWord;
        recyclerView.refresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(keyWord!=null&&!keyWord.equals("")&&!isChecked){
            recyclerView.refresh(); //自动刷新
        }
    }

    public void setKeyWord(String keyWord){
        this.keyWord=keyWord;
    }
    //上拉加载更多
    public void pullMoreFromServer( int curPage) {

        BmobExhibition bmobExhibition = BmobExhibition.getInstance(getActivity());

        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Exhibition> exhibitionList = (List<Exhibition>) Obj;

                if (exhibitionList == null || exhibitionList.size() == 0) {

                    recyclerView.setNoMore(true);

                } else {
                    datas.addAll(exhibitionList);
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
        bmobExhibition.getBykeyWord(keyWord,curPage);
    }

    //刷新数据
    public void refreshFromServer() {
        BmobExhibition bmobExhibition = BmobExhibition.getInstance(getActivity());
        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Exhibition> exhibitionList = (List<Exhibition>) Obj;
                if (exhibitionList == null || exhibitionList.size() == 0) {
                    ToastUtils.toast(getActivity(), "暂无数据");
                    recyclerView.setNoMore(true);
                    noMoreData.setVisibility(View.VISIBLE);

                } else {
                    datas.clear();
                    datas.addAll(exhibitionList);
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
        bmobExhibition.getBykeyWord(keyWord,0);
    }



    @Override
    public void onItemClick(int position) {




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

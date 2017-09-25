package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobColt;
import adapter.BaseAdapter;
import adapter.CollectionGridAdapter;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;
import model.Collection;
import util.ToastUtils;

/**
 * Created by wjc on 2017/7/15.
 */

public class SearchResultColt  extends Fragment implements BaseAdapter.OnItemClickListener, XRecyclerView.LoadingListener {

    private View view;
    private XRecyclerView recyclerView;
    private TextView noMoreData;

    private List<Object> datas=new ArrayList<>();
    private String keyWord;

    private int currentPage=0;

    private CollectionGridAdapter adapter;

    private boolean isChecked=false; //标记  对当前关键字是否已经刷新查找过了,默认没有

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



                adapter = new CollectionGridAdapter(getActivity(), datas);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recyclerView.setLoadingListener(this);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);




    }

    @Override
    public void onResume() {
        super.onResume();
        if(keyWord!=null&&!keyWord.equals("")&&!isChecked){
            recyclerView.refresh(); //自动刷新

        }
    }

    public void initData(String keyWord) {

        this.keyWord=keyWord;
       recyclerView.refresh();

    }


    public void setKeyWord(String keyWord){
        this.keyWord=keyWord;
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
        bmobColt.getBykeyWord(keyWord,curPage);
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
                    noMoreData.setVisibility(View.VISIBLE);

                } else {
                    datas.clear();
                    datas.addAll(collectionList);
                    adapter.notifyDataSetChanged();
                    currentPage = 1;
                }
                isChecked=true;  //刷新后 将标记置为true
                recyclerView.reset();
            }
            @Override
            public void onFail(Object Obj) {
                recyclerView.reset();
            }
        });
        bmobColt.getBykeyWord(keyWord,0);
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

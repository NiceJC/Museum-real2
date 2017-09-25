package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.BaseAdapter;
import adapter.UserListAdapter;
import bmobUtils.BmobSocialUtil;
import bmobUtils.BmobUserRelation;
import cn.bmob.v3.BmobUser;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;
import jintong.museum2.UserInfoActivity;
import model.User;

import static util.ParameterBase.USER_ID;
import static util.ParameterBase.USER_RELATION_ID;

/**
 * Created by wjc on 2017/6/19.
 */

public class FansFragment extends Fragment implements
        BaseAdapter.OnItemClickListener {

    private View view;
    private LinearLayoutManager manager;
    private XRecyclerView mRecyclerView;
    private List<Object> mDatas = new ArrayList<Object>();
    private UserListAdapter mAdapter;
    private int currentPage = 0;
    private String userRelationID;
    private ImageView whenNoData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment_1, container, false);


        initViews();
        initDatas();


        return view;


    }

    private void initViews() {

        userRelationID = getActivity().getIntent().getStringExtra(USER_RELATION_ID);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView_community_f1);

        whenNoData= (ImageView) view.findViewById(R.id.when_no_data);

        //设置RecyclerView的布局管理
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                refreshFromServer();
            }

            @Override
            public void onLoadMore() {

                getMoreFromServer(currentPage);
            }
        });

        mAdapter = new UserListAdapter(getActivity(), mDatas);


        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

    }

    //获取最新的一组Blog
    private void initDatas() {

        if (mDatas.size() != 0) {
            return;
        }
        mRecyclerView.refresh();

    }


    //从服务器拉取数据
    public void refreshFromServer() {


        BmobUserRelation bmobUserRelation = BmobUserRelation.getInstance(getActivity());
        bmobUserRelation.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                List<User> userList = (List<User>) Obj;

                if (userList == null || userList.size() == 0) {

                    whenNoData.setVisibility(View.VISIBLE);
                    mDatas.clear();

                    mAdapter.notifyDataSetChanged();

                } else {

                    whenNoData.setVisibility(View.GONE);
                    mDatas.clear();
                    mDatas.addAll(userList);
                    mAdapter.notifyDataSetChanged();
                    currentPage = 1;
                }
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onFail(Object Obj) {
                mRecyclerView.refreshComplete();
            }
        });


        bmobUserRelation.getFans(0, userRelationID);


    }

    public void getMoreFromServer(int curPage) {


        BmobUserRelation bmobUserRelation = BmobUserRelation.getInstance(getActivity());
        bmobUserRelation.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                List<User> userList = (List<User>) Obj;

                if (userList == null || userList.size() == 0) {


                } else {

                    mDatas.addAll(userList);
                    mAdapter.notifyDataSetChanged();
                    currentPage++;
                }
                mRecyclerView.loadMoreComplete();
            }

            @Override
            public void onFail(Object Obj) {
                mRecyclerView.loadMoreComplete();
            }
        });


        bmobUserRelation.getFans(curPage, userRelationID);

    }


    @Override
    public void onItemClick(int position) {

        User user = (User) mDatas.get(position);
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra(USER_ID, user.getObjectId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);


    }
}

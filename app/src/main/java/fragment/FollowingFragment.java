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

import BmobUtils.BmobSocialUtil;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.BlogListAdapter;
import adapter.UserListAdapter;
import cn.bmob.v3.BmobUser;
import entity.Blog;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import jintong.museum2.BlogActivity;
import jintong.museum2.R;
import util.ToastUtils;


/**
 * 关注人列表
 * Created by wjc on 2017/6/19.
 */

public class FollowingFragment extends Fragment implements
        PullBaseView.OnRefreshListener, OnItemClickListener {

    private View view;

    private LinearLayoutManager manager;
    private PullRecyclerView mRecyclerView;
    private List<User> mDatas = new ArrayList<User>();
    private UserListAdapter mAdapter;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment_1, container, false);


        initViews();
        initDatas();


        return view;


    }

    private void initViews() {

        mRecyclerView = (PullRecyclerView) view.findViewById(R.id.recylerview_community_f1);


        //设置RecyclerView的布局管理
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setOnRefreshListener(this);

        mAdapter = new UserListAdapter(mDatas, getActivity());


        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);


    }

    //获取最新的一组Blog
    private void initDatas() {

        if (mDatas.size() != 0) {
            return;
        }
        refreshFromServer();

    }

    @Override
    public void onHeaderRefresh(PullBaseView view) {

        refreshFromServer();
    }

    @Override
    public void onFooterRefresh(PullBaseView view) {

        getMoreFromServer(currentPage);
    }


    //从服务器拉取数据
    public void refreshFromServer() {

        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(getActivity());

        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<User> userList = (List<User>) Obj;


                mDatas.clear();
                mDatas.addAll(userList);
                mAdapter.notifyDataSetChanged();
                currentPage = 1;
                mRecyclerView.onHeaderRefreshComplete();
            }


            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobSocialUtil.getFollowing(BmobUser.getCurrentUser(User.class).getObjectId(), 0);


    }

    public void getMoreFromServer(int curPage) {


        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(getActivity());

        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<User> userList = (List<User>) Obj;

                if (userList == null || userList.size() == 0) {


                } else {

                    mDatas.addAll(userList);
                    mAdapter.notifyDataSetChanged();
                    currentPage++;
                }
                mRecyclerView.onFooterRefreshComplete();


            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobSocialUtil.getFollowing(BmobUser.getCurrentUser(User.class).getObjectId(), curPage);
    }


    //Item的点击事件
    @Override
    public void onItemClick(View view, int position) {
        ToastUtils.toast(getActivity(),"aiya ");
    }

    @Override
    public void OnItemLongClick(View view, int position) {

    }
}
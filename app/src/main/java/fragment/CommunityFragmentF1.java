package fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import BmobUtils.BmobExhibition;
import BmobUtils.BmobSocialUtil;
import MyView.DividerItemDecoration;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.BlogListAdapter;
import adapter.BlogRecyclerAdapter;
import cn.bmob.v3.BmobUser;
import entity.Blog;
import entity.Exhibition;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import jintong.museum2.BlogActivity;
import jintong.museum2.R;
import util.ToastUtils;

import static util.ParameterBase.BLOG_ID;

/**
 * Created by wjc on 2017/2/14.
 */
public class CommunityFragmentF1 extends Fragment implements adapter.BaseAdapter.OnItemClickListener,
        adapter.BaseAdapter.OnItemLongClickListener, adapter.BaseAdapter.OnViewClickListener,
        PullBaseView.OnRefreshListener {

    private View view;

    private LinearLayoutManager manager;
    private PullRecyclerView mRecyclerView;
    private List<Object> mDatas = new ArrayList<Object>();
    private BlogListAdapter mAdapter;


    private int currentPage=0;

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

        mAdapter = new BlogListAdapter(getActivity(), mDatas, this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    //获取最新的一组Blog
    private void initDatas() {

        if(mDatas.size()!=0){
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

    @Override
    public void onItemClick(int position) {

        Blog blog= (Blog) mDatas.get(position);
        String blogID=blog.getObjectId();
        Intent intent = new Intent(getActivity(), BlogActivity.class);
        intent.putExtra(BLOG_ID,blogID);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onViewClick(int position, int viewtype) {

        Blog blog= (Blog) mDatas.get(position);
        String blogID=blog.getObjectId();
        switch (viewtype) {
            case 1: //点击头像 进入个人详情页
                ToastUtils.toast(getActivity(), "touxiang");
                break;
            case 2: //评论  进入Blog详情页，并直接拉起输入框

                Intent intent=new Intent(getActivity(),BlogActivity.class);
                intent.putExtra(BLOG_ID,blogID);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.none);
                break;

            default:
                break;

        }

    }




    //点赞Blog
    public void likeBlog(){

    }


    //取消点赞Blog
    public void cancelLikeBlog(){

    }



    //从服务器拉取数据
    public void refreshFromServer() {

        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(getActivity());

        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Blog> blogList = (List<Blog>) Obj;

                if (blogList == null || blogList.size() == 0) {


                } else {

                    mDatas.clear();
                    mDatas.addAll(blogList);
                    mAdapter.notifyDataSetChanged();
                    currentPage=1;
                }
                mRecyclerView.onHeaderRefreshComplete();


            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobSocialUtil.getRecentBlog(0);


    }

    public void getMoreFromServer(int curPage){




        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(getActivity());

        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Blog> blogList = (List<Blog>) Obj;

                if (blogList == null || blogList.size() == 0) {


                } else {

                    for (Blog blog : blogList) {
                        mDatas.add(blog);
                    }
                    mAdapter.notifyDataSetChanged();
                    currentPage++;
                }
                mRecyclerView.onFooterRefreshComplete();


            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobSocialUtil.getRecentBlog(curPage);
    }


}

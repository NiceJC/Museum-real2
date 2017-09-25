package fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;


import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobSocialUtil;
import adapter.BaseAdapter;
import adapter.BlogListAdapter;
import db.MuseumDB;
import jintong.museum2.MainActivity;
import jintong.museum2.UserInfoActivity;
import model.Blog;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.BlogActivity;
import jintong.museum2.R;
import util.ToastUtils;

import static util.ParameterBase.BLOG_ID;
import static util.ParameterBase.BLOG_ISLIKED;
import static util.ParameterBase.USER_ID;

/**
 *
 * 刷新时，先将blog数据从服务端拉取过来并显示，
 * 点赞的图标是否亮起，需要将blog的ID与本地数据库个人数据中的点赞blog进行比对，相符就亮起
 *
 * Created by wjc on 2017/2/14.
 */
public class CommunityFragmentF1 extends Fragment implements BaseAdapter.OnItemClickListener,
        BaseAdapter.OnItemLongClickListener, BaseAdapter.OnViewClickListener, XRecyclerView.LoadingListener {

    private View view;

    private LinearLayoutManager manager;
    private XRecyclerView mRecyclerView;
    private List<Object> mDatas = new ArrayList<Object>();
    private BlogListAdapter mAdapter;

    private MuseumDB museumDB;

    private List<Blog> likedBlogs;
    private int currentPage = 0;
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

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView_community_f1);

        whenNoData= (ImageView) view.findViewById(R.id.when_no_data);

//        //设置RecyclerView的布局管理
//        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(manager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
         museumDB=MuseumDB.getInstance(getActivity());
        likedBlogs=  museumDB.loadLikedBlogs();
        mAdapter = new BlogListAdapter(getActivity(), mDatas, this);
        mAdapter.putLikedBlogsInDB(likedBlogs);
        mAdapter.setOnItemClickListener(this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(this);
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





    @Override
    public void onItemClick(int position) {

        Blog blog = (Blog) mDatas.get(position);
        String blogID = blog.getObjectId();
        Intent intent = new Intent(getActivity(), BlogActivity.class);
        intent.putExtra(BLOG_ID, blogID);
        intent.putExtra(BLOG_ISLIKED,blog.getLiked());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onViewClick(int position, int viewtype) {

        Blog blog = (Blog) mDatas.get(position);
        String blogID = blog.getObjectId();
        switch (viewtype) {
            case 1: //点击头像 进入个人详情页
                Intent intent=new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra(USER_ID,blog.getAuthor().getObjectId());
                startActivity(intent);
                break;
            case 2: //评论  进入Blog详情页，并直接拉起输入框

                Intent intent2 = new Intent(getActivity(), BlogActivity.class);
                intent2.putExtra(BLOG_ID, blogID);
                intent2.putExtra(BLOG_ISLIKED,blog.getLiked());
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);
                break;

            default:
                break;

        }

    }


    //从服务器拉取数据
    public void refreshFromServer() {

        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(getActivity());

        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Blog> blogList = (List<Blog>) Obj;

                if (blogList.size() == 0) {


                    whenNoData.setVisibility(View.VISIBLE);
                    mDatas.clear();

                    mAdapter.notifyDataSetChanged();
                    currentPage = 1;

                } else {

                    whenNoData.setVisibility(View.GONE);
                    mDatas.clear();
                    mDatas.addAll(blogList);
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
        bmobSocialUtil.getRecentBlog(0);


    }

    public void getMoreFromServer(int curPage) {


        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(getActivity());

        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Blog> blogList = (List<Blog>) Obj;

                if (blogList == null || blogList.size() == 0) {


                } else {

                    mDatas.addAll(blogList);
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
        bmobSocialUtil.getRecentBlog(curPage);
    }


    @Override
    public void onRefresh() {
        likedBlogs=  museumDB.loadLikedBlogs();
        mAdapter.putLikedBlogsInDB(likedBlogs);
        refreshFromServer();
    }

    @Override
    public void onLoadMore() {

        getMoreFromServer(currentPage);
    }


}

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

import MyView.DividerItemDecoration;
import adapter.BlogRecyclerAdapter;
import entity.Blog;
import interfaces.OnItemClickListener;
import jintong.museum2.BlogActivity;
import jintong.museum2.R;

/**
 * Created by wjc on 2017/2/14.
 */
public class CommunityFragmentF1 extends Fragment {

    private View view;


    private RecyclerView mRecyclerView;
    private List<Blog> mDatas;
    private BlogRecyclerAdapter mAdapter;

//    http://bmob-cdn-4183.b0.upaiyun.com/2017/02/20/2dcd5037401d841b8026fb38b4847ac4.jpg

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment_1, container, false);


        initViews();
        initDatas();


        mAdapter = new BlogRecyclerAdapter(getActivity(), mDatas);

        mRecyclerView.setAdapter(mAdapter);

        //设置RecyclerView的布局管理
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        //设置RecycerView的Item间分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent(getActivity(), BlogActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.none);

            }

            @Override
            public void OnItemLongClick(View view, int position) {
//                Toast.makeText(getActivity(), "longClick  " + position, Toast.LENGTH_SHORT).show();

            }
        });


        return view;


    }

    private void initViews() {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylerview_community_f1);


    }

    //    ("icon")
//    ("time"));
//    ("username"));
//    ("textcontent"));
    private void initDatas() {
        List<String> imgs = new ArrayList<String>();
        String imgs1 = "http://bmob-cdn-4183.b0.upaiyun.com/2016/11/29/118ec3614022872d80d04add7d795346.jpg";
        String imgs2 = "http://t10.baidu.com/it/u=2565424359,3856609610&fm=58#http://t10.baidu.com/it/u=374721516,1427740298&fm=58";
        String imgs3 = "http://t10.baidu.com/it/u=2565424359,3856609610&fm=58#http://t10.baidu.com/it/u=374721516,1427740298&fm=58#http://t11.baidu.com/it/u=3158457091,3429860559&fm=58";
        String imgs4 = "http://t10.baidu.com/it/u=2565424359,3856609610&fm=58#http://t10.baidu.com/it/u=374721516,1427740298&fm=58#http://t11.baidu.com/it/u=3158457091,3429860559&fm=58#http://t12.baidu.com/it/u=732128477,3149312025&fm=58";
        String imgs5 = "http://t10.baidu.com/it/u=2565424359,3856609610&fm=58#http://t10.baidu.com/it/u=374721516,1427740298&fm=58#http://t11.baidu.com/it/u=3158457091,3429860559&fm=58#http://t12.baidu.com/it/u=732128477,3149312025&fm=58#http://t11.baidu.com/it/u=2722915642,3232472693&fm=58";
        String imgs6 = "http://t10.baidu.com/it/u=2565424359,3856609610&fm=58#http://t10.baidu.com/it/u=374721516,1427740298&fm=58#http://t11.baidu.com/it/u=3158457091,3429860559&fm=58#http://t12.baidu.com/it/u=732128477,3149312025&fm=58#http://t11.baidu.com/it/u=2722915642,3232472693&fm=58#http://t12.baidu.com/it/u=1313963321,225077119&fm=58";

        imgs.add(imgs1);
        imgs.add(imgs2);
        imgs.add(imgs3);
        imgs.add(imgs4);
        imgs.add(imgs5);
        imgs.add(imgs6);

        mDatas = new ArrayList<Blog>();
        for (int i = 0; i <= 6; i++) {
            Blog blog = new Blog();


            blog.setIconURL("http://bmob-cdn-4183.b0.upaiyun.com/2017/02/20/2dcd5037401d841b8026fb38b4847ac4.jpg");
            blog.setCommentNums(i);
            blog.setContentText("hahhah哈哈哈  好开心呀 " + (char) i + " 嘿嘿嘿嘿嘿  " + (char) i);
            blog.setPraised(i > 3);
            blog.setWatched(i <= 3);
            blog.setPraiseNums(i * i);
            blog.setUserName("阿超" + i);
            blog.setTime("60年前");

            ArrayList<String> list = new ArrayList<String>();

            if (i == 0) {
                list = null;
            } else {
                String[] imgss = imgs.get(i - 1).split("#");

                for (int j = 0; j < imgss.length; j++) {
                    list.add(imgss[j]);
                }
            }

            blog.setImageURLs(list);


            mDatas.add(blog);


        }

    }

}

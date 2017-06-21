package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobColt;
import BmobUtils.BmobComment;
import adapter.CommentsListAdapter;
import adapter.GridRecyclerAdapter;
import cn.bmob.v3.BmobUser;
import entity.Collection;
import entity.Comments;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;

/**
 *
 * 消息列表
 * Created by wjc on 2017/2/23.
 */

public class MineFragmentF4 extends Fragment {

    private View view;


    private RecyclerView recyclerView;

    CommentsListAdapter adapter;

    private List<Object> datas=new ArrayList<>();

    private TextView whenNoData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_fragment_4, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.mine_f1_recyclerView);

        whenNoData= (TextView) view.findViewById(R.id.when_no_data);

        adapter = new CommentsListAdapter(datas,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void initdatas() {

        getComments();


    }

    @Override
    public void onResume() {
        super.onResume();
        initdatas();
    }

    //获取关注的博物馆
    public void getComments() {
        BmobComment bmobComment=BmobComment.getInstance(getActivity());
        bmobComment.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Comments> list= (List<Comments>) Obj;
                if(list.size()==0){
                    whenNoData.setVisibility(View.VISIBLE);
                }

                datas.clear();
                datas.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobComment.getCommentToColtByUser(BmobUser.getCurrentUser().getObjectId());
    }

}

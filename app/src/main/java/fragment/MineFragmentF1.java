package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobMuseum;
import adapter.GridRecyclerAdapter;
import cn.bmob.v3.BmobUser;
import model.Museum;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;
import model.User;

/**
 * 显示关注的博物馆
 * Created by wjc on 2017/2/23.
 */

public class MineFragmentF1 extends Fragment {

    private View view;


    private RecyclerView recyclerView;

    GridRecyclerAdapter adapter;

    private List<Object> datas=new ArrayList<>();


    private ImageView whenNoData;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_fragment_1, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.mine_f1_recyclerView);

        whenNoData= (ImageView) view.findViewById(R.id.when_no_data);

        adapter = new GridRecyclerAdapter(getActivity(),datas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    private void initdatas() {

        getLikedMuseums();


    }

    @Override
    public void onResume() {
        super.onResume();
        if(BmobUser.getCurrentUser(User.class)==null){
            return;
        }
        initdatas();
    }

    //获取关注的博物馆
    public void getLikedMuseums() {
        BmobMuseum bmobMuseum=BmobMuseum.getInstance(getActivity());
        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Museum> list= (List<Museum>) Obj;
                if(list.size()==0){
                    whenNoData.setVisibility(View.VISIBLE);
                    datas.clear();

                    adapter.notifyDataSetChanged();

                }else{
                    whenNoData.setVisibility(View.GONE);
                    datas.clear();
                    datas.addAll(list);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobMuseum.getLikedMuseums(BmobUser.getCurrentUser().getObjectId());
    }



}

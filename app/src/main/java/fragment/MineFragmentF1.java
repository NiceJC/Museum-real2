package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobMuseum;
import BmobUtils.BmobSocialUtil;
import MyView.GridItemDecoration;
import adapter.MuseumLikeAdapter;
import cn.bmob.v3.BmobUser;
import entity.Museum;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;

/**
 * 显示关注的博物馆
 * Created by wjc on 2017/2/23.
 */

public class MineFragmentF1 extends Fragment {

    private View view;


    private RecyclerView recyclerView;

    MuseumLikeAdapter adapter;

    private List<Museum> datas=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_fragment_1, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.mine_f1_recyclerView);



        adapter = new MuseumLikeAdapter(getActivity(), datas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        initdatas();
        return view;
    }

    private void initdatas() {

        getLikedMuseums();


    }

    //获取关注的博物馆
    public void getLikedMuseums() {
        BmobMuseum bmobMuseum=BmobMuseum.getInstance(getActivity());
        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Museum> list= (List<Museum>) Obj;
                datas.clear();
                datas.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobMuseum.getLikedMuseums(BmobUser.getCurrentUser().getObjectId());




    }


}

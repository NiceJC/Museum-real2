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

import MyView.GridItemDecoration;
import adapter.MuseumLikeAdapter;
import entity.Museum;
import jintong.museum2.R;

/**
 * Created by wjc on 2017/2/23.
 */

public class MineFragmentF1 extends Fragment {

    private View view;
    private List<Museum> datas;

    private RecyclerView recyclerView;

    MuseumLikeAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.mine_fragment_1,container,false);

        recyclerView= (RecyclerView) view.findViewById(R.id.mine_f1_recyclerView);

        initdatas();

        adapter=new MuseumLikeAdapter(getActivity(),datas);
        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        return view;
    }

   String u0= "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg";
    String u1= "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg";
    String u2= "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg";
    String u3= "http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg";
    private void initdatas() {

        List<String> us=new ArrayList<String>();
        us.add(u0);
        us.add(u1);
        us.add(u2);
        us.add(u3);

        datas=new ArrayList<Museum>();
        for(int i=0;i<=3;i++){

            Museum museum=new Museum();
            List<String> urls=new ArrayList<String>();
            urls.add(us.get(i));
            museum.setMuseumName(i+"号博物馆");
            museum.setImageURLs(urls);

            datas.add(museum);

        }


    }


}

package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.MuseumListAdapter;
import entity.Exhibition;
import entity.Museum;
import interfaces.OnItemClickListener;
import jintong.museum2.MuseumActivity;
import jintong.museum2.R;

/**
 * 展馆列表
 * Created by wjc on 2017/2/14.
 */
public class MainFragmentF2 extends Fragment {

    private View view;

    private RecyclerView recyclerView;

    private List<Museum> datas;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_2, container, false);


        initViews();
        initDatas();
        initEvents();


        MuseumListAdapter adapter = new MuseumListAdapter(getContext(), datas);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(getActivity(),MuseumActivity.class);
                startActivity(intent);

               getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.none);

            }

            @Override
            public void OnItemLongClick(View view, int position) {
                Toast.makeText(getActivity(),"long click "+position,Toast.LENGTH_SHORT).show();

            }
        });

        return view;


    }


    private void initViews() {

        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview_main_f2);


    }

    private void initDatas() {
        datas = new ArrayList<Museum>();
        for (int i = 0; i < 6; i++) {
            List<String> urls = new ArrayList<String>();
            urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg");
            Museum museum = new Museum();
            museum.setMuseumName("杭州市博物馆");
            museum.setImageURLs(urls);
            museum.setLocateCity("浙江省杭州市");


            datas.add(museum);
        }


    }

    private void initEvents() {


    }
}

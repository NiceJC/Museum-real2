package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.SearchingResultAdapter;
import entity.Collection;
import entity.Exhibition;
import entity.Museum;
import jintong.museum2.R;

/**
 * Created by wjc on 2017/3/22.
 */

public class SearchingFragmentF2 extends Fragment {

    private View view;

    private LinearLayout museumLayout;
    private RecyclerView museumRecyclerView;

    private LinearLayout exhibitionLayout;
    private RecyclerView exhibitionRecyclerView;

    private LinearLayout coltLayout;
    private RecyclerView coltRecyclerView;


    private List<Museum> museumList;
    private List<Exhibition> exhibitionList;
    private List<Collection> collectionList;
    private TextView noResultText;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment_2, container, false);

        initView();
        initData();
        setData();
        initEvents();


        return view;
    }

    private void setData() {

        //没有结果 直接返回
        if (museumList.size() == 0 && exhibitionList.size() == 0 && collectionList.size() == 0) {
            noResultText.setVisibility(View.VISIBLE);
            return;
        }


        SearchingResultAdapter adapter = new SearchingResultAdapter
                (getActivity(), museumList, exhibitionList, collectionList);

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


    }

    private void initView() {

        noResultText = (TextView) view.findViewById(R.id.noResultText);
        recyclerView = (RecyclerView) view.findViewById(R.id.search_2_recyclerView);

    }

    private void initData() {
        museumList = new ArrayList<Museum>();
        exhibitionList = new ArrayList<Exhibition>();
        collectionList = new ArrayList<Collection>();

        for (int i = 0; i < 1; i++) {
            List<String> urls = new ArrayList<String>();
            urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg");
            Museum museum = new Museum();
            museum.setMuseumName("杭州市博物馆");
            museum.setImageURLs(urls);
            museum.setLocateCity("浙江省杭州市");


            museumList.add(museum);
        }


        for (int i = 0; i < 2; i++) {
            List<String> urls = new ArrayList<String>();
            urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/04/e91d99f1407610818055e4b642ef040a.jpg");
            Exhibition exhibition = new Exhibition();
            exhibition.setExhibitName("吕时之艺术陈列  主题展");
            exhibition.setImageURLs(urls);
            exhibition.setLocateCity("浙江省杭州市");
            exhibition.setMuseumName("杭州市博物馆");
            exhibitionList.add(exhibition);
        }

        for (int i = 0; i < 3; i++) {
            Collection collection = new Collection();
            List<String> urls = new ArrayList<String>();
            urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/38ef58db401620a38093b48211c1a027.jpg");
            collection.setColtLikeNum(9955);
            collection.setColtImageURLs(urls);
            collection.setColtName("鸟纹包月瓶");
            collection.setColtDynasty("清朝");
            collection.setColtSize("高十米，宽十米");
            collection.setColtIntru("清末民国时期使用的称量粮食的工具，一面书“㕠聚号记”，一面书“校准市斗”。");

            collection.setColtCommentNum(996);
            collectionList.add(collection);

        }


    }

    private void initEvents() {

    }
}

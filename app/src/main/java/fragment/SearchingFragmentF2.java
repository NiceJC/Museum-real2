package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobColt;
import BmobUtils.BmobExhibition;
import BmobUtils.BmobMuseum;
import adapter.SearchingResultAdapter;
import entity.Collection;
import entity.Exhibition;
import entity.Museum;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;
import util.ToastUtils;

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


    private List<Museum> museumList = new ArrayList<Museum>();
    private List<Exhibition> exhibitionList = new ArrayList<Exhibition>();
    private List<Collection> collectionList = new ArrayList<Collection>();

    private TextView noResultText;
    private RecyclerView recyclerView;
    private SearchingResultAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment_2, container, false);


        initView();


        initEvents();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        setData();

    }

    private void setData() {

//
//
//        //没有结果 设置无结果提示
//        if (museumList.size() == 0 && exhibitionList.size() == 0 && collectionList.size() == 0) {
//            noResultText.setVisibility(View.VISIBLE);
//
//        }

    }

    private void initView() {

        noResultText = (TextView) view.findViewById(R.id.noResultText);
        recyclerView = (RecyclerView) view.findViewById(R.id.search_2_recyclerView);


        adapter = new SearchingResultAdapter
                (getActivity(), museumList, exhibitionList, collectionList);

        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

    }

    private void initData() {

        //从Bundle中获取关键词，不为空就进行搜索
        Bundle bundle = getArguments();
        String keyWord = bundle.getString("keyWord");
        if (keyWord != null && !keyWord.equals("")) {
            Log.e("keyword", keyWord);
            searchMuseum(keyWord);
            searchExhibition(keyWord);
            searchColt(keyWord);
        }


    }


    //根据关键词，从服务断端获取数据
    private void searchMuseum(String keyWord) {

        BmobMuseum bmobMuseum = BmobMuseum.getInstance(getContext());
        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                List<Museum> museums = (List<Museum>) Obj;
                museumList.clear();
                for (int i = 0; i < museums.size(); i++) {
                    museumList.add(museums.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobMuseum.getBykeyWord(keyWord);

    }

    private void searchExhibition(String keyWord) {
        BmobExhibition bmobExhibition = BmobExhibition.getInstance(getActivity());
        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                List<Exhibition> exhibitions = (List<Exhibition>) Obj;
                exhibitionList.clear();
                for (int i = 0; i < exhibitions.size(); i++) {
                    exhibitionList.add(exhibitions.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobExhibition.getBykeyWord(keyWord);

    }

    private void searchColt(String keyWord) {

        BmobColt bmobColt = BmobColt.getInstance(getActivity());
        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                List<Collection> collections = (List<Collection>) Obj;
                collectionList.clear();
                for (int i = 0; i < collections.size(); i++) {
                    collectionList.add(collections.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(Object Obj) {

            }
        });


        bmobColt.getBykeyWord(keyWord);

    }


    private void initEvents() {

    }


}

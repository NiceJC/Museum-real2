package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import jintong.museum2.R;

/**
 *
 * Created by wjc on 2017/3/22.
 */

public class SearchingFragmentF2 extends Fragment {

    private View view;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SearchResultTabAdapter mAdapter;
    private List<Fragment> fragmentList =new ArrayList<>();

    private SearchResultColt searchResultColt;
    private SearchResultMuseum searchResultMuseum;
    private SearchResultExhibit searchResultExhibit;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment_2, container, false);


        initView();


        return view;
    }



    private void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.search_frag2_viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.search_frag2_tab);

        if(mAdapter==null){
            mAdapter=new SearchResultTabAdapter(getChildFragmentManager());
        }
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    /**
     * 每次重新搜索，都将子Fragment的查找标记置为false
     *     重新刷新当前Fragment搜索结果
     *     如果不是当前展示的Fragment，只是将关键字set进去，滑动过去的时候再刷新结果
     */
    public void refreshKeyWords(String keyWord){
        searchResultColt.setChecked(false);
        searchResultExhibit.setChecked(false);
        searchResultMuseum.setChecked(false);


        if(searchResultColt.isVisible()){
            searchResultColt.initData(keyWord);
        }else{
            searchResultColt.setKeyWord(keyWord);
        }
        if(searchResultMuseum.isVisible()){
            searchResultMuseum.initData(keyWord);
        }else{
            searchResultColt.setKeyWord(keyWord);
        }
        if(searchResultExhibit.isVisible()){
            searchResultExhibit.initData(keyWord);
        }else{
            searchResultColt.setKeyWord(keyWord);
        }

    }

    // fragment的适配器类
    class SearchResultTabAdapter extends FragmentPagerAdapter {


        // 标题数组
        String[] titles = {
                "展 品",
                "展 馆",
                "展 览"};

        public SearchResultTabAdapter(FragmentManager fm) {
            super(fm);
            if(fragmentList.size()==0){
                searchResultColt=new SearchResultColt();
                searchResultMuseum=new SearchResultMuseum();
                searchResultExhibit=new SearchResultExhibit();
                fragmentList.add(searchResultColt);
                fragmentList.add(searchResultMuseum);
                fragmentList.add(searchResultExhibit);

            }}

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }



//    private void initData() {
//
//        //从Bundle中获取关键词，不为空就进行搜索
//        Bundle bundle = getArguments();
//        String keyWord = bundle.getString("keyWord");
//        if (keyWord != null && !keyWord.equals("")) {
//            Log.e("keyword", keyWord);
////            searchMuseum(keyWord);
////            searchExhibition(keyWord);
//            searchColt(keyWord);
//        }
//
//
//    }

//
//    //根据关键词，从服务断端获取数据
//    private void searchMuseum(String keyWord) {
//
//        BmobMuseum bmobMuseum = BmobMuseum.getInstance(getContext());
//        bmobMuseum.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
//            @Override
//            public void onSuccess(Object Obj) {
//
//                List<Museum> museums = (List<Museum>) Obj;
//                museumList.clear();
//                for (int i = 0; i < museums.size(); i++) {
//                    museumList.add(museums.get(i));
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFail(Object Obj) {
//
//            }
//        });
//        bmobMuseum.getBykeyWord(keyWord);
//
//    }
//
//    private void searchExhibition(String keyWord) {
//        BmobExhibition bmobExhibition = BmobExhibition.getInstance(getActivity());
//        bmobExhibition.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
//            @Override
//            public void onSuccess(Object Obj) {
//
//                List<Exhibition> exhibitions = (List<Exhibition>) Obj;
//                exhibitionList.clear();
//                for (int i = 0; i < exhibitions.size(); i++) {
//                    exhibitionList.add(exhibitions.get(i));
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFail(Object Obj) {
//
//            }
//        });
//        bmobExhibition.getBykeyWord(keyWord);
//
//    }
//
//    private void searchColt(String keyWord) {
//
//        BmobColt bmobColt = BmobColt.getInstance(getActivity());
//        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
//            @Override
//            public void onSuccess(Object Obj) {
//
//                List<Collection> collections = (List<Collection>) Obj;
//                collectionList.clear();
//                for (int i = 0; i < collections.size(); i++) {
//                    collectionList.add(collections.get(i));
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFail(Object Obj) {
//            }
//        });
//        bmobColt.getBykeyWord(keyWord);
//
//    }




}

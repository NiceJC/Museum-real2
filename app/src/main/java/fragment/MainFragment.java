package fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import jintong.museum2.R;
import jintong.museum2.SearchingActivity;
import jintong.museum2.TestActivity;

/**
 * Created by wjc on 2017/2/9.
 */
public class MainFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;
    private View view;
    private List<Fragment> fragmentList = new ArrayList<>();

    private ImageView mSearch;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.main_fragment, container, false);


        initView();
        return view;
    }

        private void initView() {

            mSearch= (ImageView) view.findViewById(R.id.main_fragment_search);
            mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), SearchingActivity.class);
                    startActivity(intent);

                }
            });


            // 绑定viewpager与tablayout
            mViewPager = (ViewPager) view.findViewById(R.id.mainfrag_viewpager);
            mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
            // 新建适配器
            if(mTabAdapter==null){
            mTabAdapter = new TabAdapter(getChildFragmentManager());}
            // 设置适配器
            mViewPager.setAdapter(mTabAdapter);
            // 直接绑定viewpager，消除了以前的需要设置监听器的繁杂工作
            mTabLayout.setupWithViewPager(mViewPager);
        }

        // fragment的适配器类
        class TabAdapter extends FragmentPagerAdapter {


            // 标题数组
            String[] titles = {
                    "展·览",
                    "展·馆",
                    "资·讯",
                    "活·动"};

            public TabAdapter(FragmentManager fm) {
                super(fm);
                if(fragmentList.size()==0){
                fragmentList.add(new MainFragmentF1());
                fragmentList.add(new MainFragmentF2());
                fragmentList.add(new MainFragmentF3());
                fragmentList.add(new MainFragmentF4());
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

    }



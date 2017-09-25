package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import jintong.museum2.PostActivity;
import jintong.museum2.R;

/**
 *
 * Created by wjc on 2017/2/9.
 */
public class CommunityFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;
    private View view;
    private List<Fragment> fragmentList = new ArrayList<>();


    private ImageView post;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.community_fragment, container, false);


        initView();
        initEvents();
        return view;
    }

    private void initEvents() {

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.none);
            }
        });


    }

    private void initView() {

        // 绑定viewpager与tablayout
        mViewPager = (ViewPager) view.findViewById(R.id.community_viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.community_tab);
        post= (ImageView) view.findViewById(R.id.post);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        /**
         *新建适配器
         * 注意 在Fragment中内嵌Viewpager需要使用getChildFragmentManager()
         * 来获得FragmentManager，不然会出错
         */

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
                "   新·鲜   ",
                "   关·注   "};

        public TabAdapter(FragmentManager fm) {
            super(fm);
            if(fragmentList.size()==0){
                fragmentList.add(new CommunityFragmentF1());
                fragmentList.add(new CommunityFragmentF2());

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

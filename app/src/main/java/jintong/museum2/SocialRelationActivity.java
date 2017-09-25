package jintong.museum2;

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
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import fragment.CommunityFragment;
import fragment.CommunityFragmentF1;
import fragment.CommunityFragmentF2;
import fragment.FansFragment;
import fragment.FollowingFragment;

/**
 * Created by wjc on 2017/6/19.
 */

public class SocialRelationActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SocialRelationActivity.TabAdapter mTabAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_relation);


        initView();
        initData();
        initEvents();
    }

    private void initData() {

    }





    private void initEvents() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.none,R.anim.out_to_right);
            }
        });

    }

    private void initView() {

        // 绑定viewpager与tablayout
        mViewPager = (ViewPager) findViewById(R.id.community_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.community_tab);
        back= (ImageView) findViewById(R.id.social_back);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);


        /**
         *新建适配器
         * 注意 在Fragment中内嵌Viewpager需要使用getChildFragmentManager()
         * 来获得FragmentManager，不然会出错
         */

        if(mTabAdapter==null){
            mTabAdapter = new TabAdapter(getSupportFragmentManager());
        }


        // 设置适配器
        mViewPager.setAdapter(mTabAdapter);
        // 直接绑定viewpager，消除了以前的需要设置监听器的繁杂工作
        mTabLayout.setupWithViewPager(mViewPager);




    }

    // fragment的适配器类
    class TabAdapter extends FragmentPagerAdapter {


        // 标题数组
        String[] titles = {
                "   关·注   ",
                "   粉·丝   "};

        public TabAdapter(FragmentManager fm) {
            super(fm);
            if(fragmentList.size()==0){
                fragmentList.add(new FollowingFragment());
                fragmentList.add(new FansFragment());

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

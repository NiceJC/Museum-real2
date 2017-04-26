package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import BmobUtils.BmobRegisterAndLogin;
import MyView.ChangeColorView;
import entity.User;
import jintong.museum2.R;
import jintong.museum2.SetUpActivity;
import util.ToastUtils;

/**
 * Created by wjc on 2017/2/9.
 */
public class MineFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {


    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private String[] mTittles = new String[]{
            "mineFragmentF1", "mineFragmentF2", "mineFragmentF3", "mineFragmentF4"

    };

    private ImageView toSetUp;

    private View view;

    private FragmentPagerAdapter mAdapter;

    private int mSelectedFragment;

    private List<LinearLayout> mTabIndicators ;

    private MineFragmentF1 mineFragmentF1;
    MineFragmentF2 mineFragmentF2;
    MineFragmentF3 mineFragmentF3;
    MineFragmentF4 mineFragmentF4;

    private LinearLayout tab1, tab2, tab3, tab4;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_fragment, container, false);


        initViews();
        initDatas();

        initEvents();

        mViewPager.setAdapter(mAdapter);

        resetOtherTabs();
        mTabIndicators.get(0).setSelected(true);
        mViewPager.setCurrentItem(0, true);

        return view;

    }

    private void initEvents() {
        mViewPager.addOnPageChangeListener(this);
        toSetUp.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);


    }

    private void initDatas() {



        mFragments = new ArrayList<Fragment>();
        Log.e("mine", "initDatas");
        for (String title : mTittles) {


            switch (title) {

                case "mineFragmentF1":
                    if (mineFragmentF1 == null) {
                        mineFragmentF1 = new MineFragmentF1();
                    }

                    mFragments.add(mineFragmentF1);

                    break;
                case "mineFragmentF2":
                    if (mineFragmentF2 == null) {
                        mineFragmentF2 = new MineFragmentF2();
                    }

                    mFragments.add(mineFragmentF2);
                    break;
                case "mineFragmentF3":
                    if (mineFragmentF3 == null) {
                        mineFragmentF3 = new MineFragmentF3();
                    }

                    mFragments.add(mineFragmentF3);
                    break;
                case "mineFragmentF4":
                    if (mineFragmentF4 == null) {
                        mineFragmentF4 = new MineFragmentF4();
                    }

                    mFragments.add(mineFragmentF4);
                    break;
                default:
                    break;


            }

        }
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };


    }

    private void initViews() {
        Log.e("mine", "initViews");
        mViewPager = (ViewPager) view.findViewById(R.id.mine_viewPager);
        mTabIndicators= new ArrayList<LinearLayout>();
        tab1 = (LinearLayout) view.findViewById(R.id.mine_indicator_1);
        tab2 = (LinearLayout) view.findViewById(R.id.mine_indicator_2);
        tab3 = (LinearLayout) view.findViewById(R.id.mine_indicator_3);
        tab4 = (LinearLayout) view.findViewById(R.id.mine_indicator_4);

        mTabIndicators.add(tab1);
        mTabIndicators.add(tab2);
        mTabIndicators.add(tab3);
        mTabIndicators.add(tab4);


        toSetUp = (ImageView) view.findViewById(R.id.to_setup);


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.to_setup:


                User currentUser = BmobRegisterAndLogin.chekIfLogin();
                if (currentUser == null) {

                    ToastUtils.toast(getActivity(),"用户尚未登录");
                    break;
                }
                Intent intent = new Intent(getActivity(), SetUpActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.none);
                break;


            case R.id.mine_indicator_1:
                resetOtherTabs();
                Log.e("mine", "click 11");
                tab1.setSelected(true);
                mViewPager.setCurrentItem(0, true);

                break;
            case R.id.mine_indicator_2:
                resetOtherTabs();
                Log.e("mine", "click 22");
                tab2.setSelected(true);
                mViewPager.setCurrentItem(1, true);

                break;
            case R.id.mine_indicator_3:
                resetOtherTabs();
                Log.e("mine", "click 33");
                tab3.setSelected(true);
                mViewPager.setCurrentItem(2, true);

                break;
            case R.id.mine_indicator_4:
                resetOtherTabs();
                Log.e("nine", "click 44");
                tab4.setSelected(true);
                mViewPager.setCurrentItem(3, true);

                break;
            default:
                break;

        }


    }

    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setSelected(false);

        }


    }

    //OnPageChangeListener的实现方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {

        Log.e("mine", "selected" + position);
        resetOtherTabs();
        mTabIndicators.get(position).setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("mine", "onCreate");
    }
}

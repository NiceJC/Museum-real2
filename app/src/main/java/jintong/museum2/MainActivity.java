package jintong.museum2;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import MyView.ChangeColorView;
import fragment.CommunityFragment;
import fragment.MainFragment;
import fragment.MineFragment;
import fragment.MuseumFragment;


public class MainActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private String[] mTittles = new String[]{
            "MainFragment", "MuseumFragment", "CommunityFragment", "MineFragment"

    };


    private FragmentPagerAdapter mAdapter;

    private int mSelectedFragment;


    private MainFragment mainFragment;
    MuseumFragment museumFragment;
    CommunityFragment communityFragment;
    MineFragment mineFragment;


    private LinearLayout one, two, three, four;
    private List<LinearLayout> mTabIndicators ;

    private ImageView oneImage, twoImage, threeImage, fourImage;
    private List<ImageView> mImages;

    private TextView oneText, twoText, threeText, fourText;
    private List<TextView> mTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "ActivityOnCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title


        Log.e("TAG", "onCreate");
        setContentView(R.layout.activity_main);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initView();
        initDatas();

        mViewPager.setAdapter(mAdapter);
        initEvent();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {

        mViewPager.addOnPageChangeListener(this);


    }

    private void initDatas() {
        for (String title : mTittles) {


            switch (title) {

                case "MainFragment":
                    if (mainFragment == null) {
                        mainFragment = new MainFragment();
                    }

                    mTabs.add(mainFragment);

                    break;
                case "MuseumFragment":
                    if (museumFragment == null) {
                        museumFragment = new MuseumFragment();
                    }

                    mTabs.add(museumFragment);
                    break;
                case "CommunityFragment":
                    if (communityFragment == null) {
                        communityFragment = new CommunityFragment();
                    }

                    mTabs.add(communityFragment);
                    break;
                case "MineFragment":
                    if (mineFragment == null) {
                        mineFragment = new MineFragment();
                    }

                    mTabs.add(mineFragment);
                    break;
                default:
                    break;


            }


//            Bundle bundle = new Bundle();
//            bundle.putString(TabFragment.TITTLE, title);
//            tabFragment.setArguments(bundle);
//            mTabs.add(tabFragment);

        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };


    }

    private void initView() {
        mTabIndicators = new ArrayList<LinearLayout>();
        mImages=new ArrayList<ImageView>();
        mTexts=new ArrayList<TextView>();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        one = (LinearLayout) findViewById(R.id.indicator_one);
        mTabIndicators.add(one);
        two = (LinearLayout) findViewById(R.id.indicator_two);
        mTabIndicators.add(two);
        three = (LinearLayout) findViewById(R.id.indicator_three);
        mTabIndicators.add(three);
        four = (LinearLayout) findViewById(R.id.indicator_four);
        mTabIndicators.add(four);


        oneImage = (ImageView) findViewById(R.id.indicator_one_image);
        twoImage = (ImageView) findViewById(R.id.indicator_two_image);
        threeImage = (ImageView) findViewById(R.id.indicator_three_image);
        fourImage = (ImageView) findViewById(R.id.indicator_four_image);
        mImages.add(oneImage);
        mImages.add(twoImage);
        mImages.add(threeImage);
        mImages.add(fourImage);


        oneText = (TextView) findViewById(R.id.indicator_one_text);
        twoText = (TextView) findViewById(R.id.indicator_two_text);
        threeText = (TextView) findViewById(R.id.indicator_three_text);
        fourText = (TextView) findViewById(R.id.indicator_four_text);
        mTexts.add(oneText);
        mTexts.add(twoText);
        mTexts.add(threeText);
        mTexts.add(fourText);



        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        oneImage.setSelected(true);
        oneText.setTextColor(getResources().getColor(R.color.colorPrimary));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        clickTab(v);

        switch (v.getId()) {





        }

    }

    /**
     * 点击Tab
     * 如果是点击下面的tab切换的话，就传递个标记
     */

    public void clickTab(View v) {



        switch (v.getId()) {
            case R.id.indicator_one:
                resetOtherTabs();
                oneImage.setSelected(true);
                oneText.setTextColor(getResources().getColor(R.color.colorPrimary));
                mViewPager.setCurrentItem(0, true);

                break;
            case R.id.indicator_two:
                resetOtherTabs();
                twoImage.setSelected(true);
                twoText.setTextColor(getResources().getColor(R.color.colorPrimary));
                mViewPager.setCurrentItem(1, true);

                break;
            case R.id.indicator_three:
                resetOtherTabs();
                threeImage.setSelected(true);
                threeText.setTextColor(getResources().getColor(R.color.colorPrimary));
                mViewPager.setCurrentItem(2, true);

                break;
            case R.id.indicator_four:
                resetOtherTabs();
                fourImage.setSelected(true);
                fourText.setTextColor(getResources().getColor(R.color.colorPrimary));
                mViewPager.setCurrentItem(3, true);

                break;
            default:
                break;

        }


    }

    /**
     * 重置其他tabIndicator的颜色
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicators.size(); i++) {

            mImages.get(i).setSelected(false);
            mTexts.get(i).setTextColor(getResources().getColor(R.color.sd));



        }

    }

    /**
     *
     * pageChangeListener的几个需要实现的方法
     */
    /**
     * 从第一页滑动到第二页：
     * position=0，直到滑动结束才=1
     * positionOffset 从0.0 缓慢变化到1.0
     * <p/>
     * 从第二页滑动回到第一页：
     * position=0，并且一直是0
     * positionOffset 从1.0 缓慢变化到0.0
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


//        if (positionOffset > 0) {
//
//            ChangeColorView left = mTabIndicators.get(position);
//            ChangeColorView right = mTabIndicators.get(position + 1);
//
//            left.setIconAlpha(1 - positionOffset);
//            right.setIconAlpha(positionOffset);
//
//
//        }


    }

    /**
     * 调用在滑动positionOffset达到0.7的时候
     * 但是如果是点击tab直接切换fragment 也就是通过 mViewPager.setCurrentItem(0, true)，
     * 是先调用本方法再调用 onPageScrolled
     */

    @Override
    public void onPageSelected(int position) {

        mSelectedFragment = position;

    }

    /**
     * 有三种状态（0，1，2）。arg0 ==1的时辰默示正在滑动，arg0==2的时候表示滑动完毕了，arg0==0时表示什么都没做。
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == 0) {

            resetOtherTabs();

            mImages.get(mSelectedFragment).setSelected(true);
            mTexts.get(mSelectedFragment).setTextColor(getResources().getColor(R.color.colorPrimary));


        }

    }


}

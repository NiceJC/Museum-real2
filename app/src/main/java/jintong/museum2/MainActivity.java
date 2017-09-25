package jintong.museum2;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobSocialUtil;
import bmobUtils.BmobUserRelation;
import cn.bmob.v3.BmobUser;
import db.MuseumDB;
import fragment.CommunityFragment;
import fragment.MainFragment;
import fragment.MineFragment;
import fragment.MuseumFragment;
import interfaces.OnBmobReturnWithObj;
import model.Blog;
import model.User;
import util.MyLocationListener;
import util.ToastUtils;


/**
 *需要在这里做一些额外的初始化工作
 *
 * 1、发起定位请求，存储定位结果
 *    定位的时候，注意6.0以上的系统需要针对定位权限进行动态权限请求
 * 2、同步用户数据（关注、收藏等等）
 *
 */
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
    private List<LinearLayout> mTabIndicators;

    private ImageView oneImage, twoImage, threeImage, fourImage;
    private List<ImageView> mImages;

    private TextView oneText, twoText, threeText, fourText;
    private List<TextView> mTexts;

    //百度定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener ;

    public static final int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title


        setContentView(R.layout.activity_main);

        if(RegisterActivity.registerActivity!=null){
            RegisterActivity.registerActivity.finish();
        }
        if(LoginActivity.loginActivity!=null){
            LoginActivity.loginActivity.finish();
        }

        initView();
        initDatas();

        mViewPager.setAdapter(mAdapter);
        initEvent();

    }

    /**
     * 初始化事件
     *
     * 从服务器同步用户的收藏数据
     */
    private void initEvent() {

        mViewPager.addOnPageChangeListener(this);

        updateUserInfo();


        //百度定位初始化LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        myListener= new MyLocationListener(mLocationClient,getApplicationContext());
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数
        initLocation();

        //发起定位之前 先确定权限
        checkLocatePermission();




    }

    private void checkLocatePermission() {

        boolean isGranted= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        if(isGranted){
            mLocationClient.start();
        }else{

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length >0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    mLocationClient.start();
                }else{
                    //用户拒绝授权
                    ToastUtils.toast(this,"没有定位权限将导致定位失败，用户可以前往应用权限进行设置");
                }
                break;
        }
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        /**
         *
         高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
         低功耗定位模式：这种定位模式下，不会使用GPS进行定位，只会使用网络定位（WiFi定位和基站定位）；
         仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位。
         */
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");//百度经纬度坐标；
        //可选，默认gcj02，设置返回的定位结果坐标系
        mLocationClient.setLocOption(option);
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
        mImages = new ArrayList<ImageView>();
        mTexts = new ArrayList<TextView>();

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
//7676
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


    //从服务器获取用户的数据
    public void updateUserInfo(){
        BmobSocialUtil bmobSocialUtil=BmobSocialUtil.getInstance(this);
        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {


                List<Blog> blogList= (List<Blog>) Obj;
                updateLikedBlogs(blogList);
            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobSocialUtil.getlikedBlogsByUser(BmobUser.getCurrentUser(User.class).getObjectId());


        BmobUserRelation.getInstance(this).updateFollowingAndFansNum(BmobUser.getCurrentUser(User.class).getUserRelationID());

        BmobUserRelation.getInstance2(this).checkUserRelationPointer(BmobUser.getCurrentUser(User.class));

    }


    //将从服务端的获得的用户的数据写入到应用的数据库中
    public void updateLikedBlogs(final List<Blog> blogList){


        new Thread(new Runnable() {
            @Override
            public void run() {

                MuseumDB museumDB=MuseumDB.getInstance(MainActivity.this);
                museumDB.updataLikedBlog(blogList);
            }
        }).start();

    }








}

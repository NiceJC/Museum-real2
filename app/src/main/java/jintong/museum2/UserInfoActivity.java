package jintong.museum2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobRegisterAndLogin;
import bmobUtils.BmobSocialUtil;
import bmobUtils.BmobUserRelation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import db.MuseumDB;
import fragment.MineFragment;
import fragment.MineFragmentF1;
import fragment.MineFragmentF2;
import fragment.MineFragmentF3;
import fragment.MineFragmentF4;
import interfaces.OnBmobReturnWithObj;
import model.User;
import myView.GlideCircleTransform;
import util.ToastUtils;

import static util.ParameterBase.USER_ID;
import static util.ParameterBase.USER_RELATION_ID;

/**
 * 查看其他用户的基本信息
 * Created by wjc on 2017/3/14.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {


    private ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();

    private ImageView back, portrait, watchIcon;


    private int mSelectedFragment;

    private RelativeLayout followerAndFollowing;

    private TextView fans, following, nicNameText;
    private TabLayout mTabLayout;

    private BaseFragmentAdapter mAdapter;
    private String userID;

    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userinfo);
        initViews();
        initDatas();
        initEvents();

    }

    private void initViews() {

        userID = getIntent().getStringExtra(USER_ID);
        mViewPager = (ViewPager) findViewById(R.id.mine_viewPager);


        watchIcon = (ImageView) findViewById(R.id.watch_icon);
        portrait = (ImageView) findViewById(R.id.mine_portrait);
        nicNameText = (TextView) findViewById(R.id.mine_nick_name);
        back = (ImageView) findViewById(R.id.back);
        followerAndFollowing = (RelativeLayout) findViewById(R.id.mine_follower_following);
        fans = (TextView) findViewById(R.id.mine_fans_num);
        following = (TextView) findViewById(R.id.mine_following_num);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

    }


    private void initDatas() {

        if (mAdapter == null) {

            mAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        }

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        getUserInfo();
    }

    private void initEvents() {

        back.setOnClickListener(this);
        followerAndFollowing.setOnClickListener(this);

        watchIcon.setOnClickListener(this);
    }


    //从网络获取用户个人信息
    private void getUserInfo() {

        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(this);
        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                user = (User) Obj;
                setUserInfo(user);

                checkIfWatched(user.getObjectId());
                setFansAndFollowingNum(user);
            }

            @Override
            public void onFail(Object Obj) {

            }
        });


        bmobSocialUtil.getUserInfoByID(userID);


    }


    public void checkIfWatched(final String userID) {

        if (userID.equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
            watchIcon.setVisibility(View.GONE);
        }

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Boolean isWatched = MuseumDB.getInstance(UserInfoActivity.this).getIfFollwed(userID);

                return isWatched;

            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                watchIcon.setSelected((Boolean) o);

            }
        };

        asyncTask.execute();


    }


    public void setFansAndFollowingNum(User user) {
        BmobUserRelation bmobUserRelation = BmobUserRelation.getInstance2(this);
        bmobUserRelation.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                int[] ints = (int[]) Obj;
                int fansNum = ints[0];
                int followingNum = ints[1];
                fans.setText(fansNum + "");
                following.setText(followingNum + "");

            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        bmobUserRelation.getFansAndFollowingsNum(user.getUserRelationID());


//        BmobUserRelation.getInstance2(this, new OnBmobReturnWithObj() {
//            @Override
//            public void onSuccess(Object Obj) {
//
//            }
//
//            @Override
//            public void onFail(Object Obj) {
//
//            }
//        }).


    }

    public void setUserInfo(User user) {

        String porTraitUrl = user.getPortraitURL();
        String nickName = user.getNickName();


        if (porTraitUrl != null) {
            Glide.with(this).load(porTraitUrl).transform(new GlideCircleTransform(this)).into(portrait);
        }
        if (nickName != null) {
            nicNameText.setText(nickName);
        }

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.back:

                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);
                break;


            case R.id.mine_follower_following:
                Intent intent2 = new Intent(this, SocialRelationActivity.class);

                intent2.putExtra(USER_RELATION_ID, user.getUserRelationID());
                startActivity(intent2);
                overridePendingTransition(R.anim.in_from_right, R.anim.none);

                break;

            case R.id.watch_icon:

                boolean isWatched = watchIcon.isSelected();
                watchStateChange(isWatched);

                break;

            default:
                break;

        }


    }

    public void watchStateChange(final boolean isWatched) {


        BmobUserRelation bmobUserRelation = BmobUserRelation.getInstance(this);
        bmobUserRelation.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                if (isWatched) {
                    watchIcon.setSelected(false);
                    ToastUtils.toast(UserInfoActivity.this, "已取消关注");
                } else {
                    watchIcon.setSelected(true);
                    ToastUtils.toast(UserInfoActivity.this, "关注成功");
                }
            }

            @Override
            public void onFail(Object Obj) {

            }
        });

        if (isWatched) {
            bmobUserRelation.unWatchUsers(BmobUser.getCurrentUser(User.class), user);

        } else {
            bmobUserRelation.watchUsers(BmobUser.getCurrentUser(User.class), user);

        }


    }


    //

    class BaseFragmentAdapter extends FragmentPagerAdapter {

        // 标题数组
        String[] titles = {
                "展·馆",
                "展·览",
                "展·品",
                "评·论"};

        public BaseFragmentAdapter(FragmentManager fm) {
            super(fm);
            if (mFragments.size() == 0) {
                mFragments.add(new MineFragmentF1());
                mFragments.add(new MineFragmentF2());
                mFragments.add(new MineFragmentF3());
                mFragments.add(new MineFragmentF4());
            }
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

}

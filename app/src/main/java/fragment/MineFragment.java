package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobRegisterAndLogin;
import bmobUtils.BmobSocialUtil;
import cn.bmob.v3.BmobUser;
import db.MuseumDB;
import model.User;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;
import jintong.museum2.SetUpActivity;
import jintong.museum2.SocialRelationActivity;
import myView.GlideCircleTransform;
import util.ToastUtils;

import static util.ParameterBase.USER_ID;
import static util.ParameterBase.USER_RELATION_ID;

/**
 * Created by wjc on 2017/2/9.
 */
public class MineFragment extends Fragment implements View.OnClickListener {


    private ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();

    private ImageView toSetUp,portrait;

    private View view;

    private int mSelectedFragment;

    private RelativeLayout followerAndFollowing;

    private TextView fans, following,nicNameText;
    private TabLayout mTabLayout;

    private BaseFragmentAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_fragment, container, false);


        initViews();
        initDatas();

        initEvents();


        return view;

    }
    private void initViews() {
        Log.e("mine", "initViews");
        mViewPager = (ViewPager) view.findViewById(R.id.mine_viewPager);


        portrait= (ImageView) view.findViewById(R.id.mine_portrait);
        nicNameText= (TextView) view.findViewById(R.id.mine_nick_name);
        toSetUp = (ImageView) view.findViewById(R.id.to_setup);
        followerAndFollowing = (RelativeLayout) view.findViewById(R.id.mine_follower_following);
        fans = (TextView) view.findViewById(R.id.mine_fans_num);
        following = (TextView) view.findViewById(R.id.mine_following_num);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);

    }


    private void initDatas() {







        if (mAdapter == null) {

            mAdapter = new BaseFragmentAdapter(getChildFragmentManager());
        }

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void initEvents() {

        toSetUp.setOnClickListener(this);
        followerAndFollowing.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    //从网络获取用户个人信息
    private void getUserInfo() {

        User user = BmobUser.getCurrentUser(User.class);

        if(user==null){
            return;
        }
        String porTraitUrl = user.getPortraitURL();
        String nickName = user.getNickName();

        Integer fansNum = MuseumDB.getInstance(getActivity()).loadFans().size();
        Integer followingNum=MuseumDB.getInstance(getActivity()).loadFollowings().size();

        if (porTraitUrl != null) {
            Glide.with(this).load(porTraitUrl) .transform(new GlideCircleTransform(getContext())).into(portrait);
        }
        if (nickName != null) {
            nicNameText.setText(nickName);
        }
        if(fansNum!=null){
            fans.setText(fansNum+"");
        }
        if(followingNum!=null){
            following.setText(followingNum+"");
        }
    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.to_setup:


                User currentUser = BmobRegisterAndLogin.chekIfLogin();
                if (currentUser == null) {

                    ToastUtils.toast(getActivity(), "用户尚未登录");
                    break;
                }
                Intent intent = new Intent(getActivity(), SetUpActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);
                break;


            case R.id.mine_follower_following:
                Intent intent2 = new Intent(getActivity(), SocialRelationActivity.class);
                User user=BmobUser.getCurrentUser(User.class);
                intent2.putExtra(USER_RELATION_ID,user.getUserRelationID());
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);

                break;
            default:
                break;

        }


    }


//
//    //设置粉丝和关注人数
//    public void setFansNum() {
//        BmobSocialUtil bmobSocialUtil = BmobSocialUtil.getInstance(getActivity());
//        bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
//            @Override
//            public void onSuccess(Object Obj) {
//                int num = (int) Obj;
//                following.setText(num + "");
//            }
//
//            @Override
//            public void onFail(Object Obj) {
//                int num2 = (int) Obj;
//                fans.setText(num2 + "");
//            }
//        });
//        bmobSocialUtil.getFansAndFollowingNum(BmobUser.getCurrentUser(User.class).getObjectId());
//
//
//    }



    class BaseFragmentAdapter extends FragmentPagerAdapter {

        // 标题数组
        String[] titles = {
                "展·馆",
                "展·览",
                "展·品",
                "交·流"};

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

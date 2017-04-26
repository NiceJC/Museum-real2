package BmobUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import entity.User;
import interfaces.OnBmobReturnSuccess;
import util.ToastUtils;

/**
 * Created by wjc on 2017/4/19.
 */

public class BmobUserInfo {

    private Context context;
    private String TAG;
    private static BmobUserInfo instance=null;
    private OnBmobReturnSuccess onBmobReturnSuccess;

    public void setOnBmobReturnSuccess(OnBmobReturnSuccess onBmobReturnSuccess) {
        this.onBmobReturnSuccess = onBmobReturnSuccess;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    onBmobReturnSuccess.onSuccess();
                    break;
                case 2:
                    onBmobReturnSuccess.onFail();
                    break;
                default:
                    break;

            }
        }
    };

    private BmobUserInfo(Context context){
        this.context=context;
        TAG="UserInfo";

    }
    //单例
    public static BmobUserInfo getInstance(Context context){

        if (instance==null){
            instance=new BmobUserInfo(context);

        }

        return instance;

    }

    //修改用户昵称
    public void setNickName(String nickName){

        User newUser=new User();
        newUser.setNickName(nickName);

        User user=BmobUser.getCurrentUser(User.class);

        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){

                    ToastUtils.toast(context,"更新用户信息成功");
                    handler.sendEmptyMessage(1);

                }else{
                    ToastUtils.toast(context,"更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }

    //设置年龄
    public void setAge(String age){

        User newUser=new User();
        newUser.setAge(age);

        User user=BmobUser.getCurrentUser(User.class);

        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){

                    ToastUtils.toast(context,"更新用户信息成功");
                    handler.sendEmptyMessage(1);
                }else{
                    ToastUtils.toast(context,"更新用户信息失败:" + e.getMessage());
                }
            }
        });


    }
    //设置性别
    public void setGender(Boolean isMan){
        User newUser=new User();
        newUser.setMan(isMan);

        User user=BmobUser.getCurrentUser(User.class);

        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){

                    ToastUtils.toast(context,"更新用户信息成功");
                    handler.sendEmptyMessage(1);
                }else{
                    ToastUtils.toast(context,"更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }

//    //设置绑定手机号
//    public void setPhoneNum(String phoneNum){
//        User newUser=new User();
//        newUser.setPhoneNum(phoneNum);
//
//        User user=BmobUser.getCurrentUser(User.class);
//
//        newUser.update(user.getObjectId(), new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    ToastUtils.toast(context,"更新用户信息成功");
//                }else{
//                    ToastUtils.toast(context,"更新用户信息失败:" + e.getMessage());
//                }
//            }
//        });
//
//    }

    //上传头像的照片，返回URL
    public void setPortrait(){



    }







}

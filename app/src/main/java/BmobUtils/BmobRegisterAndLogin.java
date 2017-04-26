package BmobUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import entity.User;
import interfaces.OnBmobReturnSuccess;
import util.ToastUtils;

/**
 * 调用Bmob的API注册与登录
 * Created by wjc on 2017/4/14.
 */

public class BmobRegisterAndLogin {


    private static BmobRegisterAndLogin instance=null;
    private String TAG ;
    private Context context;

    private OnBmobReturnSuccess onBmobReturnSuccess;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:


                    //返回成功，结果1

                    onBmobReturnSuccess.onSuccess();

                    break;
                case 2:

                    //返回成功，结果2
                    onBmobReturnSuccess.onFail();
                    break;

                default:
                    break;


            }
        }
    };


    public void setOnBmobReturnSuccess(OnBmobReturnSuccess onBmobReturnSuccess){
        this.onBmobReturnSuccess = onBmobReturnSuccess;
    }


    private BmobRegisterAndLogin(Context context) {

        this.TAG= "bmob registerAndLogin";
        this.context=context;

    }
    //单例
    public static BmobRegisterAndLogin getInstance(Context context){
        if(instance==null){
            instance=new BmobRegisterAndLogin(context);
        }
        return  instance;
    }


    //检测是否已经有本地用户,返回null表示当前没有用户
    public static User chekIfLogin() {
        User user = BmobUser.getCurrentUser(User.class);
        return user;

    }




    //通过本地缓存的用户 ，不经过UI交互直接登录
    public void loginBycurrentUser() {
        BmobUser currentUser = chekIfLogin();

        if (currentUser != null) {

        } else {
            Log.e("TAG", "本地用户为空");
        }
    }

    //注册用户,只需要用户名密码以及注册类型
    public void createUser(String userName, String passWord, Integer type) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(passWord);
        user.setRegisterType(type);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    ToastUtils.toast(context, "注册成功");

                } else {
                    e.printStackTrace();
                    ToastUtils.toast(context, e.toString());
                }
            }
        });
    }


    //查询手机号码是否已经注册
    public void checkIfNumExist(String phoneNum){
        BmobQuery<BmobUser> query=new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username",phoneNum);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> list, BmobException e) {

                if(e==null){
                    Log.d(TAG, "done: 查询用户成功");
                    if(list.size()==0){
                        //size为0，表示该号码未被注册
                        handler.sendEmptyMessage(1);

                    }else{
                        //size为1，表示已经被注册
                        handler.sendEmptyMessage(2);

                    }

                }else{
                    Log.d(TAG ,e.getMessage());
                }

            }
        });



    }



    //使用用户名+密码登录 用户名也可以是手机号
    public void loginByUserName(String userName, String passWord) {


        BmobUser.loginByAccount(userName, passWord, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null) {

                    Log.i(TAG, "登录成功");
                    ToastUtils.toast(context,"用户登录成功");

                    Message message=new Message();
                    message.what=1;
                    message.obj=user;
                    handler.sendMessage(message);

                }else{

                    ToastUtils.toast(context,"用户名或者密码错误");
                    Log.e(TAG, e.getMessage());

                }
            }
        });

    }




    //申请验证码  可以用于登录绑定 或者修改密码
    public void requestSMSCode(String num) {

        BmobSMS.requestSMSCode(num, "Museum", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    ToastUtils.toast(context, "验证码已发送");
                    Log.i(TAG, "验证码已发送");

                }else{
                    ToastUtils.toast(context,"失败:" + e.getMessage());

                }
            }
        });
    }

//    public void resetPassWord(String num,String code ,String newPass){
//        BmobUser.resetPasswordBySMSCode(code, newPass, new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    Log.i("smile", "密码重置成功");
//                    ToastUtils.toast(context,"密码重置成功,使用新密码登录吧");
//
//                }else{
//                    Log.i("smile", "重置失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
//                    ToastUtils.toast(context,"重置失败，"+e.getLocalizedMessage());
//
//
//                }
//            }
//        });
//
//
//    }

    //使用 手机号码以及验证码 一键登录或者注册
    public void signOrLoginByPhone(String num, String code) {

        BmobUser.signOrLoginByMobilePhone(num, code, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {

                if (user != null) {
                    ToastUtils.toast(context, "登录成功");
                    Log.i(TAG, "登录成功");

                }else {
                    ToastUtils.toast(context,"失败:" + e.getMessage());

                }
            }
        });
    }

    //使用手机号和验证码 一键注册并登录，包括提交用户自定义密码等
    public void signOrLoginByPhone(String num, String passWord, String code) {

        User user = new User();
        user.setMobilePhoneNumber(num);
        user.setPassword(passWord);
        user.signOrLogin(code, new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    ToastUtils.toast(context,"注册登录成功");
                    Log.i(TAG,"注册登录成功");
                    Message message=new Message();
                    message.what=1;
                    message.obj=user;
                    handler.sendMessage(message);


                }else{
                    ToastUtils.toast(context,"失败:" + e.getMessage());
                }

            }
        });


    }

    //通过短信重置密码（账号已经绑定手机号，并且先发送过验证码）
    public void resetPassBySMSCode(String code, String newPass){

        BmobUser.resetPasswordBySMSCode(code, newPass, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i(TAG, "密码重置成功");
                    ToastUtils.toast(context,"重置密码成功");
                    handler.sendEmptyMessage(1);

                }else{
                    Log.i(TAG, "重置失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage());
                    ToastUtils.toast(context,"密码重置失败"+e.getLocalizedMessage());

                }
            }
        });
    }




}
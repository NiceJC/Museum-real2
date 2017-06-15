package jintong.museum2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BmobUtils.BmobRegisterAndLogin;
import entity.User;
import interfaces.OnBmobReturnSuccess;
import interfaces.OnBmobReturnWithObj;
import util.MD5;
import util.ToastUtils;

//import cn.smssdk.EventHandler;
//import cn.smssdk.OnSendMessageHandler;
//import cn.smssdk.SMSSDK;

/**
 * 注册页面
 * 引入了Mob的短信接口
 * <p>
 * <p>
 * Created by wjc on 2017/3/1.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone;
    private EditText checkNum;
    private EditText passWord;

    private Button getCheckNum;
    private Button registerCommit;


    private android.os.Handler timerHandler = new android.os.Handler();
    private Runnable runnable;

    private int remainSeconds = 30; //计时剩余秒数

    private String phoneNum;

    private boolean checkIsPass; //短信验证是否已经通过

    //短信验证的返回信息接收器


    private int intentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        intentType = getIntent().getIntExtra("type", 1);

        initView();
        initData();
        initEvents();
    }


    private void initView() {

        phone = (EditText) findViewById(R.id.register_phone);
        checkNum = (EditText) findViewById(R.id.register_checkNum);
        passWord = (EditText) findViewById(R.id.register_passWord);
        getCheckNum = (Button) findViewById(R.id.get_check_num);
        registerCommit = (Button) findViewById(R.id.register_commit);

        if (intentType == 2) {
            registerCommit.setText("确认修改密码");
            passWord.setHint("输入新密码");
        }

    }


    private void initData() {

        runnable = new Runnable() {
            @Override
            public void run() {

                if (remainSeconds == 1) {
                    getCheckNum.setClickable(true);
                    getCheckNum.setBackgroundResource(R.drawable.blue_to_black);
                    getCheckNum.setText("获取验证码");
                    remainSeconds = 30;
                    return;
                }

                remainSeconds--;
                getCheckNum.setText(remainSeconds + "");
                timerHandler.postDelayed(this, 1000);

            }
        };


    }


    private void initEvents() {


        getCheckNum.setOnClickListener(this);
        registerCommit.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_check_num:
                /**
                 * 点击获取验证码之后，验证号码无误，发送返回验证码的请求，并且按钮变灰开始30s倒计时
                 *
                 *
                 */
                final String phoneNum = phone.getText().toString();


                Pattern p = Pattern.compile("1(3|5|7|8)[0-9]{9}");

                Matcher m = p.matcher(phoneNum);
                if (!m.matches()) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }


                /**
                 * 验证号码是否已经被注册
                 */
                BmobRegisterAndLogin.getInstance(this).setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                    @Override
                    public void onSuccess(Object Obj) {
                        afterClick(phoneNum);

                    }

                    @Override
                    public void onFail(Object Obj) {
                        ToastUtils.toast(RegisterActivity.this,"该号码已经被注册");

                    }



                });

                BmobRegisterAndLogin.getInstance(this).checkIfNumExist(phoneNum);





                break;
            case R.id.register_commit:
                final String phoneNum2 = phone.getText().toString();
                String verificationNum = checkNum.getText().toString();
                String passWordString = passWord.getText().toString();
                if (phoneNum2.equals("")) {
                    ToastUtils.toast(this, "手机号不能为空");
                } else if (verificationNum.equals("")) {
                    ToastUtils.toast(this, "请输入验证码");
                } else if (passWordString.equals("")) {
                    ToastUtils.toast(this, "请先设置密码");
                } else if (passWordString.length() < 8) {
                    ToastUtils.toast(this, "密码不能少于八位");
                } else {


                    try {
                        String newPass = MD5.getMD5(passWordString);

//                        BmobRegisterAndLogin.getInstance(this).clearFlag();


                        if (intentType == 2) {
                            /**
                             * 如果用户目的是修改密码 ，那么就提交新的密码以及验证码
                             * 查找标志位，验证是否重置成功
                             * 成功后跳转登录界面
                             *
                             */
                            BmobRegisterAndLogin.getInstance(this).setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                                @Override
                                public void onSuccess(Object Obj) {
                                    Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent2.putExtra("phoneNum",phoneNum2);

                                    startActivity(intent2);
                                    overridePendingTransition(R.anim.none, R.anim.out_to_right);

                                }

                                @Override
                                public void onFail(Object Obj) {

                                }



                            });


                            BmobRegisterAndLogin.getInstance(this).resetPassBySMSCode(verificationNum, newPass);


                            return;
                        }

                        /**
                         * 如果用户目的是注册账号，就提交用户名，密码以及验证码
                         * 注册并一并登录
                         * 如果返回结果正确 就跳转主界面
                         *
                         * 额  这个Bmob的signOrLogin方法是这样的，如果号码未被注册，就注册并设置密码，然后登录
                         *   如果已经被注册了，就修改为新密码，然后登录。。。
                         *
                         *
                         */


                        BmobRegisterAndLogin.getInstance(this).setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                            @Override
                            public void onSuccess(Object Obj) {
                                Intent intent2 = new Intent(RegisterActivity.this, MainActivity.class);


                                startActivity(intent2);
                                overridePendingTransition(R.anim.none, R.anim.out_to_right);

                            }

                            @Override
                            public void onFail(Object Obj) {

                            }


                        });




                        BmobRegisterAndLogin.getInstance(this).signOrLoginByPhone(phoneNum2, newPass, verificationNum);

                  } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }


                }


//                           finish();
//                overridePendingTransition(R.anim.none,R.anim.out_to_right);


                break;
            default:
                break;


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SMSSDK.unregisterAllEventHandler();

    }

    /**
     * 点击发送验证短信之后，按钮变灰，并开始倒计时
     */
    public void afterClick(String phoneNum) {

        getCheckNum.setClickable(false);
        getCheckNum.setBackgroundColor(Color.GRAY);
        timerHandler.postDelayed(runnable, 1000);

        BmobRegisterAndLogin.getInstance(this).requestSMSCode(phoneNum);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none, R.anim.out_to_right);

    }
}
package jintong.museum2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import javax.security.auth.login.LoginException;

import BmobUtils.BmobRegisterAndLogin;
import entity.User;
import interfaces.OnBmobReturnSuccess;
import interfaces.OnBmobReturnWithObj;
import util.MD5;
import util.ToastUtils;

/**
 * 登录页面
 * Created by wjc on 2017/3/1.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone;
    private EditText passWord;
    private Button toRegister;
    private Button login;
    private TextView forgetPass;
    private ImageView weChatLogin;
    private ImageView qqLogin;

    private String phoneNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        phoneNum=getIntent().getStringExtra("phoneNum");//如果是从重置密码页面跳转过来的，就获取号码，自动填写

        initView();
        initData();
        initEvents();
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.login_phone);

        //自动填写号码
        if(phoneNum!=null&&!phoneNum.equals("")){
            phone.setText(phoneNum);
        }

        passWord = (EditText) findViewById(R.id.login_passWord);
        toRegister = (Button) findViewById(R.id.login_to_register);
        login = (Button) findViewById(R.id.login);
        forgetPass = (TextView) findViewById(R.id.forget_passWord);
        weChatLogin = (ImageView) findViewById(R.id.login_weChat);
        qqLogin = (ImageView) findViewById(R.id.login_qq);


    }

    private void initData() {
    }

    private void initEvents() {
        toRegister.setOnClickListener(this);
        login.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
        weChatLogin.setOnClickListener(this);
        qqLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_to_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("type",1);//type为1 表示是正常的注册
                startActivity(intent);

                //切换动画需要放在startActivity或者finish的后面
                overridePendingTransition(R.anim.in_from_right, R.anim.none);

                break;
            case R.id.login:
                Log.e("******", "hahha");

                String phoneNum = phone.getText().toString();
                String passWordString = passWord.getText().toString();
                if (phoneNum.equals("")) {
                    ToastUtils.toast(this, "用户名不能为空");
                    return;
                } else if (passWordString.equals("")) {
                    ToastUtils.toast(this, "密码不能为空");
                    return;
                } else {

                    try {
                        String newPass= MD5.getMD5(passWordString);



                        BmobRegisterAndLogin.getInstance(this).setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                            @Override
                            public void onSuccess(Object Obj) {
                                //登录返回结果真确，登录成功并跳转主界面
                                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent2);
                                overridePendingTransition(R.anim.none,R.anim.out_to_right);

                            }

                            @Override
                            public void onFail(Object Obj) {

                            }


                        });

                        BmobRegisterAndLogin.getInstance(this).loginByUserName(phoneNum, newPass);



                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }






                }

                break;
            case R.id.forget_passWord:
                Intent intent1=new Intent(LoginActivity.this,RegisterActivity.class);
                intent1.putExtra("type",2);//type 2 表示要找回密码。。复用一下注册界面 哈哈哈
                startActivity(intent1);
                overridePendingTransition(R.anim.in_from_right, R.anim.none);


                break;
            case R.id.login_weChat:
                break;
            case R.id.login_qq:
                break;
            default:
                break;


        }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none,R.anim.out_to_right);


    }
}

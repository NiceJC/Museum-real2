package jintong.museum2;



import android.app.Application;

import cn.bmob.v3.Bmob;


/**
 * Created by wjc on 2017/4/10.
 */

public class MyApplication extends Application {


    //Bmob的初始化appID
    public static String APPID="5d3ed91c22b8a36cf7f36d35bed95428";


    //ShareSDK的appkey和APPSecret
//    public static String APPKEY="1af879d9c01a0";
//    public static String APPSECRET="bb05b679188bdebc1c83d32c0f1ca6d3";

    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.initialize(this,APPID);

//        SMSSDK.initSDK(this,APPKEY,APPSECRET);

    }
}

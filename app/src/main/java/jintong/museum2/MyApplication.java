package jintong.museum2;



import android.app.Application;

import cn.bmob.v3.Bmob;


/**
 * Created by wjc on 2017/4/10.
 */

public class MyApplication extends Application {


    //Bmob的初始化appID
    public static String APPID="0f1dcb2068225a70641d061d23e3c747";


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

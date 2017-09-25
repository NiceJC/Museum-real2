package jintong.museum2;



import android.app.Application;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.bmob.v3.Bmob;
import util.MyLocationListener;


/**
 * Created by wjc on 2017/4/10.
 */

public class MyApplication extends Application {


    //Bmob的初始化appID
    public static String APPID="443dccd52fafe0a53cc95cf53b28f12f";




    @Override
    public void onCreate() {
        super.onCreate();

        //Bmob初始化
        Bmob.initialize(this,APPID);



    }



}

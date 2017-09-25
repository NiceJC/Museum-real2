package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;

import java.util.List;

public class MyLocationListener implements BDLocationListener {
    private LocationClient mLocationClient;
    private  Context context;

    public MyLocationListener() {
    }

    public MyLocationListener(LocationClient mLocationClient ,Context context) {
        this.mLocationClient = mLocationClient;
        this.context=context;

    }

    @Override
    public void onReceiveLocation(BDLocation location) {


        Log.d("GPS",location.getLatitude()+" raw  "+location.getLongitude());
    /**
     *
     * 将经纬度保存
     */


        SharedPreferences preferences=context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("latitude",location.getLatitude()+"");
        editor.putString("longitude",location.getLongitude()+"");

        editor.commit();
        mLocationClient.stop();

    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
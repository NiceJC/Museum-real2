package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by wjc on 2017/7/4.
 */

public class DistanceUtil {


    /**
     * 从SharedPreferences中取出用户坐标数据并计算与目标博物馆的距离
     */
    public static double getCurrentLocal(Context context, BmobGeoPoint geoPoint) {

        if(geoPoint==null){
            return 0;
        }

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        double latitude = Double.valueOf(preferences.getString("latitude", "30.321432")).doubleValue();
        double longitude = Double.valueOf(preferences.getString("longitude", "120.197597")).doubleValue();

        BmobGeoPoint localGeoPoint = new BmobGeoPoint(longitude, latitude);
        double distance = localGeoPoint.distanceInKilometersTo(geoPoint);

        BigDecimal bg = new BigDecimal(distance).setScale(1, RoundingMode.UP);


        Log.d("GPS", latitude + "   " + longitude + "   " + bg.doubleValue());
        return bg.doubleValue();
    }
}

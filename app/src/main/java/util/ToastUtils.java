package util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import net.steamcrafted.loadtoast.LoadToast;

import jintong.museum2.MyApplication;
import jintong.museum2.R;

/**
 * Created by wjc on 2017/4/17.
 */

public class ToastUtils {

public static void toast(Context context,String text){
    Toast.makeText(context,text,Toast.LENGTH_SHORT).show();

}


    public static LoadToast getLoadingToast(Context context){
        int height = 0;
        Activity activity= (Activity) context;
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        height = display.getWidth();




        LoadToast loadToast=new LoadToast(context);
        loadToast.setTranslationY(height/2);

        loadToast.setBackgroundColor(Color.LTGRAY);

        loadToast.setText("发送中...");





        return  loadToast;
    }


}

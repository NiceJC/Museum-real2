package util;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import jintong.museum2.MyApplication;

/**
 * Created by wjc on 2017/4/17.
 */

public class ToastUtils {

public static void toast(Context context,String text){
    Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_SHORT).show();

}


}

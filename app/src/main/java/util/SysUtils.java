package util;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import model.Collection;

/**
 * 1、根据当前手机的屏幕密度，将Dp单位转为PX
 * <p>
 * 2、获取当前手机的屏幕宽度（单位当然是px）
 */
public class SysUtils {


    public static int DpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Activity activity) {
        int width = 0;
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        width = display.getWidth();

        return width;
    }




    //提取出摸某个藏品的所有图片的URL
    public static ArrayList<String> getURLs(Collection collection) {
        List<String> list = new ArrayList<String>();
        if (collection.getImage1() != null) {
            list.add(collection.getImage1().getFileUrl());
            if (collection.getImage2() != null) {
                list.add(collection.getImage2().getFileUrl());

                if (collection.getImage3() != null) {
                    list.add(collection.getImage3().getFileUrl());
                    if (collection.getImage4() != null) {
                        list.add(collection.getImage4().getFileUrl());
                        if (collection.getImage5() != null) {
                            list.add(collection.getImage5().getFileUrl());
                        }
                    }
                }
            }

        }


        return (ArrayList<String>) list;
    }


}

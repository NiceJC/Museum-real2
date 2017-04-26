package util;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 *
 * 1、根据当前手机的屏幕密度，将Dp单位转为PX
 *
 * 2、获取当前手机的屏幕宽度（单位当然是px）
 *
 *
 */
public class SysUtils {


	public static int DpToPx(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	  
	  @SuppressWarnings("deprecation")
	public static int getScreenWidth(Activity activity){
		  int width = 0;
		  WindowManager windowManager = activity.getWindowManager();    
          Display display = windowManager.getDefaultDisplay();    
          width=display.getWidth();
		  return width;
	  }
	
}

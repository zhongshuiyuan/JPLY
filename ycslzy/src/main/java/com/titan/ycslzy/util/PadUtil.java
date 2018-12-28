package com.titan.ycslzy.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by li on 2017/5/31.
 * 判断设备是否为大屏幕设备类
 */
public class PadUtil {

	/**
	 * 判断设备是否为大于6.0屏幕的设备
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean isPad(Context context) {

//		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
//				>= Configuration.SCREENLAYOUT_SIZE_LARGE) {
//			//平板
//			return true;
//		} else {
//			//手机
//			return false;
//		}


		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point point = new Point();
		display.getRealSize(point);
		Log.d("=================","the screen real size is "+point.toString());

		DisplayMetrics displayMetrics =context.getResources().getDisplayMetrics();
		int densityDpi =  displayMetrics.densityDpi;
		Log.d("=======densityDpi","the screen densityDpi is "+densityDpi);
		@SuppressWarnings("deprecation")
		float screenWidth = display.getWidth();
		@SuppressWarnings("deprecation")
		float screenHeight = display.getHeight();

		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;
		double x = Math.pow(dm.widthPixels / xdpi, 2);//1280 1812 1024 2048
		double y = Math.pow(dm.heightPixels / ydpi, 2);//800 1080 768  1536
		double screenInches = Math.sqrt(x + y);
		if (screenInches >= 6.0) {
			return true;
		}
		return false;
	}
}

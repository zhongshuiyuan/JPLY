package com.titan.ycslzy.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.titan.ycslzy.MyApplication;
import com.titan.ycslzy.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jsqlite.Callback;
import jsqlite.Database;

public class Util {


	/*设置button不可用并且颜色变成灰色*/
	public static void setBtnGray(Context context,Button btn){
		btn.setEnabled(false);
		int color = context.getResources().getColor(R.color.gray);
		btn.setBackgroundColor(color);
	}
	/*设置button可用并且颜色变成蓝色*/
	public static void setBtnBlue(Context context,Button btn){
		btn.setEnabled(true);
		Drawable color = context.getResources().getDrawable(R.drawable.background_view_rounded_blue,null);
		btn.setBackground(color);
	}

	/**根据县乡村的代码获取县乡村列表*/
	public static Map<String, String> getXXCDomain(Context context,String key,String upStr,String tbname){
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> values = new HashMap<String, String>();
		if(tbname.equals("xian")){
			values = getXianValue(context);
		}else if(tbname.equals("xiang")){
			map = getXiangValue(context);
			for(String str : map.keySet()){
				if(str.contains(upStr)){
					values.put(str,map.get(str));
				}
			}
		}else if(tbname.equals("cun")){
			map = getCunValue(context);
			for(String str : map.keySet()){
				if(str.contains(upStr)){
					values.put(str,map.get(str));
				}
			}
		}
		return values;
	}

	/**获取县乡村的代码值*/
	public static String getXXCValue(Context context,String key,String upStr,Map<String, String> map){

		String value = key;
		String str = map.get(key);
		if(str != null){
			value = str;
		}else{
			if(!key.contains(upStr)){
				String str1 = map.get(upStr+key);
				if(str1 != null){
					value = str1;
				}
			}
		}
		return value;
	}

	/**获取所有县名称及县代码*/
	public static Map<String,String> getXianValue(Context context) {
		final Map<String,String> map = new HashMap<String, String>();
		try {
			String databaseName = MyApplication.resourcesManager.getDataBase("db.sqlite");// "kaiyangxian.sqlite"
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from xian";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6
					map.put(data[5], data[4]);
					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**获取所有xiang名称xiang代码*/
	public static Map<String,String> getXiangValue(Context context) {
		final HashMap<String, String> map = new HashMap<String, String>();

		try {
			String databaseName = MyApplication.resourcesManager.getDataBase("db.sqlite");// "kaiyangxian.sqlite"
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from xiang";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6

					map.put(data[5]+data[9],data[10]);

					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**获取所有村名称及村代码*/
	public static Map<String,String> getCunValue(Context context) {
		final Map<String,String> map = new HashMap<String, String>();
		try {
			String databaseName = MyApplication.resourcesManager.getDataBase("db.sqlite");// "kaiyangxian.sqlite"
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from cun";
			db.exec(sql, new Callback() {

				@Override
				public void types(String[] arg0) {

				}

				@Override
				public boolean newrow(String[] data) {// 3 5 6
					map.put(data[3]+data[4]+data[5], data[6]);
					return false;
				}

				@Override
				public void columns(String[] arg0) {

				}
			});
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**复制文件到平板中*/
	public static void copyFile(Context context,String fileDir,String assetPath, String filename){
		try {
			InputStream db = context.getResources().getAssets().open(assetPath);
			FileOutputStream fos = new FileOutputStream(fileDir + "/"+ filename);
			byte[] buffer = new byte[8129];
			int count = 0;

			while ((count = db.read(buffer)) >= 0) {
				fos.write(buffer, 0, count);
			}

			fos.close();
			db.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**检查String字符串是否是纯数字*/
	public static boolean CheckStrIsDouble(String str){
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
//		Pattern pattern = Pattern.compile("^[+-//d.]");//"^[+-\d]"  "[0-9]{1,}"
//		Matcher matcher = pattern.matcher((CharSequence)str);
//		boolean result=matcher.matches();
//		return result;
	}

	/** 判断当前手机是否有ROOT权限 */
	public static boolean isRoot() throws Exception{
		boolean bool = false;

		if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
			bool = false;
		} else {
			bool = true;
		}
		Log.d("===============", "bool = " + bool);
		return bool;
	}

	/** 把TextView的光标放置在最后 */
	public static void setTextViewCursorLocation(TextView et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	/** 把edittext的光标放置在最后 */
	public static void setEditTextCursorLocation(EditText et) {
		CharSequence txt = et.getText();
		if (txt instanceof Spannable) {
			Spannable spanText = (Spannable) txt;
			Selection.setSelection(spanText, txt.length());
		}
	}

	public static int dp2px(Context mContext,int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				mContext.getResources().getDisplayMetrics());
	}

	/**

	 * 获取当前 jvm 的内存信息

	 *

	 * @return

	 */

	public static String toMemoryInfo() {

		Runtime currRuntime = Runtime.getRuntime ();

		int nFreeMemory = ( int ) (currRuntime.freeMemory() / 1024 / 1024);

		int nTotalMemory = ( int ) (currRuntime.totalMemory() / 1024 / 1024);

		return nFreeMemory + "M/" + nTotalMemory +"M(free/total)" ;

	}

	/** int颜色值 转化为rgb值并含有透明度 */
	public static int getColor(int color, int tmd) {
		int red = (color & 0xff0000) >> 16;
		int green = (color & 0x00ff00) >> 8;
		int blue = (color & 0x0000ff);
		int tt = tmd*255/100;
		return Color.argb(255-tt, red, green, blue);
	}


}

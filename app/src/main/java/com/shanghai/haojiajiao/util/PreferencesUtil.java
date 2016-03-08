package com.shanghai.haojiajiao.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.shanghai.haojiajiao.app.HaojiajiaoApplication;

/**
 * sharePreferences文件操作工具类
 * @author ahnw_01
 *
 */
 public class PreferencesUtil {
	 /**
	  * 向sharePreferences文件中放入String类型的数据
	  * @param context
	  * @param Key
	  * @param value
	  */
 	public static void putStringContent(String Key,String value) {
		SharedPreferences preferences = HaojiajiaoApplication.context.getSharedPreferences("homeworkcat",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(Key, value);
		editor.commit();
	}
 	
	/**
	 * 获取消息内容
	 * @param context
	 * @return
	 */
	public static String getStringByKey(Context context,String key) {

		SharedPreferences preferences = context.getSharedPreferences("homeworkcat",
				Context.MODE_PRIVATE);
		return preferences.getString(key, null);
	}

	public static SharedPreferences getShared(Context context) {

		SharedPreferences preferences = context.getSharedPreferences("homeworkcat",
				Context.MODE_PRIVATE);
		return preferences;
	}
}

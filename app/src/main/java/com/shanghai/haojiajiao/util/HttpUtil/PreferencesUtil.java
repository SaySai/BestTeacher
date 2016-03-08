package com.shanghai.haojiajiao.util.HttpUtil;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.shanghai.haojiajiao.app.HaojiajiaoApplication;


/**
 * sharePreferences文件操作工具类
 * @author homeworkcat
 *
 */
 public class PreferencesUtil {
	 /**
	  * 向sharePreferences文件中放入String类型的数据
	  */
 	public static void putStringContent( String key,String value) {
		SharedPreferences preferences = HaojiajiaoApplication.context.getSharedPreferences("homeworkcat",
				Context.MODE_WORLD_READABLE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
 	
	/**
	 * 获取消息内容
	 * @param context
	 * @return
	 */
	public static String getStringByKey(Context context,String key) {

		SharedPreferences preferences = context.getSharedPreferences("homeworkcat",
				Context.MODE_WORLD_READABLE);
		return preferences.getString(key, null);
	}

}

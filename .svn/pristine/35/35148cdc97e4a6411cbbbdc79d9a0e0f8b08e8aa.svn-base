package com.shanghai.haojiajiao.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.reflect.Field;

/**
 * 设备的工具类
 * 
 * @author ahnw_01
 *
 */
public class DeviceUtil {

	/**
	 * 判断wift是否可用
	 * @param context
	 * @return
	 */
	public static  boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取当前的版本号
	 */
	public static int getVersionCode(Context context) {
		// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
		int versionCode = 1;
		// 获取当前版本的包名，版本号
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					"com.simei.yfzx_2", 1).versionCode;
		} catch (NameNotFoundException e) {
			//
			e.printStackTrace();
		}

		return versionCode;
	}



	//获取通知栏高度
	public static int getStatusBarHeight(Context context){
	        Class<?> c = null;
	        Object obj = null;
	        Field field = null;
	        int x = 0, statusBarHeight = 0;
	        try {
	            c = Class.forName("com.android.internal.R$dimen");
	            obj = c.newInstance();
	            field = c.getField("status_bar_height");
	            x = Integer.parseInt(field.get(obj).toString());
	            statusBarHeight = context.getResources().getDimensionPixelSize(x);
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
	        return statusBarHeight;
	    }

}

package com.shanghai.haojiajiao.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

/**
 * app运行时，保存在内存中的缓存
 * 
 *
 * 
 */
public class RunCache {
	private static RunCache instance;
	private Map<String, Object> map;

	/**
	 * 非wifi环境下自动加载图片
	 */
	public final static String AUTO_LOAD = "auto_load";
	


	private RunCache() {

	}


	/**
	 * 获取缓存对象的实例
	 * 
	 * @return
	 */
	public static RunCache getInstance() {
		if (instance == null) {
			instance = new RunCache();
			instance.map = new HashMap<String, Object>();
		}

		return instance;

	}

	/**
	 * 将数据放入缓存中
	 * 
	 * @param key
	 * @param value
	 */
	public static void putCache(String key, Object value) {
		getInstance().map.put(key, value);

	}

	/**
	 * 将数据放入缓存中
	 * 
	 * @param key
	 * @param value
	 */
	public static void putCacheAndPreferences(String key, String value) {
		getInstance().map.put(key, value);
		PreferencesUtil.putStringContent(key, value);
	}

	/**
	 * 将数据从缓存中取出
	 * 
	 * @param key
	 */
	public static Object getForCache(String key) {
		return getInstance().map.get(key);
	}
}

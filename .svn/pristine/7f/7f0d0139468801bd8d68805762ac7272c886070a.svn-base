package com.shanghai.haojiajiao.util;

import java.util.Map;

/**
 * 实体类取值的工具类
 * 
 * @author ahnw_01
 * 
 */
public class NullUtil {

	/**
	 * 从map中取出int类型的值
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static int getIntValue(Map<String, Object> data, String key) {
		int value = -1;

		Object objValue = data.get(key);
		if (objValue == null) {
			return value;
		}

		try {
			value = Integer.parseInt((String) objValue);
		} catch (NumberFormatException e) {
			return value;
		}

		return value;

	}

	public static float getFloatValue(Map<String, Object> data, String key) {
		float value = -1;

		Object objValue = data.get(key);
		if (objValue == null) {
			return value;
		}

		try {
			value = Float.parseFloat((String) objValue);
		} catch (NumberFormatException e) {
			return value;
		}

		return value;

	}

	public static String getStringValue(Map<String, Object> data, String key) {
		String value = null;

		Object objValue = data.get(key);
		if (objValue == null) {
			return "";
		}

		value = (String) objValue;
		if ("null".equalsIgnoreCase(value)) {
			value ="";
		}
		return value;

	}
	
	
	
	public static boolean isNullOrEmpty(String key) {
		boolean value = true;
        if(key!=null&&(!key.equals("")))
        {
        	value=false;
        }
		return value;

	}
	
	
}

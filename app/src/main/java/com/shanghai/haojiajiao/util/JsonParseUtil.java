package com.shanghai.haojiajiao.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public class JsonParseUtil {
	
	/**
	 * 公用的解析json的方式，将json转换成集合的模式存放，最外层是一个map，
	 * 	里面由map和list嵌套组成，集合内部的格式与json数据本身的格式相同
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> parseJsonToCollect(String jsonStr) throws Exception{
  		Map<String, Object>	data = new HashMap<String, Object>();  
		JSONObject root=new JSONObject(jsonStr);  
		
		Iterator<String> iterator = root.keys();
		
		String key = null;
		String value = null; 
		JSONArray dataArray = null;
		while(iterator.hasNext()){
			key = iterator.next();
			try {
				dataArray = root.getJSONArray(key);
				if(dataArray != null){		//如果这个字段是一个list
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					for( int i = 0 ;i < dataArray.length(); i++ ){ 
						 list.add(parseJsonToCollect(dataArray.getString(i))); 
					}
					data.put(key, list);  
					
				}else{
					value = root.getString(key);			//将json键值对放入map中
					data.put(key, value);  
				}
			} catch (Exception e) {
				try {
					root.getJSONObject(key);
					data.put(key, parseJsonToCollect(root.getString(key)));
				} catch (Exception e1) {
					value = root.getString(key);	//将json键值对放入map中
					data.put(key, value);  
 				} 
  				
			} 
 
		}
		
		
		return data;
	} 
	
	
	
}

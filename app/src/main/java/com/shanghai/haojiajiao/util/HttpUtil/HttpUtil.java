package com.shanghai.haojiajiao.util.HttpUtil;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 网络请求的工具类，一般情况用volley框架，这个类中的方法适用于比较耗时的网络操作，如上传下载等
 * 
 * @author ahnw_01
 * 
 */
public class HttpUtil {

	private final static int CONNECTTIMEOUT = 5 * 1000; // 连接超时时间

	private final static int READTIMEOUT = 5 * 1000; // 读取超时时间

	public final static String POST = "POST";

	// http://www.ahnw.gov.cn/dataSrv/AppApi.asmx/Service?inputStr={%09%22action%22%3A%22userLogin%22%2C%09%22userid%22%3A%2211212321%22%2C%09%22device%22%3A%22android%22%2C%09%22data%22%3A{%22username%22%3A%22015-011%22%2C%22password%22%3A%228880253%22%2C%22sim%22%3A%2213485696949%22%2C%22imei%22%3A%22imei00001%22%09}}

	/**
	 * 封装统一的数据请求参数
	 * 
	 * @param action
	 * @param dataParams
	 * @return
	 */
	public static Map<String, String> combParams(String action,
			Map<String, String> dataParams) {
		JSONObject jsonObject = new JSONObject();
		JSONObject dataObject = new JSONObject();
		try {
			if (dataParams != null && dataParams.size() > 0) {
				Iterator<String> iter = dataParams.keySet().iterator();
				String key = null, value = null;
				while (iter.hasNext()) {

					key = iter.next();

					value = dataParams.get(key);
					dataObject.put(key, value);
				}
			}

			jsonObject.put("data", dataObject);
			// TODO 后面这里要改
			jsonObject.put("device", "android");
			// 等有用户的时候取消注释

			if (HaojiajiaoApplication.getUserInfo() != null) {
				jsonObject.put("userid", HaojiajiaoApplication.getUserInfo().userid);
			} else {
				jsonObject.put("userid", "");
			}
//			jsonObject.put("userid", "281");
			jsonObject.put("action", action);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("inputStr", jsonObject.toString());

		return params;
	}

	

	
	/*key为jsonarray 里面json名,key1为jsonarray 名 */
	public static Map<String, String> combParams(String key,String key1,String action,
			List<String> productids) {
		JSONObject jsonObject = new JSONObject();
		JSONObject obj = new JSONObject();
		try {
			JSONArray sa=new JSONArray();
			for(int i=0;i<productids.size();i++)
			{
				JSONObject dataObject = new JSONObject();
				dataObject.put(key, productids.get(i));
				sa.put(i,dataObject);
			}
			obj.accumulate(key1, sa);
			jsonObject.put("data", obj);
			// TODO 后面这里要改
			jsonObject.put("device", "android");
			// 等有用户的时候取消注释
			if (HaojiajiaoApplication.getUserInfo() != null) {
				jsonObject.put("userid", HaojiajiaoApplication.getUserInfo().userid);
			} else {
				jsonObject.put("userid", "");
			}
//			jsonObject.put("userid", "281");
			jsonObject.put("action", action);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("inputStr", jsonObject.toString());

		return params;
	}
	
	public static Map<String, String> combParams(String key,String key1,String action,
			List<String> productids,Map<String, String> dataParams ) {
		JSONObject jsonObject = new JSONObject();
		JSONObject obj = new JSONObject();
		try {
			JSONArray sa=new JSONArray();
			for(int i=0;i<productids.size();i++)
			{
				JSONObject dataObject = new JSONObject();
				dataObject.put(key, productids.get(i));
				sa.put(i,dataObject);
			}
			obj.accumulate(key1, sa);
			obj.put("type", dataParams.get("type"));
			jsonObject.put("data", obj);
			// TODO 后面这里要改
			jsonObject.put("device", "android");
			// 等有用户的时候取消注释
			if (HaojiajiaoApplication.getUserInfo() != null) {
				jsonObject.put("userid", HaojiajiaoApplication.getUserInfo().userid);
			} else {
				jsonObject.put("userid", "");
			}
			jsonObject.put("action", action);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("inputStr", jsonObject.toString());

		return params;
	}
	/**
	 * 封装统一的数据请求参数
	 * 
	 * @param action
	 * @param dataParams
	 * @return
	 */
	public static Map<String, String> combParamsForArray(String action,
			Map<String, Object> dataParams) {
		JSONObject jsonObject = new JSONObject();
		JSONObject dataObject = new JSONObject();
		try {
			if (dataParams != null && dataParams.size() > 0) {
				Iterator<String> iter = dataParams.keySet().iterator();
				String key = null;
				Object value = null;
				while (iter.hasNext()) {

					key = iter.next();

					value = dataParams.get(key);
					dataObject.put(key, value);
				}
			}

			jsonObject.put("data", dataObject);
			// TODO 后面这里要改
			jsonObject.put("device", "android");

			if(HaojiajiaoApplication.getUserInfo() != null){
				jsonObject.put("userid", HaojiajiaoApplication.getUserInfo().userid);

			
				if (HaojiajiaoApplication.getUserInfo() != null) {
					jsonObject.put("userid", HaojiajiaoApplication.getUserInfo().userid);

				} else {
					jsonObject.put("userid", "");
			  
			  }
			}else{
				jsonObject.put("userid", "");
			}
			
			if(HaojiajiaoApplication.IMEI!=null){
				jsonObject.put("imei", HaojiajiaoApplication.IMEI);
			}else{
				jsonObject.put("imei", "");
			} 

			  if(HaojiajiaoApplication.IMEI!=null){ jsonObject.put("imei",
					  HaojiajiaoApplication.IMEI); }else{ jsonObject.put("imei", ""); }

			jsonObject.put("action", action);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Map<String, String> params = new HashMap<String, String>();

		params.put("inputStr", jsonObject.toString());

		return params;
	}

	/**
	 * 获取指定路径的InputStream - 通用
	 * 
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	public static InputStream getUrlInputStream(String urlPath)
			throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(POST);
		connection.setConnectTimeout(CONNECTTIMEOUT);
		connection.setReadTimeout(READTIMEOUT);
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = connection.getInputStream();
			return inputStream;
		}

		return null;

	}

	/**
	 * 将网络数据存储到本地
	 * 
	 * @param urlPath
	 *            网络地址
	 * @param localPath
	 *            本地地址
	 * @throws Exception
	 */
	public static void downLoad(String urlPath, String localPath)
			throws Exception {
		FileUtil.writeStreamToLocal(getUrlInputStream(urlPath), localPath);
	}

	/**
	 * 将inputStream中的数据读取到byte数组中
	 * 
	 * @param inputStream
	 * @return
	 */
	public static byte[] readStream(InputStream inputStream) {
		int lenth = 1024 * 1000;
		byte[] buffer = new byte[lenth]; // 数据长度有可能不够
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = -1;
		try {
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				outputStream.flush();
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return outputStream.toByteArray();
	}

	/**
	 * 向服务器发送请求,从服务器获取数据
	 * 
	 * @param url
	 * @return
	 */
	public static String getDataFromServer(String url) {
		String data = null;
		try {
			InputStream inputStream = getUrlInputStream(url);
			data = new String(readStream(inputStream));
			if (data != null) {
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return data;
	}

/*	public static ArrayList getTeacherInfoForRongyun(final Context context, final String addr){
		ArrayList userInfo = new ArrayList();
		sendHttpRequest(addr,new HttpCallbackListener(){
			public void onFinish(final String response){
				userInfo=handleTeacherResponse(context,response);
			}
		});
		return userInfo;
	}*/

/*

	public static void sendHttpRequest(final String address,
									   final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						// 回调onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						// 回调onError()方法
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	public static ArrayList handleTeacherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			ArrayList List = new ArrayList();
			List.clear();
			String userName = jsonObject.optString("teacherUserName");
			List.add(userName);
			String teacherName = jsonObject.optString("teacherName");
			List.add(teacherName);
			String picUrl = jsonObject.optString("teacherPortrait");
			List.add(picUrl);
			return List;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public static ArrayList handleParentResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			ArrayList List = new ArrayList();
			List.clear();
			String userName = jsonObject.optString("parentUserName");
			List.add(userName);
			String teacherName = jsonObject.optString("parentName");
			List.add(teacherName);
			String picUrl = jsonObject.optString("parentPortrait");
			List.add(picUrl);
			return List;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
*/


	/**
	 * 向服务器发送请求获取教师信息：name,picUrl.从服务器获取数据
	 *
	 * @param url
	 * @return
	 */
/*
	public static String getTeacherDataFromServer(String userName) {
		String data = null;
		String url = null;
		url = GoodTeacherURL.getTeacherInfoByUserName+"?TeacherUserName="+userName;
		try {
			InputStream inputStream = getUrlInputStream(url);
			data = new String(readStream(inputStream));
			if (data != null) {
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return data;
	}
*/

	/**
	 * 以post的方式将参数发送到服务器上,接收返回数据
	 * 
	 * @param actionUrl
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String postParams(String actionUrl, Map<String, String> params)
			throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setConnectTimeout(CONNECTTIMEOUT);
		conn.setReadTimeout(READTIMEOUT);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);
		conn.setRequestMethod("POST"); // Post方式
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");

		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);
		if (params != null && params.size() > 0) {

			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}

			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(sb.toString().getBytes());

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			outStream.close();
		}
		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		byte[] data = readStream(in);
		String str = new String(data);

		conn.disconnect();
		return str;

	}

	// 上传代码，第一个参数，为要使用的URL，第二个参数，为表单内容，第三个参数为要上传的文件，可以上传多个文件，这根据需要页定
	public static String postParamsAndFiles(String actionUrl,
			Map<String, String> params, Map<String, File> files)
			throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

		conn.setConnectTimeout(CONNECTTIMEOUT);
		conn.setReadTimeout(READTIMEOUT);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);
		conn.setRequestMethod("POST"); // Post方式
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		System.setProperty("http.keepAlive", "false");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				// sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
				// + file.getKey() + "\"" + LINEND);
				sb1.append("Content-Disposition: form-data; name=\""
						+ file.getKey() + "\"; filename=\"" + file.getKey()
						+ "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) > -1) {
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}
		}
		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		String result = FileUtil.getStringByInputStream(in);
		conn.disconnect();
		return result;
	}

	/**
	 * 上传多个参数和一个文件
	 * 
	 * @param actionUrl
	 * @param params
	 * @param fileName
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String postParamsAndOneFile(String actionUrl,
			Map<String, String> params, String fileName, File file)
			throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

		conn.setConnectTimeout(CONNECTTIMEOUT);
		conn.setReadTimeout(READTIMEOUT);
		conn.setDoInput(true);// 允许输入

		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);
		conn.setRequestMethod("POST"); // Post方式
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		System.setProperty("http.keepAlive", "false");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (file != null) {

			StringBuilder sb1 = new StringBuilder();
			sb1.append(PREFIX);
			sb1.append(BOUNDARY);
			sb1.append(LINEND);
			sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
					+ fileName + "\"" + LINEND);
			sb1.append("Content-Type: application/octet-stream; charset="
					+ CHARSET + LINEND);
			sb1.append(LINEND);
			outStream.write(sb1.toString().getBytes());

			InputStream is = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}

			is.close();
			outStream.write(LINEND.getBytes());
		}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();

		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		StringBuilder sb2 = new StringBuilder();
		if (res == 200) {
			int ch;
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
		}
		outStream.close();
		conn.disconnect();
		return sb2.toString();
	}

	/**
	 * 判断该请求是否到了可以请求的时候
	 * 
	 * @param context
	 * @param tag
	 *            请求的tag
	 * @param timeOut
	 *            请求间隔时间
	 * @return
	 */
	public static boolean isTimeToRequest(Context context, String tag,
			long timeOut) {
		boolean isTimeToRequest = false;
		String timeMillStr = PreferencesUtil.getStringByKey(context, tag);
		if (timeMillStr == null || "".equals(timeMillStr.trim())) {
			isTimeToRequest = true;
		} else {
			long timeMill = 0;
			try {
				timeMill = Long.parseLong(timeMillStr);
			} catch (Exception e) {
				isTimeToRequest = true;
			}
			if (timeMill <= 0) {
				isTimeToRequest = true;
			} else {
				long currentTime = System.currentTimeMillis();
				if (currentTime - timeMill < timeOut) {
					isTimeToRequest = false;
				}
			}
		}
		return isTimeToRequest;

	}
}
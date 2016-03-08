package com.shanghai.haojiajiao.util.HttpUtil;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;

import java.util.HashMap;
import java.util.Map;


/**
 * 统一的网络请求处理类
 * 
 * @author ahnw_01
 * 
 */
public class RequestHandler {
	public RequestListener listener; // 回调监听对象
	public final static int SOCKET_TIMEOUT = 10 * 1000;

	/**
	 * 以post的方式发送请求，处理返回结果
	 * 
	 * @param url
	 * @param params
	 * @param requestCode
	 */
	public void sendHttpRequestWithParam(  String url,
			final Map<String, String> params, final RequestTag requestTag) {

		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {
					@Override
					public void onResponse(String responseData) {
						// 实例化网络请求的返回数据对象
						ResponseOwn response = new ResponseOwn(responseData);
						response.requestTag = requestTag;
						if (listener != null) {
							if (response.requestSuccess) {
								listener.onRequestSuccess(response);
							} else {
								listener.onRequestError(response);
							}
						} else {
							Log.w("RequestHandler", "有请求未处理!");

						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("wlqq", error.getMessage(), error);
						// 后面可以对这个statusCode 进行判断，404 或者500之类的，做具体的提示
						// int statusCode = error.networkResponse.statusCode;

						ResponseOwn response = new ResponseOwn();
						 //response.errorMessage = "网络请求错误，请稍后再试";//2015/1/29更改
						//response.errorMessage = error.getMessage();
						// System.out.println("-------------------  "+statusCode+" ++"
						// + error.getMessage());

						if (listener != null) {
							listener.onRequestError(response);
						} else {
							Log.w("RequestHandler", "有请求未处理!");

						}

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Charset", "UTF-8");
				headers.put("Connection", " Keep-Alive");
				return headers;
			}

		 

		};
		request.setRetryPolicy(new DefaultRetryPolicy(10000,
						DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// 发送网络请求
		HaojiajiaoApplication.getRequestQueue().add(request);
	}


	/**update用户信息时，post方法不成功
	 * 以get的方式发送请求，处理返回结果
	 *
	 * @param url
	 * @param params
	 * @param requestCode
	 */

	public void sendHttpRequestWithParamByGet(  String url,
										   final Map<String, String> params, final RequestTag requestTag) {

		StringRequest request = new StringRequest(Method.GET, url,
				new Listener<String>() {
					@Override
					public void onResponse(String responseData) {
						// 实例化网络请求的返回数据对象
						ResponseOwn response = new ResponseOwn(responseData);
						response.requestTag = requestTag;
						if (listener != null) {
							if (response.requestSuccess) {
								listener.onRequestSuccess(response);
							} else {
								listener.onRequestError(response);
							}
						} else {
							Log.w("RequestHandler", "有请求未处理!");

						}
					}
				}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("RequestHandler", error.getMessage(), error);
				// 后面可以对这个statusCode 进行判断，404 或者500之类的，做具体的提示
				// int statusCode = error.networkResponse.statusCode;

				ResponseOwn response = new ResponseOwn();
				//response.errorMessage = "网络请求错误，请稍后再试";//2015/1/29更改
				//response.errorMessage = error.getMessage();
				// System.out.println("-------------------  "+statusCode+" ++"
				// + error.getMessage());

				if (listener != null) {
					listener.onRequestError(response);
				} else {
					Log.w("RequestHandler", "有请求未处理!");

				}

			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Charset", "UTF-8");
				headers.put("Connection", " Keep-Alive");
				return headers;
			}



		};
		request.setRetryPolicy(new DefaultRetryPolicy(10000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// 发送网络请求
		HaojiajiaoApplication.getRequestQueue().add(request);
	}

}

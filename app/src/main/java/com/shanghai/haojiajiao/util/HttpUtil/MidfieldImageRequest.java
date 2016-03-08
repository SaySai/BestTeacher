package com.shanghai.haojiajiao.util.HttpUtil;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.shanghai.haojiajiao.util.ImgLoadListener;

/**
 * 自定义的volley
 * @author wqb
 *
 */
public class MidfieldImageRequest extends ImageRequest {

	public ImgLoadListener downListener;
	public MidfieldImageRequest(String url, Listener<Bitmap> listener,
			int maxWidth, int maxHeight, Config decodeConfig,
			ErrorListener errorListener) {
		super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
 	}
	public MidfieldImageRequest(String url, Listener<Bitmap> listener,
			int maxWidth, int maxHeight, Config decodeConfig,
			ErrorListener errorListener,ImgLoadListener  downListener) { 
		super(url, listener, maxWidth, maxHeight, decodeConfig, errorListener);
		this.downListener = downListener;
 	}



	public void xx(){
		
	}
}

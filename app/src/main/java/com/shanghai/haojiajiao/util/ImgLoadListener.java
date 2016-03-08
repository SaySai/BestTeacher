package com.shanghai.haojiajiao.util;

import com.shanghai.haojiajiao.util.HttpUtil.BitmapResponse;

/**
 *图片加载
 * @author ahnw_01
 *
 */
public interface ImgLoadListener {
	void afterDownLoad(BitmapResponse bitmapResponse);
	void onDownLoadError(BitmapResponse bitmapResponse);
}


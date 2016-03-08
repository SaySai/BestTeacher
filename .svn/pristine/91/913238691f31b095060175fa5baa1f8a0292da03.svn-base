package com.shanghai.haojiajiao.util;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.shanghai.haojiajiao.R;

public class UilUtil {
    public static DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片加载时的默认图片
                .showImageOnLoading(R.drawable.pic_loading)
                        // 设置图片URI为空时默认图片
                .showImageForEmptyUri(R.drawable.pic_nopicture)
                        // 设置图片加载失败的默认图片
                .showImageOnFail(R.drawable.pic_errpicture)
                        // 设置添加到内存缓存
                .cacheInMemory(true)
                        // 设置规模类型的解码图像
                .cacheOnDisk(true).imageScaleType(ImageScaleType.NONE)
                        //给图片加圆角
                .displayer(new RoundedBitmapDisplayer(3))//是否设置为圆角，弧度为多少
                        // 设置位图图像解码配置
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // 载入图片前稍做延时可以提高整体滑动的流畅度
                .delayBeforeLoading(100)
                .build();
        return options;
    }

    public static void setVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }
}

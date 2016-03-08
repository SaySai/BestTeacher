package com.shanghai.haojiajiao.util.CrashHandlerUtils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by junw2 on 2015/10/28.
 */
public class FileUtil {

    private static final String SDCARD_IMG_PATH = Environment.getExternalStorageDirectory().getPath() + "/ecomo/images/";
    private static final String CACHE_IMG_PATH = Environment.getDownloadCacheDirectory().getPath() + "/ecomo/images/";

    private static String IMAGE_PATH;

    public static String getImagePath() {
        if (TextUtils.isEmpty(IMAGE_PATH)) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                IMAGE_PATH = SDCARD_IMG_PATH;
            } else {
                IMAGE_PATH = CACHE_IMG_PATH;
            }
            File file=new File(IMAGE_PATH);
            if(!file.exists()){
                file.mkdirs();
            }
        }
        return IMAGE_PATH;
    }

    public static String getRandomPicNamePng() {
        return Calendar.getInstance().getTimeInMillis() + "_" + new Random().nextInt(100000) + ".png";
    }
    public static String getRandomPicNameJpg() {
        return Calendar.getInstance().getTimeInMillis() + "_" + new Random().nextInt(100000) + ".jpg";
    }

    public static InputStream getFileInputStream(String filePath) {
        try {
            return new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void writeBitmapFile(Bitmap bitmap, String filePath) {
        if (bitmap == null || TextUtils.isEmpty(filePath))
            return;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }

}

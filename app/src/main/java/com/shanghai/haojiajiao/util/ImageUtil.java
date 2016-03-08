package com.shanghai.haojiajiao.util;

import java.util.List;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.util.HttpUtil.BitmapResponse;
import com.shanghai.haojiajiao.util.HttpUtil.MidfieldImageRequest;

/**
 * 图片处理相关工具类
 *
 * @author yfzx_2
 */
public class ImageUtil {
    private static int img_w;
    private static int img_h;
    private static List<ImgLoadListener> listeners;


    private static ImgLoadListener getListener(){
        if(listeners == null || listeners.size() == 0){
            listeners = new ArrayList<ImgLoadListener>();
            return null;
        }

        return listeners.remove(0);

    }
    public static Bitmap getBitmapFromFile(String dst, int width, int height,
                                           int defalutSampleSize) {

        try {

            if (null != dst && !"".equals(dst)) {
                BitmapFactory.Options opts = null;
                opts = new BitmapFactory.Options();
                if (width > 0 && height > 0) {
                    opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(dst, opts);
                    final int minSideLength = Math.min(width, height);
                    opts.inSampleSize = computeSampleSize(opts, minSideLength,
                            width * height);
                    opts.inJustDecodeBounds = false;
                    opts.inInputShareable = true;
                    opts.inPurgeable = true;
                } else {
                    opts.inJustDecodeBounds = false;
                    opts.inInputShareable = true;
                    opts.inPurgeable = true;
                    opts.inSampleSize = defalutSampleSize;

                }

                FileInputStream inputStream = new FileInputStream(dst);
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(
                        inputStream.getFD(), null, opts);
                inputStream.close();
                // Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
                // opts);
                // inputStream.close();
                // Bitmap bitmap = BitmapFactory.decodeFile(dst, opts);
                int angle = getExifOrientation(dst);
                if (angle != 0) { // 濡傛灉鐓х墖鍑虹幇浜� 鏃嬭浆 閭ｄ箞 灏辨洿鏀规棆杞害鏁�
                    Matrix matrix = new Matrix();
                    matrix.postRotate(angle);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix,
                            false);

                }
                return bitmap;
            }
        } catch (Exception e) {
            return null;
        }
        return null;

    }

    /**
     * 浠庡唴瀛樹腑鑾峰彇鍥剧墖
     *
     * @param picName
     * @param options
     */
    public static Bitmap getBitmapFormCache(String picName, ImgOption options) {
        Bitmap bitmap = null;
        bitmap = HaojiajiaoApplication.getBitmapFromMemoryCache(picName); // 浠庝竴绾х紦瀛樹腑鍙栧嚭bitamp瀵硅薄
        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = getBitmapFromFile(
                    Environment.getExternalStorageDirectory() + "/"
                            + HaojiajiaoApplication.IMG_PATH
                            + StringUtil
                            .getUrlExchange(picName),
                    options.img_w, options.img_h, options.sampleSize);
        }
        if (bitmap == null || bitmap.isRecycled()) {            //濡傛灉鍙屽眰鍐呭瓨涓兘鍙栦笉鍒板��
            if (options.isReTurnDefault) {
                //鏄剧ず榛樿鍥剧墖
                bitmap = HaojiajiaoApplication.getDefaultBitmap();
            } else {

                return null;
            }

        }

        if (options.isMatrix) {        //濡傛灉瑕佺缉鏀�

            bitmap = doodleBitmap(bitmap, options.img_w, options.img_h);

        }
        //鍒ゆ柇璇ュ浘鐗囨槸鍚﹀凡缁忓湪鍐呭瓨涓紝濡傛灉涓嶅湪鍐呭瓨涓垯灏嗗叾鍔犲叆鍐呭瓨涓紝杩欐牱渚夸簬鑾峰彇銆�
        HaojiajiaoApplication.addBitmapToMemoryCache(picName, bitmap);
        return bitmap;
    }

    /**
     * 缁欏師鍥炬坊鍔犺儗鏅紝骞跺皢鍘熷浘灞呬腑鏄剧ず
     *
     * @param bitmap
     * @param bgWidth
     * @param bgHeight
     * @return
     */
    public static Bitmap doodleBitmap(Bitmap bitmap, float bgWidth,
                                      float bgHeight) {

        if (bgWidth == 0 || bgHeight == 0 || bitmap == null) {
            return bitmap;
        }

        // 鑾峰彇杩欎釜鍥剧墖鐨勫鍜岄珮

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

		/*
         * if(width< newWidth && height < newHeight){ return bgimage; }
		 */

        float scale = bgWidth / width;
        if (width >= height) { // 瀹藉ぇ浜庨珮锛岄偅涔堟柊鍥剧殑瀹藉害璺熻儗鏅浘鐨勫鐩稿悓
            scale = bgWidth / width;

        } else {
            scale = bgHeight / height;
        }

        // 鍒涘缓鎿嶄綔鍥剧墖鐢ㄧ殑matrix瀵硅薄
        Matrix matrix = new Matrix();

		/*
		 * // 璁＄畻缂╂斁鐜囷紝鏂板昂瀵搁櫎鍘熷灏哄 float scaleWidth = ((float) newWidth) / width;
		 * float scaleHeight = ((float) newHeight) / height;
		 */
        // 缂╂斁鍥剧墖鍔ㄤ綔
        matrix.postScale(scale, scale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }
    /**
     * 涓嬭浇鍥剧墖
     * @param picName
     * @param view
     * @param options
     * @param listener
     */
    public static void downloadBitmapByOptions(final String picName,
                                               final View view, final ImgOption options, ImgLoadListener listener) {
        if(!HaojiajiaoApplication.isWifi && !HaojiajiaoApplication.autoLoad){			//濡傛灉鏄潪wifi鐜锛屽垏闈炶嚜鍔ㄥ姞杞斤紝閭ｄ箞灏变笉鍘讳笅杞藉浘鐗�

            return;
        }
        if(listeners == null){
            listeners = new ArrayList<ImgLoadListener>();
        }
        listeners.add(listener);

        MidfieldImageRequest imageRequest = new MidfieldImageRequest(picName,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {

                        if (response == null) {
                            return;
                        }
                        ImgLoadListener downListener = null;
                        if ((downListener = getListener()) != null) {
                            if(options.isMatrix){
                                response = doodleBitmap(response, options.img_w, options.img_h);
                            }
                            BitmapResponse bitmapResponse = new BitmapResponse(
                                    response, picName, view);
                            downListener.afterDownLoad(bitmapResponse);
                        }
                        // 鍐呭瓨缂撳瓨
                        if (options.memoryCache) {
                            HaojiajiaoApplication.addBitmapToMemoryCache(picName,
                                    response); // 鎶奲itmap瀵硅薄鏀惧埌涓�绾х紦瀛樹腑
                        }
                        // 纭洏缂撳瓨
                        if (options.diskCacheAble) {
                            // 鎶奲itmap瀛樻斁鍒版湰鍦扮紦瀛樼洰褰曚腑
                            FileUtil.saveBitmapToLocal(
                                    response,
                                    Environment
                                            .getExternalStorageDirectory()
                                            + "/"
                                            + HaojiajiaoApplication.IMG_PATH
                                            + StringUtil
                                            .getUrlExchange(picName));
                        }
                    }
                }, img_w, img_h, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { // 璇锋眰鍑洪敊鐨勬儏鍐碉紝涓嶉渶瑕佸仛澶勭悊锛屽洜涓轰箣鍓嶅凡缁忔樉绀洪粯璁ゅ浘鐗囦簡

                ImgLoadListener downListener = null;
                if ((downListener = getListener()) != null) {
                    downListener.onDownLoadError(new BitmapResponse(null, picName, view));

                }

            }
        },listener);
        HaojiajiaoApplication.getRequestQueue().add(imageRequest); // 灏嗗浘鐗囦笅杞借姹傚璞℃斁鍒拌姹傞槦鍒椾腑鍘�
    }


    private static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e("test", "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static String getBase64StrFromImageFileBySize(String imgPath,
                                                         int width, int height) {
        ByteArrayOutputStream baos = null;
        String result = "";

        try {
            Bitmap bitmap = getBitmapFromFile(imgPath, width, height, 1);

            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;

    }
}

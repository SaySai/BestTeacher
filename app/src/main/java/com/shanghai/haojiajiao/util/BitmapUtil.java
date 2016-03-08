package com.shanghai.haojiajiao.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jun.w on 2015/8/26.
 */
public class BitmapUtil {

    public static final int CAMERA_WITH_DATA = 3023;
    public static String path;
    public static final int UPLOAD_IMAGE = 1;
    public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");

    public static Bitmap decodeBitmap(Bitmap bitmap, int displayWidth, int displayHeight) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        int wRatio = (int) Math.ceil(bitmap.getWidth() / (float) displayWidth);
        int hRatio = (int) Math.ceil(bitmap.getHeight() / (float) displayHeight);
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        return Bitmap.createScaledBitmap(bitmap, displayWidth, displayHeight, true);
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static Bitmap convertViewToBitmap(View view) {
        // view.measure(View.MeasureSpec.makeMeasureSpec(0,
        // View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0,
        // View.MeasureSpec.UNSPECIFIED));
        // view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return Bitmap.createBitmap(view.getDrawingCache());
    }


    public static void doTakePhoto(Activity activity) {
        try {
            path = com.shanghai.haojiajiao.util.CrashHandlerUtils.FileUtil.getImagePath() + com.shanghai.haojiajiao.util.CrashHandlerUtils.FileUtil.getRandomPicNamePng();
            final Intent intent = getTakePickIntent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
            activity.startActivityForResult(intent, CAMERA_WITH_DATA);
//            activity.startActivityForResult(intent, UPLOAD_IMAGE);

        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "Get photo error", Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap decodeUriAsBitmap(Activity activity, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;

    }

    public static Bitmap returnBitMap(Context context, String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = getImageFromAssetsFile(context,"pic_user");
        }
        return bitmap;
    }

    private static Bitmap getImageFromAssetsFile(Context context,String fileName)
    {
        Bitmap image = null;
        AssetManager am = context.getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;

    }

    public static void onLoadImage( final Context context, final URL bitmapUrl, final OnLoadImageListener onLoadImageListener){
        final Handler handler = new Handler(){
            public void handleMessage(Message msg){
                onLoadImageListener.OnLoadImage((Bitmap) msg.obj, null);
            }
        };
        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                URL imageUrl = bitmapUrl;
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    InputStream inputStream = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Bitmap bitmap = getImageFromAssetsFile(context,"pic_user");
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            }

        }).start();

    }
    public interface OnLoadImageListener{
        public void OnLoadImage(Bitmap bitmap,String bitmapPath);
    }

    public  boolean checkURL(String url) {
        boolean value = false;
//        new Thread(new Runnable(){
//
//        }).start();
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            int code = conn.getResponseCode();
            if (code != 200) {
                value = false;
            } else {
                value = true;
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }


    public static Intent getTakePickIntent(String action) {
        Intent intent = new Intent();
        intent.putExtra("return-data", true);
        intent.setAction(action);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    public static void doCropPhoto(Intent data, Activity activity) {
        Uri currImageURI;
        if (data != null) {
            if (data.getExtras() != null) {
                try {
                    File file = new File(saveBitmapToFile(getBitmap(data)));
                    currImageURI = Uri.fromFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "没数据", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                currImageURI = data.getData();
            }
        } else {
            currImageURI = Uri.fromFile(new File(path));
        }
        // String realPath = getRealPathFromURI(currImageURI);
        try {
            final Intent intent = getCropImageIntent(currImageURI);
            activity.startActivityForResult(intent, UPLOAD_IMAGE);
        } catch (Exception e) {
            Toast.makeText(activity, "没数据", Toast.LENGTH_LONG).show();
        }
    }

    public static Intent getCropImageIntent(Uri photoUri) {
//        Intent intent = new Intent(
//                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        return intent;
    }

    //get bitmap after take photo
    private static Bitmap getBitmap(Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        return bitmap;
    }

    public static void doPickPhotoFromGallery(Activity activity) {
        try {
            final Intent intent = getPhotoPickIntent();
            activity.startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "没数据", Toast.LENGTH_LONG).show();
        }
    }

    private static Intent getPhotoPickIntent() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 0);
//        intent.putExtra("aspectY", 0);
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
//        intent.putExtra("return-data", true);
        return intent;
    }

    public static String saveBitmapToFile(Bitmap bitmap) throws IOException {
        BufferedOutputStream os = null;
        String _file = com.shanghai.haojiajiao.util.CrashHandlerUtils.FileUtil.getImagePath() + "/crop_photo.png";
        try {
            File file = new File(_file);
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.fillInStackTrace();
                }
            }
        }
        return _file;
    }


    public static String saveBitmapToFileForCompress(Bitmap bitmap) throws IOException {
        BufferedOutputStream os = null;
        String _file = com.shanghai.haojiajiao.util.CrashHandlerUtils.FileUtil.getImagePath() + "/crop_photo.jpg";
        try {
            File file = new File(_file);
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.fillInStackTrace();
                }
            }
        }
        return _file;
    }



    public static Bitmap getWindowBitmap(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        return Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
    }

    public static byte[] convertBitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap readBitMap(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }
}

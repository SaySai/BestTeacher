package com.shanghai.haojiajiao.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.shanghai.haojiajiao.app.HaojiajiaoApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * 文件操作工具类
 * 
 * @author wqb
 * 
 */
public class FileUtil {

	/**
	 * 建立HTTP请求，并获取Bitmap对象。
	 * 
	 * @param imageUrl
	 *            图片的URL地址
	 * @return 解析后的Bitmap对象
	 */
	public static Bitmap downloadBitmap(String imageUrl) {
		// String urlPath = URLConfig.PICTURE_URL + imageUrl; // 拼接下载图片地址
		String urlPath = imageUrl; // 拼接下载图片地址

		Bitmap bitmap = null;
		HttpURLConnection con = null;
		FileOutputStream bitmapWtriter = null;
		try {
			URL url = new URL(urlPath);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(10 * 1000);
			con.setReadTimeout(20 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			bitmap = BitmapFactory.decodeStream(con.getInputStream());
			File bitmapFile = new File(
					Environment.getExternalStorageDirectory() + "/"
							+ HaojiajiaoApplication.IMG_PATH
							+ StringUtil.getImgNameFormUrl(imageUrl));
			bitmapWtriter = new FileOutputStream(bitmapFile);
			if (bitmap.compress(CompressFormat.JPEG, 100, bitmapWtriter)) { // 100是压缩最大的值,表示原图保存
				System.out.println("存储原图成功");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bitmapWtriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (con != null) {
				con.disconnect();
			}
		}
		return bitmap;
	}

	/**
	 * 获取文件夹的大小
	 * 
	 * @param folder
	 * @return
	 * @throws Exception
	 */
	public static long getFolderSize(File folder) throws Exception {
		if (!folder.exists()) {
			return 0;
		}
		long size = 0;
		File flist[] = folder.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFolderSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 将bitmap对象保存到本地
	 * 
	 * @param bitmap
	 * @param localPath
	 */
	public static void saveBitmapToLocal(Bitmap bitmap, String localPath) {
		if (bitmap == null || bitmap.isRecycled()) {
			return;
		}
		File bitmapFile = new File(localPath);
		FileOutputStream bitmapWtriter = null;

		try {
			bitmapWtriter = new FileOutputStream(bitmapFile);
			if (bitmap.compress(CompressFormat.JPEG, 50, bitmapWtriter)) { // 100是压缩最大的值,表示原图保存
				System.out.println("存储原图成功");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bitmapWtriter.flush();
				bitmapWtriter.close();
			} catch (Exception e) {
			}

		}

	}
	
	/**
	 * 将bitmap对象保存到本地
	 * 
	 * @param bitmap
	 * @param localPath
	 */
	public static void saveBitmapToLocal(Bitmap bitmap, String localPath,int scoos) {
		if (bitmap == null || bitmap.isRecycled()) {
			return;
		}
		File bitmapFile = new File(localPath);
		FileOutputStream bitmapWtriter = null;

		try {
			bitmapWtriter = new FileOutputStream(bitmapFile);
			if (bitmap.compress(CompressFormat.JPEG, scoos, bitmapWtriter)) { // 100是压缩最大的值,表示原图保存
				System.out.println("存储原图成功");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bitmapWtriter.flush();
				bitmapWtriter.close();
			} catch (Exception e) {
			}

		}

	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 * @param isDelFolder
	 *            是否删除文件夹
	 */
	public static void delFolder(String folderPath, boolean isDelFolder) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (isDelFolder) {
				myFilePath.delete(); // 删除空文件夹
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i], true);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 讲String写入本地文件中
	 * 
	 * @param str
	 * @param path
	 */
	public static void writeStringToLocal(String str, String path) {

		File file = new File(path);
		OutputStream outputStream = null;
		ByteArrayInputStream inputStream = null;
		byte[] data = str.getBytes();
		try {
			file.createNewFile();
			outputStream = new FileOutputStream(file);

			inputStream = new ByteArrayInputStream(data);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
		} catch (Exception e) {

		} finally {
			try {
				outputStream.flush();
				outputStream.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 讲String写入本地文件中
	 * 
	 */
	public static String readStringFromLocalFile(File file) {
		String str = null;
		if (file == null || !file.exists()) {
			return str;
		}

		ByteArrayOutputStream outputStream = null;
		InputStream inputStream = null;

		try {
			file.createNewFile();
			outputStream = new ByteArrayOutputStream();

			inputStream = new FileInputStream(file);
			byte[] buffer = new byte[1024 * 1024];
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			str = outputStream.toString();

		} catch (Exception e) {

		} finally {
			try {
				outputStream.flush();
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;

	}

	/**
	 * 将流文件写入本地
	 * @param inputStream
	 * @param path
	 */
	public static void writeStreamToLocal(InputStream inputStream, String path) {

		File file = new File(path);
		OutputStream outputStream = null;
		try {
			file.createNewFile();
			outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
		} catch (Exception e) {

		} finally {
			try {
				outputStream.flush();
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * 删除文件
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // 删除;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					FileUtil.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		}
	}
	/**
	 * 检查本地是否存在该视频文件
	 * @param path
	 * @return
	 */
	public static boolean checkVideoFromLocation(String path){
		File file = new File(path);
		return file.exists();
	}
	
	
	/**
	 * 将流文件写入本地
	 * @param inputStream
	 * @param path
	 */
	public static String getStringByInputStream(InputStream inputStream) {
		String str = null;
		ByteArrayOutputStream outputStream = null;
		try {
 			outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			str = new String(outputStream.toByteArray());
		} catch (Exception e) {
			System.out.println(e);

			System.out.println(e);
		} finally {
			try {
				outputStream.flush();
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;

	}
}

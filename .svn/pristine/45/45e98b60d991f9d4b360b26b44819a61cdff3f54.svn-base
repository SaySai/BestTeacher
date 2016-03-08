package com.shanghai.haojiajiao.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;

/**
 * 用于在sharedpreference中存取对象
 * 
 * @author cyj
 * 
 */
public class ObjectHandlerUtil {
	public static String SceneList2String(HashMap<String, Object> hashmap)
			throws IOException {
		// 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// 然后将得到的字符数据装载到ObjectOutputStream
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
		objectOutputStream.writeObject(hashmap);
		// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
		String SceneListString = new String(Base64.encode(
				byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
		// 关闭objectOutputStream
		objectOutputStream.close();
		return SceneListString;
	}

	public static HashMap<String, Object> String2SceneList(
			String SceneListString) throws
			IOException, ClassNotFoundException {
		byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
				Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				mobileBytes);
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		HashMap<String, Object> SceneList = (HashMap<String, Object>) objectInputStream
				.readObject();
		objectInputStream.close();
		return SceneList;
	}

	public static boolean putHashMap(Context context, String key,
			HashMap<String, Object> hashmap) {
		SharedPreferences settings = context.getSharedPreferences("homeworkcat",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		try {
			if (hashmap == null) {
				editor.putString(key, null);
			} else {
				String liststr = SceneList2String(hashmap);
				editor.putString(key, liststr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return editor.commit();
	}

	public static HashMap<String, Object> getHashMap(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences("homeworkcat",
				Context.MODE_PRIVATE);
		String liststr = settings.getString(key, "");
		try {
			return String2SceneList(liststr);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

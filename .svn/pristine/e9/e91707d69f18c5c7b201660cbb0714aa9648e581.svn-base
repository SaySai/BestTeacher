package com.shanghai.haojiajiao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.util.Base64;

public class FileBase64Code {
	/**
	* encodeBase64File:(将文件转成base64 字符串). <br/>
	* @author guhaizhou@126.com
	* @param path 文件路径
	* @return
	* @throws Exception
	* @since JDK 1.6
	*/
	public static String encodeBase64File(String path) throws Exception {
		File  file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int)file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer,Base64.DEFAULT);
	}
	public static void decoderBase64File(String base64Code,String savePath) throws Exception {
		//byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
		byte[] buffer =Base64.decode(base64Code, Base64.DEFAULT);
		FileOutputStream out = new FileOutputStream(savePath);
		out.write(buffer);
		out.close();

	}
}

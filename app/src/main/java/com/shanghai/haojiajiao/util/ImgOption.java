package com.shanghai.haojiajiao.util;
/**
 * 下载图片选项
 * @author ahnw
 *
 */
public class ImgOption {
	public int img_w =0;		//长
	public int img_h =0;		//高
	public boolean diskCacheAble = true;	//硬盘缓存 默认选中
	public boolean memoryCache  = true;		//内存缓存 默认选中
	public boolean isReTurnDefault = false;		//是否返回默认图片
	public boolean isMatrix = true;		//是否缩放图片
	public int sampleSize = 2;
	
	public ImgOption setImg_W(int width){
		this.img_w = width;
		return this;
	}
	
	
	
	public ImgOption setImg_H(int Height){
		this.img_h = Height;
		return this;
	}
	
	
	
	public ImgOption setDiskCacheAble(boolean diskCacheAble){
		this.diskCacheAble = diskCacheAble;
		return this;
	}
	
	public ImgOption setMemoryCache(boolean memoryCache){
		this.memoryCache = memoryCache;
		return this;
	}
	
	
	public ImgOption setIsReTurnDefault(boolean isReTurnDefault){
		this.isReTurnDefault = isReTurnDefault;
		return this;
	}
	
	
	
	public ImgOption setIsMatrix(boolean isMatrix){
		this.isMatrix = isMatrix;
		return this;
	}
 	
	
	public ImgOption setSampleSize(int sampleSize){
		this.sampleSize = sampleSize;
		return this;
		
	}
	
}

package com.shanghai.haojiajiao.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> listData;

    public ViewPagerAdapter(List<View> listData) {
        super();
        this.listData = listData;
    }

    /**
     * 获得数据长度
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (null!=listData || listData.size() > 0) {
            return listData.size();
        }
        return 0;
    }
/*
	*//**
     * 初始化position位置的界面
     */
    @Override
    public Object instantiateItem(View view, int position) {

        ((ViewPager) view).addView(listData.get(position));

        return listData.get(position);
    }

    /**
     * 判断是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    /**
     * 销毁position位置的界面
     */
    @Override
    public void destroyItem(View view, int position, Object arg2) {
        ((ViewPager) view).removeView(listData.get(position));
    }
}

/**
 * 文件名：AdapterBase.java
 * 版本信息：
 * 日期：2013-6-27
 */

package com.shanghai.haojiajiao.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * 项目名称：Wisdom4S 类名称：AdapterBase 类描述：适配器 基类 创建人：杨琳 创建时间：2013-6-27 下午4:06:07
 * 修改人：杨琳 修改时间：2013-6-27 下午4:06:07 修改备注：
 */
public abstract class AdapterBase<T> extends BaseAdapter {
    public ArrayList<T> list = new ArrayList<T>();
    private Activity act;

    public ArrayList<T> getList() {
        return list;
    }

    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public AdapterBase(Activity act) {
        this.act = act;
        inflater = LayoutInflater.from(act);
    }

    public Activity getAct() {
        return act;
    }

    public void setData(ArrayList<T> list) {
        if (list == null) {
            return;
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void clearPositon(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public void setT(int position, T t) {
        this.list.set(position, t);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return super.getViewTypeCount();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public void setList(ArrayList<T> list) {
        this.list.addAll(list);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return getExView(position, convertView, parent, inflater);
    }

    protected abstract View getExView(int position, View convertView,
                                      ViewGroup parent, LayoutInflater inflater);
}

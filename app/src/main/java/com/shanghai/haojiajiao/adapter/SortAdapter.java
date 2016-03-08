package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.Course2Model;
import com.shanghai.haojiajiao.model.SortModel;

/**
 * Created by Administrator on 2016/1/10.
 */
public class SortAdapter extends AdapterBase<SortModel> {
    public static String[] testCourse = {"Best review", "Price up", "Price down"};

    public SortAdapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        SortModel cityModel = (SortModel) getItem(position);
        if (cityModel.isCatchSort()) {
            convertView = inflater.inflate(R.layout.city_list1, null);
        } else {
            convertView = inflater.inflate(R.layout.city_list2, null);
        }
        TextView tv_text = (TextView) convertView.findViewById(R.id.tv_text);
        tv_text.setText(testCourse[position]);
        return convertView;
    }

    public void setSortColor(int position) {
        for (int i = 0; i < getCount(); i++) {
            ((Course2Model) getItem(i)).setCatchCourse(false);
            ((Course2Model) getItem(position)).setCatchCourse(true);
        }
        notifyDataSetChanged();
    }
}

package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.Course2Model;
import com.shanghai.haojiajiao.model.Model;

/**
 * Created by Administrator on 2016/1/10.
 */
public class Course2Adapter extends AdapterBase<Course2Model> {
    public static final String[] testCourse = {Model.EN, Model.MA, Model.SC, Model.GC, Model.GA, Model.PH, Model.CH};
    public static final String[] testCourseType = {"EN", "MA","SC", "GC", "GA", "PH", "CH"};
    public Course2Adapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        Course2Model cityModel = (Course2Model) getItem(position);

        if (cityModel.isCatchCourse()) {
            convertView = inflater.inflate(R.layout.city_list1, null);
        } else {
            convertView = inflater.inflate(R.layout.city_list2, null);
        }
        TextView tv_text = (TextView) convertView.findViewById(R.id.tv_text);
        tv_text.setText(testCourse[position]);
        return convertView;
    }

    public void setCourse2Color(int position) {
        for (int i = 0; i < testCourse.length; i++) {
            ((Course2Model) getItem(i)).setCatchCourse(false);
            ((Course2Model) getItem(position)).setCatchCourse(true);
        }
        notifyDataSetChanged();
    }
}

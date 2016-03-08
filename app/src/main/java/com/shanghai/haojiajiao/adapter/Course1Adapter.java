package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.Course1Model;
import com.shanghai.haojiajiao.model.Model;

/**
 * Created by Administrator on 2016/1/10.
 */
public class Course1Adapter extends AdapterBase<Course1Model> {
//    public static final String[] testCourse = {Model.AD, Model.HI, Model.HS, Model.OT};
//    public static final String[] testCourse1 = {"AD", "HI", "HS", "OT"};

    public static final String[] testCourse = {Model.APEN, Model.APMA, Model.APAB, Model.HIEN,
            Model.HIMA, Model.HISC, Model.HIWR, Model.SCEN, Model.SCMA, Model.SCSC, Model.SCWR};
    public static final String[] testCourse1 = {"Preparation English", "Preparation Math",
            "Preparation Ability", "High School English", "High School Math", "High School Science",
            "High School Writing", "HSC English", "HSC Math", "HSC Science", "HSC Writing"};

    public Course1Adapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        Course1Model cityModel = (Course1Model) getItem(position);

        if (cityModel.isCatchCourse()) {
            convertView = inflater.inflate(R.layout.city_list1, null);
        } else {
            convertView = inflater.inflate(R.layout.city_list2, null);
        }
        TextView tv_text = (TextView) convertView.findViewById(R.id.tv_text);
        tv_text.setText(testCourse[position]);
        return convertView;
    }

    public void setCourse1Color(int position) {
        for (int i = 0; i < getCount(); i++) {
            ((Course1Model) getItem(i)).setCatchCourse(false);
            ((Course1Model) getItem(position)).setCatchCourse(true);
        }
        notifyDataSetChanged();
    }
}

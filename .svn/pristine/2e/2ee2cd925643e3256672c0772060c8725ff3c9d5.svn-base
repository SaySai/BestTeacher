package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;

/**
 * Created by Administrator on 2016/1/13.
 */
public class CourseListAdapter extends AdapterBase<String> {

    public CourseListAdapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.course_list_layout, null);
        TextView tv_class = (TextView) convertView.findViewById(R.id.tv_class);
        TextView tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
        TextView tv_booking = (TextView) convertView.findViewById(R.id.tv_booking);
        tv_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPositon(position);
            }
        });
        return convertView;
    }
}

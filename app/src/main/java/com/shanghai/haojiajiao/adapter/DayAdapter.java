package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.DayModel;

/**
 * Created by Administrator on 2015/12/23.
 */
public class DayAdapter extends AdapterBase<DayModel> {
    public static String[] hours = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"};
    public static String[] abc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n"};

    public DayAdapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.day_layout, null);
        DayModel dayModel = (DayModel) getItem(position);
        TextView tv_times1 = (TextView) convertView.findViewById(R.id.tv_times1);
        TextView fuhao = (TextView) convertView.findViewById(R.id.fuhao);
        TextView tv_times2 = (TextView) convertView.findViewById(R.id.tv_times2);
        if (position < 14) {
            tv_times1.setText(hours[position]);
            tv_times2.setText(hours[position + 1]);
            if (dayModel.getState() == 0) {
                tv_times1.setTextColor(Color.BLACK);
                fuhao.setTextColor(Color.BLACK);
                tv_times2.setTextColor(Color.BLACK);
                convertView.setBackgroundColor(Color.WHITE);
            } else if (dayModel.getState() == 1) {
                tv_times1.setTextColor(Color.WHITE);
                fuhao.setTextColor(Color.WHITE);
                tv_times2.setTextColor(Color.WHITE);
                convertView.setBackgroundColor(Color.rgb(67, 205, 128));

            } else if (dayModel.getState() == 2) {
                tv_times1.setTextColor(Color.WHITE);
                fuhao.setTextColor(Color.WHITE);
                tv_times2.setTextColor(Color.WHITE);
                convertView.setBackgroundColor(Color.rgb(255, 99, 99));
            }
        }
        return convertView;
    }

    public String getTeacherTime() {
        StringBuffer str = new StringBuffer();
        for (int a = 0; a < 14; a++) {
            DayModel item = (DayModel) getItem(a);
            if (item.getState() == 1) {
                str.append(abc[a]);
            }
        }
        return str.toString();
    }

    public String getParentTime() {
        StringBuffer str = new StringBuffer();
        for (int a = 0; a < 14; a++) {
            DayModel item = (DayModel) getItem(a);
            if (item.getState() == 2 && item.isClick()) {
                str.append(abc[a]);
            }
        }
        return str.toString();
    }


}

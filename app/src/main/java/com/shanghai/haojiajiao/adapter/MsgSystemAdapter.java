package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;

/**
 * Created by Administrator on 2015/12/18.
 */
public class MsgSystemAdapter<String> extends AdapterBase<String> {
    public MsgSystemAdapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.msg_system_list, null);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tv_times = (TextView) convertView.findViewById(R.id.tv_times);
        return convertView;
    }
}

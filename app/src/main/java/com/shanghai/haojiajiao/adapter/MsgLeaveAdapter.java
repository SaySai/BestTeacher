package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;

/**
 * Created by Administrator on 2015/12/18.
 */
public class MsgLeaveAdapter<String> extends AdapterBase<String> {
    public MsgLeaveAdapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.msg_leave_list, null);
        return convertView;
    }
}

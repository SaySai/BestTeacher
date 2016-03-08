package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/30.
 */
public class SearchHistorydapter extends AdapterBase<String> {
    private SharedPreferences preferences;

    public SearchHistorydapter(Activity act, SharedPreferences preferences) {
        super(act);
        this.preferences = preferences;
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.text1_layout, null);
        TextView tv_text = (TextView) convertView.findViewById(R.id.tv_text);
        tv_text.setText(getItem(Math.abs(getCount() - 1 - position)).toString());
        tv_text.setTextColor(Color.BLACK);
        TextView tv_del = (TextView) convertView.findViewById(R.id.tv_del);
        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPositon(Math.abs(getCount() - 1 - position));
            }
        });
        return convertView;
    }


}

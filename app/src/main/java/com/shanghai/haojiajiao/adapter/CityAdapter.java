package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.CityModel;

/**
 * Created by Administrator on 2016/1/10.
 */
public class CityAdapter extends AdapterBase<CityModel> {
    public static final String[] testCity = {"NSW", "QLD", "SA", "TAS", "VIC", "WA","ACT"};

    public CityAdapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        CityModel cityModel = (CityModel) getItem(position);

        if (cityModel.isCatchCity()) {
            convertView = inflater.inflate(R.layout.city_list1, null);
        } else {
            convertView = inflater.inflate(R.layout.city_list2, null);
        }
        TextView tv_text = (TextView) convertView.findViewById(R.id.tv_text);
        tv_text.setText(testCity[position]);
        return convertView;
    }

    public void setCityColor(int position) {
        for (int i = 0; i < getCount(); i++) {
            ((CityModel) getItem(i)).setCatchCity(false);
            ((CityModel) getItem(position)).setCatchCity(true);
        }
        notifyDataSetChanged();
    }
}

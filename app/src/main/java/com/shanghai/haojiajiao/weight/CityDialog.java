package com.shanghai.haojiajiao.weight;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.CityAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.model.CityModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/12.
 */
public class CityDialog extends Dialog {
    private ListView lv_city_teacher;
    private TextView tv_city_gps_teacher;
    private CityLisenner cityLisenner;
    private CityAdapter cityAdapter;
    private Activity activity;

    public CityDialog(Activity context, CityLisenner cityLisenner) {
        super(context, R.style.MyDialog);
        this.cityLisenner = cityLisenner;
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_dialog);
        lv_city_teacher = (ListView) findViewById(R.id.lv_city_teacher);
        lv_city_teacher.setAdapter(cityAdapter = new CityAdapter(activity));
        tv_city_gps_teacher = (TextView) findViewById(R.id.tv_city_gps_teacher);
        String city = HaojiajiaoApplication.city;
        cityAdapter.clearData();
        ArrayList<CityModel> cityModels = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            CityModel cityModel = new CityModel();
            cityModel.setCatchCity(i == 2);
            cityModels.add(cityModel);
        }
        cityAdapter.setData(cityModels);
        if (tv_city_gps_teacher != null) {
            if (city != null) {
                tv_city_gps_teacher.setText("当前定位城市：" + city);
                tv_city_gps_teacher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CityModel cityMode = new CityModel();
                        cityLisenner.getCity(cityMode);
                        dismiss();
                    }
                });
            } else {
                tv_city_gps_teacher.setText("定位信息无法获取");
            }
            lv_city_teacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CityModel cityModel = (CityModel) cityAdapter.getItem(position);
                    cityLisenner.getCity(cityModel);
                    dismiss();
                }
            });
        }
    }

}

package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.MyOrderModel;

/**
 * Created by Administrator on 2015/12/21.
 */
public class OrderAdapter extends AdapterBase<MyOrderModel> {
    public OrderAdapter(Activity act) {
        super(act);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.order_list, null);
        MyOrderModel myOrderModel = (MyOrderModel) getItem(position);
        TextView tv_times = (TextView) convertView.findViewById(R.id.tv_times);
        tv_times.setText(myOrderModel.getOrderTime());
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        tv_number.setText(myOrderModel.getOrderNumber() + "");
        TextView tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
        tv_teacher.setText(myOrderModel.getTeacherId() + "xxxxxx");
        TextView tv_finish = (TextView) convertView.findViewById(R.id.tv_finish);
        if (myOrderModel.getOrderState().equals("1")) {
            tv_finish.setText("已预约");
        } else if (myOrderModel.getOrderState().equals("2")) {
            tv_finish.setText("预约完成");

        } else if (myOrderModel.getOrderState().equals("3")) {
            tv_finish.setText("协调处理中");

        } else if (myOrderModel.getOrderState().equals("4")) {

            tv_finish.setText("教师交易失败");
        } else if (myOrderModel.getOrderState().equals("5")) {

            tv_finish.setText("家长教交易失败");
        } else if (myOrderModel.getOrderState().equals("6")) {
            tv_finish.setText("交易达成");

        }

        return convertView;
    }
}

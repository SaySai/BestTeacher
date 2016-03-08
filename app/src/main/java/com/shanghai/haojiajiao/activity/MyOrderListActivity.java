package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.OrderAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.model.MyOrderModel;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.weight.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrderListActivity extends BaseActivity {
    private ImageView iv_back;
    private ListView listview;
    private OrderAdapter orderAdapter;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyOrderListActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        listview = (ListView) findViewById(R.id.listview);
        orderAdapter = new OrderAdapter(this);
        listview.setAdapter(orderAdapter);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyOrderListActivity.this, OrderSthingActivity.class);
                MyOrderModel myOrderModel = (MyOrderModel) orderAdapter.getItem(position);
                intent.putExtra("TimeSegment", myOrderModel.getOrderTimeSegment());
                intent.putExtra("TeacherId", myOrderModel.getTeacherId());
                intent.putExtra("ParentId", myOrderModel.getParentId());
                intent.putExtra("OrderDay", myOrderModel.getOrderDay());
                intent.putExtra("OrderCharge", myOrderModel.getOrderCharge());
                intent.putExtra("OrderState", myOrderModel.getOrderState());
                intent.putExtra("orderNumber", myOrderModel.getOrderNumber());
                intent.putExtra("userName", myOrderModel.getUserName());
                startActivity(intent);
            }
        });
        if (HaojiajiaoApplication.ISSTATE) {
            getTeacherOrder();
        } else {
            getParentOrder();
        }
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MyOrderListActivity.class.getCanonicalName().equals(intent.getAction())) {
                finish();
            }
        }
    }

    private void getParentOrder() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("ParentId", HaojiajiaoApplication.userId + "");
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getParentOrder, stringMap, RequestTag.getParentOrder);
    }

    private void getTeacherOrder() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("TeacherId", HaojiajiaoApplication.userId + "");
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherOrder, stringMap, RequestTag.getParentOrder);
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        loadingDialog.dismiss();
        super.onRequestError(response);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        if (response.requestTag.toString().equals("getParentOrder")) {
            String dataStr = response.responseString;
            try {
                JSONArray jsonArray = new JSONArray(dataStr);
                if (jsonArray != null) {
                    ArrayList<MyOrderModel> arrayList = new ArrayList<>();
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        MyOrderModel model = new MyOrderModel();
                        model.setId(jsonObject.getInt("id"));
                        model.setParentId(jsonObject.getInt("parentId"));
                        model.setTeacherId(jsonObject.getInt("teacherId"));
                        model.setOrderCharge(jsonObject.optString("orderCharge"));
                        model.setOrderTime(jsonObject.optString("orderTime"));
                        model.setOrderTimeSegment(jsonObject.optString("orderTimeSegment"));
                        model.setOrderNumber(jsonObject.optString("orderNumber"));
                        model.setOrderState(jsonObject.optString("orderState"));
                        model.setOrderDay(jsonObject.optString("orderDay"));
                        userName = jsonObject.optString("parentUserName");
                        if (userName == null) {
                            userName = jsonObject.optString("teacherUserName");
                        }
                        model.setUserName(userName);
                        arrayList.add(model);
                    }
                    orderAdapter.setData(arrayList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

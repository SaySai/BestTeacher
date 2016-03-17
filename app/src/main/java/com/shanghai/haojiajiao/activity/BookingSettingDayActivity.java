package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.DayAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.model.BroadCastAction;
import com.shanghai.haojiajiao.model.DayModel;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.ToastUtil;
import com.shanghai.haojiajiao.weight.LoadingDialog;
import com.shanghai.haojiajiao.weight.MyGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingSettingDayActivity extends BaseActivity {
    private ImageView iv_back;
    private MyGridView myGridView;
    private DayAdapter dayAdapter;
    private DayModel dayModel;
    private ArrayList<DayModel> dayModels = null;
    private TextView tv_thisTime;
    private TextView tv_signUp;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;
    private String endStr = null;
    private String teacherId = null;
    private String appointmentAvailableSegment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_setting_day);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        loadingDialog = new LoadingDialog(BookingSettingDayActivity.this);
        intentFilter.addAction(BroadCastAction.BOOKINGSETTINGDAYACTIVITY);
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        myGridView = (MyGridView) findViewById(R.id.gridview);
        tv_thisTime = (TextView) findViewById(R.id.tv_thisTime);
        int temp = getIntent().getIntExtra("teacherId", 0);
        if (temp == 0) {
            teacherId = getIntent().getStringExtra("teacherId");
        } else {
            teacherId = Integer.toString(temp);
        }

        appointmentAvailableSegment = getIntent().getStringExtra("appointmentAvailableSegment");
        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        dayAdapter = new DayAdapter(this);
        myGridView.setAdapter(dayAdapter);

        String moths = getIntent().getStringExtra("yearMonth");
        int day = getIntent().getIntExtra("day", 0);
        if (day < 10) {
            if (moths != null && day != 0) {
                tv_thisTime.setText(moths + "-0" + day);
            }
        } else {
            if (moths != null && day != 0) {
                tv_thisTime.setText(moths + "-" + day);
            }
        }

        if (HaojiajiaoApplication.ISSTATE) {//HaojiajiaoApplication.ISSTATE
            tv_signUp.setText("SUBMIT");
            dayModels = new ArrayList<>();
            for (int a = 0; a < 16; a++) {
                dayModel = new DayModel();
                if (a < 14) {
                    if (appointmentAvailableSegment != null) {
                        int length = appointmentAvailableSegment.length();
                        for (int b = 0; b < length; b++) {
                            if (DayAdapter.abc[a].equals(String.valueOf(appointmentAvailableSegment.charAt(b)))) {
                                dayModel.setState(1);
                                break;
                            } else {
                                dayModel.setState(0);
                            }
                        }
                    }
                }
                dayModels.add(dayModel);
            }
            dayAdapter.setData(dayModels);
        } else {
            getParentOrderWithTeacher();
        }
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 14) {
                    DayModel model = (DayModel) dayAdapter.getItem(position);
                    if (HaojiajiaoApplication.ISSTATE) {
                        if (model.getState() == 0) {
                            model.setState(1);
                        } else if (model.getState() == 1) {
                            model.setState(0);
                        }
                    } else {
                        if (model.isClick()) {
                            if (model.getState() == 1) {
                                model.setState(2);
                            } else if (model.getState() == 2) {
                                model.setState(1);
                            }
                        }

                    }
                    dayAdapter.notifyDataSetChanged();
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HaojiajiaoApplication.ISSTATE) {
                    editTeacherAvailableTimeByDate();
                } else {
                    Intent intent = new Intent(BookingSettingDayActivity.this, OrderSthingActivity.class);
                    intent.putExtra("teb", 1);
                    if (dayAdapter.getCount() != 0) {
                        intent.putExtra("TimeSegment", dayAdapter.getParentTime());
                        intent.putExtra("TeacherId", teacherId);
                        intent.putExtra("ParentId", getIntent().getStringExtra("parentId"));
                        intent.putExtra("OrderDay", tv_thisTime.getText().toString());
                        double orderCharge;
                        if (getIntent().getStringExtra("OrderCharge") != null) {
                            try {
                                orderCharge = Double.parseDouble(getIntent().getStringExtra("OrderCharge")) * (dayAdapter.getParentTime().length());
                                intent.putExtra("OrderCharge", orderCharge);
                            } catch (Exception e) {
                                intent.putExtra("OrderCharge", getIntent().getStringExtra("OrderCharge"));
                            }
                        } else {
                            intent.putExtra("OrderCharge", 0);
                        }
                        intent.putExtra("userName", getIntent().getStringExtra("TeacherUserName"));
                        startActivity(intent);
                    }

                }
            }
        });

    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadCastAction.BOOKINGSETTINGDAYACTIVITY.equals(intent.getAction())) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void editTeacherAvailableTimeByDate() {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("TeacherId", HaojiajiaoApplication.userId + "");
        dataParas.put("AppointmentTime", tv_thisTime.getText().toString());
        dataParas.put("AppointmentAvailableSegment", dayAdapter.getTeacherTime());
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.editTeacherAvailableTimeByDate, dataParas, RequestTag.editTeacherAvailableTimeByDate);
        loadingDialog.show();
    }

    private void getParentOrderWithTeacher() {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("TeacherId", teacherId);
        dataParas.put("OrderDate", tv_thisTime.getText().toString());
        dataParas.put("ParentId", HaojiajiaoApplication.userId + "");
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getParentOrderWithTeacher, dataParas, RequestTag.getParentOrderWithTeacher);
        loadingDialog.show();
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        loadingDialog.dismiss();
        super.onRequestError(response);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        loadingDialog.dismiss();
        if (response.requestTag.toString().equals("editTeacherAvailableTimeByDate")) {
            String dataStr = response.responseString;
            Log.e("data", dataStr);
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                String jsonArray = total1.getString("result");
                if (jsonArray.equals("1")) {
                    ToastUtil.showLong(BookingSettingDayActivity.this, "Successful modification");
                    finish();
                } else {
                    ToastUtil.showLong(BookingSettingDayActivity.this, "Modify failed");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (response.requestTag.toString().equals("getParentOrderWithTeacher")) {
            String dataStr = response.responseString;
            System.out.println("====>>" + dataStr);
            Log.d("data", dataStr);
            JSONObject total1 = null;
            String jsonArray = null;
            try {
                total1 = new JSONObject(dataStr);
                jsonArray = total1.optString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dayModels = new ArrayList<>();
            for (int a = 0; a < 16; a++) {
                dayModel = new DayModel();
                if (a < 14) {
                    if (appointmentAvailableSegment != null) {
                        dayModel.setState(appointmentAvailableSegment.contains(DayAdapter.abc[a]) ? 1 : 0);
                    }
                }
                dayModels.add(dayModel);
            }
            if (jsonArray != null) {
                for (int b = 0; b < 14; b++) {
                    dayModels.get(b).setClick(!jsonArray.contains(DayAdapter.abc[b]));
                }
            }
            dayAdapter.setData(dayModels);


        }
    }

}

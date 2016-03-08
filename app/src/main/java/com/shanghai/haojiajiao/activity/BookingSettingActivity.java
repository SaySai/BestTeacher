package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.CalendarAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.model.CalendarModel;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.weight.LoadingDialog;
import com.shanghai.haojiajiao.weight.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingSettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_times, tv_next;
    private MyGridView gridview;
    private CalendarAdapter calendarAdapter;
    private CalendarModel ts = null;
    private ArrayList<CalendarModel> calendarModels = null;
    private ArrayList<CalendarModel> calendarModels2 = null;
    private int oneMouth1 = 0;
    private int oneMouth2 = 0;
    private long thisMounthSS = 0;
    private int twoMouth1 = 0;
    private int twoMouth2 = 0;
    private FinishReceiver receiver;
    private int id = 0;
    String str = "This Month";
    private LoadingDialog loadingDialog;
    private int line1 = 0;
    private int line2 = 0;
    private boolean thisM = true;

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_setting);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BookingSettingActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        loadingDialog = new LoadingDialog(BookingSettingActivity.this);
        iv_back.setOnClickListener(this);
        tv_times = (TextView) findViewById(R.id.tv_times);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setText("Next Month");
        gridview = (MyGridView) findViewById(R.id.gridview);
        calendarAdapter = new CalendarAdapter(this);
        gridview.setAdapter(calendarAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tv_next.getText().equals(str)) {//calendarModels2
                    if (calendarModels2.get(position).getChoiceState() == 1) {
                        Intent intent = new Intent(BookingSettingActivity.this, BookingSettingDayActivity.class);
                        intent.putExtra("yearMonth", CalendarAdapter.getCurrentyyyy_MMN(0));
                        intent.putExtra("day", calendarModels2.get(position).getDay() + 1);
                        intent.putExtra("teacherId", HaojiajiaoApplication.userId);
                        intent.putExtra("appointmentAvailableSegment", calendarModels2.get(position).getAppointmentAvailableSegment());
                        startActivity(intent);
                    }
                } else if (tv_next.getText().equals("Next Month")) {//calendarModels
                    if (calendarModels.get(position).getChoiceState() == 1) {
                        Intent intent = new Intent(BookingSettingActivity.this, BookingSettingDayActivity.class);
                        intent.putExtra("yearMonth", CalendarAdapter.getCurrentyyyy_MM(Math.abs(thisMounthSS)));
                        intent.putExtra("day", calendarModels.get(position).getDay() + 1);
                        intent.putExtra("appointmentAvailableSegment", calendarModels.get(position).getAppointmentAvailableSegment());
                        intent.putExtra("teacherId", HaojiajiaoApplication.userId);
                        startActivity(intent);
                    }
                }
            }
        });
        tv_next.setOnClickListener(this);
        tv_times.setText(CalendarAdapter.getCurrentyyyyMM(0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (calendarModels != null) {
            calendarModels.clear();
        }
        if (calendarModels2 != null) {
            calendarModels2.clear();
        }
        oneMounth();
        oneAddMounth(thisMounthSS);
        //getTeacherAvailableDate(getIntent().getIntExtra("id", 0) + "");//教师个人没设置id===>>
        if (getIntent().getIntExtra("id", 0) == 0) {
            getTeacherAvailableDate(Integer.toString(HaojiajiaoApplication.userId));
        } else {
            getTeacherAvailableDate(getIntent().getIntExtra("id", 0) + "");
        }
    }

    private void getTeacherAvailableDate(String id) {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("TeacherId", id);
        System.out.println("TeacherId=" + id);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherAvailableDate, dataParas, RequestTag.getTeacherAvailableDate);
        loadingDialog.show();
    }

    private void oneMounth() {
        calendarModels = new ArrayList<>();
        for (int a = 0; a < 7; a++) {
            ts = new CalendarModel();
            ts.setClickState(false);
            calendarModels.add(ts);
        }
        int zhouji = CalendarAdapter.getWeekNumber(0);
        tv_times.setText(CalendarAdapter.getCurrentyyyyMM(0));
        int mounts = CalendarAdapter.getCurrentMMM(0);
        int[] days = CalendarAdapter.isLeapYear(0, 0);
        int thisDay = CalendarAdapter.getCurrentDatedd();
        int mountCount = days[mounts];
        thisMounthSS = (mountCount + 1) * 24 * 60 * 60 * 1000;
        for (int a = 0; a < zhouji; a++) {
            ts = new CalendarModel();
            ts.setClickState(false);
            calendarModels.add(ts);
        }
        for (int a = 0; a < mountCount; a++) {
            ts = new CalendarModel();
            ts.setClickState(true);
            ts.setDay(a);
            if (a + 1 < 10) {
                ts.setDayTime(CalendarAdapter.getCurrentyyyy_MM(0) + "-0" + (a + 1) + " 00:00:00.0");
            } else {
                ts.setDayTime(CalendarAdapter.getCurrentyyyy_MM(0) + "-" + (a + 1) + " 00:00:00.0");
            }
            //ts.setDayTime(CalendarAdapter.getCurrentyyyy_MM(0) + "-" + (a + 1) + " 00:00:00.0");
            if (a <= (thisDay - 1)) {
                ts.setChoiceState(0);
            } else {//����Ϊ�����ʾ����
                ts.setChoiceState(1);
            }
            calendarModels.add(ts);
        }
        oneMouth1 = zhouji + mountCount;
        line1 = oneMouth1 % 7;

    }

    private void oneAddMounth(long mouth) {
        mouth = Math.abs(mouth);
        calendarModels2 = new ArrayList<>();
        for (int a = 0; a < 7; a++) {
            ts = new CalendarModel();
            ts.setClickState(false);
            calendarModels2.add(ts);
        }
        int zhouji = CalendarAdapter.getWeekNumber(mouth);
//        tv_times.setText(CalendarAdapter.getCurrentyyyyMM(mouth));
        int[] days = CalendarAdapter.isLeapYear(0, mouth);
        int mounts = CalendarAdapter.getCurrentMMM(mouth);
        int mountCount = days[mounts];
        for (int a = 0; a < zhouji; a++) {
            ts = new CalendarModel();
            ts.setClickState(false);
            calendarModels2.add(ts);
        }
        for (int a = 0; a < mountCount; a++) {
            ts = new CalendarModel();
            ts.setClickState(true);
            ts.setDay(a);
            if (a + 1 < 10) {
                ts.setDayTime(CalendarAdapter.getCurrentyyyy_MMN(0) + "-0" + (a + 1) + " 00:00:00.0");
            } else {
                ts.setDayTime(CalendarAdapter.getCurrentyyyy_MMN(0) + "-" + (a + 1) + " 00:00:00.0");
            }
            ts.setChoiceState(1);
            calendarModels2.add(ts);
        }
        twoMouth2 = zhouji + mountCount;
        line2 = twoMouth2 % 7;

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                thisM = !thisM;
                if (calendarModels2 == null) {
                    calendarAdapter.clearData();
                    tv_next.setText(str);
                    if (line2 == 0) {
                        calendarAdapter.setData(calendarModels2);
                    } else {
                        twoMouth2 = 7 - line2;
                        for (int a = 0; a < twoMouth2; a++) {
                            ts = new CalendarModel();
                            ts.setClickState(false);
                            calendarModels2.add(ts);
                        }
                        calendarAdapter.setData(calendarModels2);
                    }
                } else {
                    if (tv_next.getText().equals("Next Month")) {
                        calendarAdapter.clearData();
                        calendarAdapter.setData(calendarModels2);
                        tv_times.setText(CalendarAdapter.getCurrentyyyyMM(Math.abs(thisMounthSS)));

                        tv_next.setText(str);
                    } else if (tv_next.getText().equals(str)) {
                        calendarAdapter.clearData();
                        tv_times.setText(CalendarAdapter.getCurrentyyyyMM(0));
                        calendarAdapter.setData(calendarModels);
                        tv_next.setText("Next Month");
                    }
                }
                break;
        }
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BookingSettingActivity.class.getCanonicalName().equals(intent.getAction())) {
                finish();
            }
        }
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        loadingDialog.dismiss();
        super.onRequestError(response);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        loadingDialog.dismiss();
        if (response.requestTag.toString().equals("getTeacherAvailableDate")) {
            loadingDialog.dismiss();
            calendarAdapter.clearData();
            String dataStr1 = response.responseString;
            //System.out.println("res===>>"+dataStr1);
            try {
                JSONArray jsonArray = new JSONArray(dataStr1);
                if (jsonArray != null) {
                    ArrayList<CalendarModel> arrayList = new ArrayList<>();
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        CalendarModel calendarModel = new CalendarModel();
                        calendarModel.setId(jsonObject.getInt("id"));
                        calendarModel.setHasdata(true);
                        calendarModel.setAppointment(jsonObject.getString("appointmentTime"));
                        calendarModel.setTeacherId(jsonObject.getInt("teacherId"));
                        calendarModel.setAppointmentAvailableSegment(jsonObject.getString("appointmentAvailableSegment"));
                        arrayList.add(calendarModel);
                    }
                    for (int i = 0; i < calendarModels.size(); i++) {
                        if (calendarModels.get(i).isClickState()) {
                            for (int a = 0; a < arrayList.size(); a++) {
                                //System.out.println(calendarModels.get(i).getDayTime()+"----"+arrayList.get(a).getAppointment());
                                if (calendarModels.get(i).getDayTime().equals(arrayList.get(a).getAppointment())) {
                                    if (!arrayList.get(a).getAppointmentAvailableSegment().equals("")) {
                                        //System.out.println("=======>>"+calendarModels.get(i).getDayTime());
                                        calendarModels.get(i).setId(arrayList.get(a).getId());
                                        calendarModels.get(i).setHasdata(true);
                                        calendarModels.get(i).setTeacherId(arrayList.get(a).getTeacherId());
                                        calendarModels.get(i).setAppointmentAvailableSegment(arrayList.get(a).getAppointmentAvailableSegment());
                                    } else {
                                        calendarModels.get(i).setHasdata(false);
                                    }
                                }
                            }
                        }
                    }
                    for (int i = 0; i < calendarModels2.size(); i++) {
                        if (calendarModels2.get(i).isClickState()) {
                            for (int a = 0; a < arrayList.size(); a++) {
                                //System.out.println(calendarModels2.get(i).getDayTime()+"----"+arrayList.get(a).getAppointment());
                                if (calendarModels2.get(i).getDayTime().equals(arrayList.get(a).getAppointment())) {
                                    if (!arrayList.get(a).getAppointmentAvailableSegment().equals("")) {
                                        //System.out.println("=======>>"+calendarModels2.get(i).getDayTime());
                                        calendarModels2.get(i).setId(arrayList.get(a).getId());
                                        calendarModels2.get(i).setHasdata(true);
                                        calendarModels2.get(i).setTeacherId(arrayList.get(a).getTeacherId());
                                        calendarModels2.get(i).setAppointmentAvailableSegment(arrayList.get(a).getAppointmentAvailableSegment());
                                    } else {
                                        calendarModels2.get(i).setHasdata(false);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (thisM) {
                if (line1 == 0) {
                    calendarAdapter.setData(calendarModels);
                } else {
                    oneMouth2 = 7 - line1;
                    for (int a = 0; a < oneMouth2; a++) {
                        ts = new CalendarModel();
                        ts.setClickState(false);
                        calendarModels.add(ts);
                    }
                    calendarAdapter.setData(calendarModels);
                }
            } else {
                if (line2 == 0) {
                    calendarAdapter.setData(calendarModels2);
                } else {
                    twoMouth2 = 7 - line2;
                    for (int a = 0; a < twoMouth2; a++) {
                        ts = new CalendarModel();
                        ts.setClickState(false);
                        calendarModels2.add(ts);
                    }
                    calendarAdapter.setData(calendarModels2);
                }
            }

        }
    }
}

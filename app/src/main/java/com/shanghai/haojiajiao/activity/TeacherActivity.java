package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.CalendarAdapter;
import com.shanghai.haojiajiao.adapter.TeacherEvaluateAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.model.BroadCastAction;
import com.shanghai.haojiajiao.model.CalendarModel;
import com.shanghai.haojiajiao.model.TeacherEvaluateModel;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.ToastUtil;
import com.shanghai.haojiajiao.util.UilUtil;
import com.shanghai.haojiajiao.weight.CircleImageView;
import com.shanghai.haojiajiao.weight.ListViewForScrollView;
import com.shanghai.haojiajiao.weight.LoadingDialog;
import com.shanghai.haojiajiao.weight.LoginDialog;
import com.shanghai.haojiajiao.weight.LoginLisenner;
import com.shanghai.haojiajiao.weight.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 教师详情
 */
public class TeacherActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 返回
     */
    private ImageView iv_back;
    /**
     * 用户头像
     */
    private CircleImageView iv_userIcon;
    /**
     * 用户名称
     */
    private TextView tv_userName;
    /**
     * 用户年龄
     */
    private TextView tv_age1;
    /**
     * 性别
     */
    private TextView iv_sex;
    /**
     * 内容
     */
    private TextView tv_content;
    /**
     * 用户基本信息
     */
    private TextView tv_homePage;
    /**
     * 评价
     */
    private TextView tv_evaluate;
    /**
     * 预约
     */
    private TextView tv_booking;
    /**
     * 聊天
     */
    private TextView tv_message;
    /**
     * 用户基本信息切换页面
     */
    private LinearLayout ll_homePage_tab1;
    /**
     * 评价切换页面
     */
    private LinearLayout ll_homePage_tab2;
    /**
     * 预约切换页面
     */
    private LinearLayout ll_homePage_tab3;
    /**
     * tab1按顺序
     */
    private TextView tv_age;
    private TextView tv_city;
    private TextView tv_pays;
    private TextView tv_course;
    private TextView tv_class;
    private TextView tv_history;
    private TextView tv_mysulf;
    /**
     * tab2按顺序
     */
    private TextView tv_count;
    private ListViewForScrollView listview;
    /**
     * tab3
     */
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
    private ScrollView scrollView;
    String str = "BACK";
    private TeacherEvaluateAdapter teacherEvaluateAdapter;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;
    private int teacherId = 0;
    private String teacherName = null;
    public String teacherUserNames = null;
    //...............................................聊天相关..........................................................
//    String Token = "3t2smePRwqNZL+kSZZj6TmAhfU1q45pmBt2Df5Dd6kCUgHNR7ixL/rYzJsjyZ6u//0f0ll9aB1JEN5lMCShKaw==";//test
    private String teacherUserName = null;
    private int line1 = 0;
    private int line2 = 0;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        loadingDialog = new LoadingDialog(TeacherActivity.this);
        intentFilter.addAction(BroadCastAction.TEACHERACTIVITY);
        teacherUserName = getIntent().getStringExtra("TeacherUserName");
        teacherId = getIntent().getIntExtra("TeacherId", 0);
        teacherName = getIntent().getStringExtra("TeacherName");
        registerReceiver(receiver, intentFilter);
        //RongIM.setUserInfoProvider(this, true);
        initView();
        switchPage(getIntent().getIntExtra("tab", 0));
        getTeacherInfoByUserName();
    }

    private void initView() {
        options = UilUtil.getOptions();
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_userIcon = (CircleImageView) findViewById(R.id.iv_userIcon);
        tv_userName = (TextView) findViewById(R.id.tv_userName);
        tv_age1 = (TextView) findViewById(R.id.tv_age1);
        iv_sex = (TextView) findViewById(R.id.iv_sex);
        tv_content = (TextView) findViewById(R.id.tv_mysulf);
        tv_homePage = (TextView) findViewById(R.id.tv_homePage);
        tv_evaluate = (TextView) findViewById(R.id.tv_evaluate);
        tv_booking = (TextView) findViewById(R.id.tv_booking);
        tv_message = (TextView) findViewById(R.id.tv_message);
        ll_homePage_tab1 = (LinearLayout) findViewById(R.id.ll_homePage_tab1);
        ll_homePage_tab2 = (LinearLayout) findViewById(R.id.ll_homePage_tab2);
        ll_homePage_tab3 = (LinearLayout) findViewById(R.id.ll_homePage_tab3);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        iv_back.setOnClickListener(this);
        tv_homePage.setOnClickListener(this);
        tv_evaluate.setOnClickListener(this);
        tv_booking.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        //tab1
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_pays = (TextView) findViewById(R.id.tv_pays);
        tv_course = (TextView) findViewById(R.id.tv_course);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_history = (TextView) findViewById(R.id.tv_history);
        tv_mysulf = (TextView) findViewById(R.id.tv_mysulf);
        //tab2
        tv_count = (TextView) findViewById(R.id.tv_count);
        listview = (ListViewForScrollView) findViewById(R.id.listview);
        teacherEvaluateAdapter = new TeacherEvaluateAdapter(this);
        listview.setAdapter(teacherEvaluateAdapter);

        //tab3
        tv_times = (TextView) findViewById(R.id.tv_times);
        tv_next = (TextView) findViewById(R.id.tv_next);
        gridview = (MyGridView) findViewById(R.id.gridview);
        calendarAdapter = new CalendarAdapter(this);
        gridview.setAdapter(calendarAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tv_next.getText().equals(str)) {//calendarModels2
                    if (calendarModels2.get(position).getChoiceState() == 1 && calendarModels2.get(position).isHasdata()) {
                        Intent intent = new Intent(TeacherActivity.this, BookingSettingDayActivity.class);
                        intent.putExtra("yearMonth", CalendarAdapter.getCurrentyyyy_MMN(Math.abs(thisMounthSS)));
                        intent.putExtra("day", calendarModels2.get(position).getDay() + 1);
                        intent.putExtra("teacherId", teacherId + "");
                        intent.putExtra("OrderCharge", tv_pays.getText().toString());
                        intent.putExtra("parentId", HaojiajiaoApplication.userId);
                        intent.putExtra("appointmentAvailableSegment", calendarModels2.get(position).getAppointmentAvailableSegment());
                        intent.putExtra("TeacherUserName", teacherUserNames);
                        startActivity(intent);
                    }
                } else if (tv_next.getText().equals("NEXT MONTH")) {//calendarModels
                    if (calendarModels.get(position).getChoiceState() == 1 && calendarModels.get(position).isHasdata()) {
                        Intent intent = new Intent(TeacherActivity.this, BookingSettingDayActivity.class);
                        intent.putExtra("yearMonth", CalendarAdapter.getCurrentyyyy_MM(0));
                        intent.putExtra("day", calendarModels.get(position).getDay() + 1);
                        intent.putExtra("appointmentAvailableSegment", calendarModels.get(position).getAppointmentAvailableSegment());
                        intent.putExtra("OrderCharge", tv_pays.getText().toString());
                        intent.putExtra("parentId", HaojiajiaoApplication.userId);
                        intent.putExtra("teacherId", teacherId + "");
                        intent.putExtra("TeacherUserName", teacherUserNames);
                        startActivity(intent);
                    }
                }
            }
        });
        oneMounth();
        oneAddMounth(thisMounthSS);
        getTeacherAvailableDate(teacherId + "");
//        getTeacherAvailableDate();
        tv_next.setOnClickListener(this);
        //融云相关start..................................................................................................................
//        String Token = "myefoCuvXwmelBzvApExXQJVbg2IPAqSO8Hao5ZEFyWSlhUwbXdnbABIujtXhwq0CpGgqGtMaX2atstURP94jw==";//test
        /**
         * IMKit SDK调用第二步
         *
         * 建立与服务器的连接
         *
         */
        /*Log.e("TeacherAct","token now: "+HaojiajiaoApplication.token);
        RongIM.connect(HaojiajiaoApplication.token, new RongIMClient.ConnectCallback() {

            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
                Log.e("TeacherAct","---onTokenIncorrect---"+HaojiajiaoApplication.token);
            }

            @Override
            public void onSuccess(String userId) {
                Log.e("TeacherAct", "——onSuccess— -" + userId);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("MainActivity", "——onError— -" + errorCode);
            }
        });*/
        //融云相关end.................................................................................................................
    }/**/

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_homePage:
                switchPage(0);
                break;
            case R.id.tv_evaluate:
                switchPage(1);
                break;
            case R.id.tv_booking:
                if (HaojiajiaoApplication.IFLOGIN) {
                    if (!HaojiajiaoApplication.ISSTATE) {
                        switchPage(2);
                    }
                } else {
                    new LoginDialog(TeacherActivity.this, "Login before you can make an appointment！", new LoginLisenner() {
                        @Override
                        public void login() {
                            Intent intent = new Intent(BroadCastAction.MAINACTIVITY);
                            sendBroadcast(intent);
                            Intent intent1 = new Intent(BroadCastAction.TEACHERACTIVITY);
                            sendBroadcast(intent1);
                        }

                        @Override
                        public void sign() {
                            Intent intent = new Intent(BroadCastAction.MAINACTIVITY);
                            sendBroadcast(intent);
                            Intent intent1 = new Intent(BroadCastAction.TEACHERACTIVITY);
                            sendBroadcast(intent1);
                        }
                    }).show();
                }

                break;
            case R.id.tv_message:
                if (HaojiajiaoApplication.IFLOGIN) {
                    //跳转到聊天
                    //启动单聊
                    /**
                     * 启动单聊
                     * context - 应用上下文。
                     * targetUserId - 要与之聊天的用户 Id。
                     * title - 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                     */
                    if (HaojiajiaoApplication.ISSTATE == false) {
                        if (RongIM.getInstance() != null) {
                            Log.e("TeacherAct:","startPrivateChat teacherUserName:"+teacherUserName);
                            Log.e("chat with a teacher:","picUrl: "+ HaojiajiaoApplication.picUrl);
                            RongContext.getInstance().getUserInfoCache().put(HaojiajiaoApplication.userName,
                                    new UserInfo(HaojiajiaoApplication.userName,
                                            HaojiajiaoApplication.name,
                                            Uri.parse(HaojiajiaoApplication.picUrl)));
                            RongIM.getInstance().startPrivateChat(this, teacherUserName, teacherName);
                        }//String.valueOf(teacherId)
                    } else {
                        ToastUtil.showShort(this, "teacher can not send a message to teacher!");

                    }
                } else {
                    new LoginDialog(TeacherActivity.this, "Login before you can make an appointment！", new LoginLisenner() {
                        @Override
                        public void login() {
                            Intent msgIntent = new Intent(MainActivity.class.getCanonicalName());
                            sendBroadcast(msgIntent);
                            Intent ntent = new Intent(TeacherActivity.class.getCanonicalName());
                            sendBroadcast(ntent);
                            finish();
                        }

                        @Override
                        public void sign() {
                            Intent msgIntent = new Intent(MainActivity.class.getCanonicalName());
                            sendBroadcast(msgIntent);
                            Intent ntent = new Intent(TeacherActivity.this, WelcomeActivity.class);
                            sendBroadcast(ntent);
                            finish();
                        }
                    }).show();
                }
                break;
            case R.id.tv_next:
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
                    if (tv_next.getText().equals("NEXT MONTH")) {
                        calendarAdapter.clearData();
                        calendarAdapter.setData(calendarModels2);
                        tv_times.setText(CalendarAdapter.getCurrentyyyyMM(Math.abs(thisMounthSS)));

                        tv_next.setText(str);
                    } else if (tv_next.getText().equals(str)) {
                        calendarAdapter.clearData();
                        tv_times.setText(CalendarAdapter.getCurrentyyyyMM(Math.abs(thisMounthSS+24 * 60 * 60 * 1000)));
                        calendarAdapter.setData(calendarModels);

                        tv_next.setText("NEXT MONTH");
                    }
                }
                break;
        }
    }

    private void switchPage(int number) {
        switch (number) {
            case 0:
                //getResources().getColor(R.color.tab_no_press_color);
                tv_homePage.setTextColor(0xFF1ABC9C);
                tv_evaluate.setTextColor(Color.BLACK);
                tv_booking.setTextColor(Color.BLACK);
                tv_homePage.getPaint().setFakeBoldText(true);
                tv_evaluate.getPaint().setFakeBoldText(false);
                tv_booking.getPaint().setFakeBoldText(false);
                ll_homePage_tab1.setVisibility(View.VISIBLE);
                ll_homePage_tab2.setVisibility(View.GONE);
                ll_homePage_tab3.setVisibility(View.GONE);
                break;
            case 1:
                tv_homePage.setTextColor(Color.BLACK);
                tv_evaluate.setTextColor(0xFF1ABC9C);
                tv_booking.setTextColor(Color.BLACK);
                tv_homePage.getPaint().setFakeBoldText(false);
                tv_evaluate.getPaint().setFakeBoldText(true);
                tv_booking.getPaint().setFakeBoldText(false);
                ll_homePage_tab1.setVisibility(View.GONE);
                ll_homePage_tab2.setVisibility(View.VISIBLE);
                ll_homePage_tab3.setVisibility(View.GONE);
                break;
            case 2:
                tv_homePage.setTextColor(Color.BLACK);
                tv_evaluate.setTextColor(Color.BLACK);
                tv_booking.setTextColor(0xFF1ABC9C);
                tv_homePage.getPaint().setFakeBoldText(false);
                tv_evaluate.getPaint().setFakeBoldText(false);
                tv_booking.getPaint().setFakeBoldText(true);
                ll_homePage_tab1.setVisibility(View.GONE);
                ll_homePage_tab2.setVisibility(View.GONE);
                ll_homePage_tab3.setVisibility(View.VISIBLE);
                break;
        }
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
            ts.setDayTime(CalendarAdapter.getCurrentyyyy_MM(0) + "-" + (a + 1) + " 00:00:00.0");
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
            ts.setDayTime(CalendarAdapter.getCurrentyyyy_MM(0) + "-" + (a + 1) + " 00:00:00.0");
            ts.setChoiceState(1);
            calendarModels2.add(ts);
        }
        twoMouth2 = zhouji + mountCount;
        line2 = twoMouth2 % 7;

    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadCastAction.TEACHERACTIVITY.equals(intent.getAction())) {
                Intent intent1 = new Intent(TeacherActivity.this, WelcomeActivity.class);
                startActivity(intent1);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        loadingDialog.dismiss();
        super.onRequestError(response);
    }

    private void getTeacherInfoByUserName() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("TeacherUserName", teacherUserName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherInfoByUserName, stringMap, RequestTag.getTeacherInfoByUserName);
    }

    private void getTeacherInfoByUserName(String TeacherUserName) {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("TeacherUserName", TeacherUserName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherInfoByUserName, stringMap, RequestTag.getTeacherInfoByUserNameForRY);
    }

    private void getParentInfoByUserName(String parentUserName) {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("userName", parentUserName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getParentInfoByUserName, stringMap, RequestTag.getParentInfoByUserName);
    }

    private void getTeacherEvaluation() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("TeacherId", teacherId + "");
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherEvaluation, stringMap, RequestTag.getTeacherEvaluation);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {

        if (response.requestTag.toString().equals("getTeacherInfoByUserName")) {
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);
                setEditString(tv_userName, total1.optString("teacherName") + "");
                setEditString(tv_age1, total1.optString("teacherAge") + "");
                setEditString(iv_sex, total1.optString("teacherGender") + "");
                setEditString(tv_age, total1.optString("teacherAge") + "");
                setEditString(tv_city, total1.optString("teacherCity") + "");
                setEditString(tv_pays, total1.optString("teacherCharge") + "");
                setEditString(tv_course, total1.optString("teacherLesson") + "");
                setEditString(tv_content, total1.optString("teacherSelfCv") + "");
                teacherUserNames = total1.optString("teacherUserName") + "";
                //获取用户头像
                ImageLoader.getInstance().displayImage(
                        total1.optString("teacherPortrait"), iv_userIcon, options);
                /*HaojiajiaoApplication.R_name = total1.optString("teacherName");
                HaojiajiaoApplication.R_userName = total1.optString("teacherUserName");
                HaojiajiaoApplication.R_picUrl = total1.optString("teacherPortrait");
                Log.e("response:","teacher: R_userName:"+HaojiajiaoApplication.R_userName);
                HaojiajiaoApplication.response=true;*/
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TeacherActivity","get teacherIcon error!!!");
//                HaojiajiaoApplication.R_name=null;
//                HaojiajiaoApplication.R_userName=null;
//                HaojiajiaoApplication.R_picUrl=null;
            }
            getTeacherEvaluation();
        }/*else if (response.requestTag.toString().equals("getTeacherInfoByUserNameForRY")) {
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);
                HaojiajiaoApplication.R_name = total1.optString("teacherName");
                HaojiajiaoApplication.R_userName = total1.optString("teacherUserName");
                HaojiajiaoApplication.R_picUrl = total1.optString("teacherPortrait");
                Log.e("response:", "teacher: R_userName:" + HaojiajiaoApplication.R_userName);
                if(!HaojiajiaoApplication.R_userName.equals("")){
                    HaojiajiaoApplication.response = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TeacherActivity", "get teacherIcon error!!!");
//                HaojiajiaoApplication.R_name=null;
//                HaojiajiaoApplication.R_userName=null;
//                HaojiajiaoApplication.R_picUrl=null;
            }
        } else if (response.requestTag.toString().equals("getParentInfoByUserName")) {
            String dataStr = response.responseString;
//            HaojiajiaoApplication.R_name=null;
//            HaojiajiaoApplication.R_userName=null;
//            HaojiajiaoApplication.R_picUrl=null;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                JSONObject jsonArray = total1.getJSONObject("result");
                HaojiajiaoApplication.R_name = jsonArray.getString("parentName");
                HaojiajiaoApplication.R_picUrl = jsonArray.getString("parentPortrait");
                HaojiajiaoApplication.R_userName = jsonArray.getString("parentUserName");
                Log.e("response:","parent: R_userName:"+HaojiajiaoApplication.R_userName);
                if(!HaojiajiaoApplication.R_userName.equals("")){
                    HaojiajiaoApplication.response = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TeacherActivity","get parentIcon error!!!");
//                HaojiajiaoApplication.R_name=null;
//                HaojiajiaoApplication.R_userName=null;
//                HaojiajiaoApplication.R_picUrl=null;
            }

        }*/ else if (response.requestTag.toString().equals("getTeacherEvaluation")) {
            loadingDialog.dismiss();
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);
                JSONArray jsonArray = total1.getJSONArray("result");
                if (jsonArray != null) {
                    ArrayList<TeacherEvaluateModel> arrayList = new ArrayList<>();
                    tv_count.setText("" + jsonArray.length());
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(a);
                        TeacherEvaluateModel model = new TeacherEvaluateModel();
                        model.setId(jsonObject.optInt("id"));
                        model.setEvaluationContent(jsonObject.optString("evaluationContent"));
                        model.setEvaluationRate(jsonObject.optInt("evaluationRate"));
                        model.setParentId(jsonObject.optInt("parentId"));
                        model.setTeacherId(jsonObject.optInt("teacherId"));
                        model.setEvaluationTime(jsonObject.optString("evaluationTime"));
                        model.setPicUrl(jsonObject.optString("ParentPor"));
                        arrayList.add(model);
                    }
                    teacherEvaluateAdapter.setData(arrayList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (response.requestTag.toString().equals("getTeacherAvailableDate")) {
            loadingDialog.dismiss();
            String dataStr1 = response.responseString;
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
                                if (calendarModels.get(i).getDayTime().equals(arrayList.get(a).getAppointment())) {
                                    if (!arrayList.get(a).getAppointmentAvailableSegment().equals("")) {
                                        System.out.println("=======>>" + calendarModels.get(i).getDayTime());
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
                                if (calendarModels2.get(i).getDayTime().equals(arrayList.get(a).getAppointment())) {
                                    if (!arrayList.get(a).getAppointmentAvailableSegment().equals("")) {
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

        }

    }

//    private void getTeacherAvailableDate() {
//        Map<String, String> dataParas = new HashMap<>();
//        dataParas.put("TeacherId", 1 + "");
//        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherAvailableDate, dataParas, RequestTag.getTeacherAvailableDate);
//    }

    private void getTeacherAvailableDate(String id) {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("TeacherId", id);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherAvailableDate, dataParas, RequestTag.getTeacherAvailableDate);
    }

    private void setEditString(TextView editString, String str) {
        editString.setText(str + "");
    }

/*    @Override
    public  UserInfo getUserInfo(String s) {
        Log.e("TeacherAct:","userName:"+s);

        HaojiajiaoApplication.response=false;
        getTeacherInfoByUserName(s);
        getParentInfoByUserName(s);
        //if(HaojiajiaoApplication.R_userName!=null){
        while(true){
            if(HaojiajiaoApplication.response){
                break;
            }
        }
        Log.e("TeacherAct:","getUserInfo: "+HaojiajiaoApplication.R_userName);
        return new UserInfo(HaojiajiaoApplication.R_userName,HaojiajiaoApplication.R_name,
                Uri.parse(HaojiajiaoApplication.R_picUrl));
    }*/
}

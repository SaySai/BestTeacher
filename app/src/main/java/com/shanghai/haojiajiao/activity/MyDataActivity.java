package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.CityAdapter;
import com.shanghai.haojiajiao.adapter.Course1Adapter;
import com.shanghai.haojiajiao.adapter.CourseAdapter;
import com.shanghai.haojiajiao.adapter.CourseListAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.model.CityModel;
import com.shanghai.haojiajiao.model.Course1Model;
import com.shanghai.haojiajiao.model.CourseModel;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.ToastUtil;
import com.shanghai.haojiajiao.weight.CourseDialog;
import com.shanghai.haojiajiao.weight.CourseLisenner;
import com.shanghai.haojiajiao.weight.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MyDataActivity extends BaseActivity implements View.OnClickListener {
    private ScrollView sv_tab1, sv_tab2;
    private ImageView iv_back;
    private TextView tv_parent, tv_teacher;
    //tab2Parent
    private EditText et_parentTab2Name, et_parentTab2Sax, et_parentTab2Email, et_parentTab2Phone;
    private EditText et_ChildTab2Name, et_ChildTab2Sax, et_ChildTab2Age, et_ChildTab2Email;
    //tab2Teacher
    private EditText et_money, et_TeacherTab2Name, et_TeacherTab2Sax, et_TeacherTab2Age, et_TeacherTab2Email, et_TeacherTab2Phone, et_TeacherTab2ZPay, et_TeacherTab2Sulf;
    //tab3Teacher
    private ImageView et_TeacherTab3Add;
    private ListView et_TeacherTab3List;
    private FinishReceiver receiver;


    private TextView et_ChildTab2City;
    private LinearLayout ll_city_listShow;
    //城市
    private LinearLayout ll_city_list;
    private ListView lv_city;
    private CityAdapter cityAdapter;
    private CityAdapter cityAdapter1;
    private TextView tv_city_gps;
    private LinearLayout ll_city_list_teacher;
    private ListView lv_city_teacher;
    private TextView et_TeacherTab2City;
    private LinearLayout ll_teacher_city;
    private CourseListAdapter courseListAdapter;
    private LoadingDialog loadingDialog;
    private int id = 0;
    private String lessons = null;
    List<CourseModel> models = new ArrayList<>();
    List<String> positionList = new ArrayList<>();
    private boolean isFrist = true;
    private String bigLei = "";//大类,比方说  高年级 低年级
    private String smallLei = "";//小类,各种学科
    private CourseAdapter courseAdapter;

    public static final int GET_TOKEN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        loadingDialog = new LoadingDialog(MyDataActivity.this);
        intentFilter.addAction(MyDataActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        sv_tab1 = (ScrollView) findViewById(R.id.sv_tab1);
        sv_tab2 = (ScrollView) findViewById(R.id.sv_tab2);
        tv_parent = (TextView) findViewById(R.id.tv_parent);
        tv_parent.setOnClickListener(this);
        tv_teacher = (TextView) findViewById(R.id.tv_teacher);
        tv_teacher.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        initTabPrarent2();
        initTabTeacher2();
        initTabTeacher3();
        if (!HaojiajiaoApplication.ISSTATE) {
            getParentInfoByUserName();
            sv_tab1.setVisibility(View.GONE);
            sv_tab2.setVisibility(View.VISIBLE);
        } else {
            getTeacherInfoByUserName();
            sv_tab1.setVisibility(View.VISIBLE);
            sv_tab2.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switchView(v.getId());
    }

    private void switchView(int id) {
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_parent:
                updateParent();
                //loadingDialog.show();
                break;
            case R.id.tv_teacher:
                updateTeacher();
                //loadingDialog.show();
                break;
        }
    }

    private void initTabPrarent2() {
        //parent
        et_parentTab2Name = (EditText) findViewById(R.id.et_parentTab2Name);
        et_parentTab2Sax = (EditText) findViewById(R.id.et_parentTab2Sax);
        et_parentTab2Email = (EditText) findViewById(R.id.et_parentTab2Email);
        et_parentTab2Phone = (EditText) findViewById(R.id.et_parentTab2Phone);
        //child
        et_ChildTab2Name = (EditText) findViewById(R.id.et_ChildTab2Name);
        et_ChildTab2City = (TextView) findViewById(R.id.et_ChildTab2City);
        et_ChildTab2Sax = (EditText) findViewById(R.id.et_ChildTab2Sax);
        et_ChildTab2Age = (EditText) findViewById(R.id.et_ChildTab2Age);
        et_ChildTab2Email = (EditText) findViewById(R.id.et_ChildTab2Email);
        ll_city_listShow = (LinearLayout) findViewById(R.id.ll_city_listShow);
        ll_city_listShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_city_list.getVisibility() == View.GONE) {
                    ll_city_list.setVisibility(View.VISIBLE);
                    cityAdapter1.clearData();
                    ArrayList<CityModel> cityModels = new ArrayList<>();
                    for (int i = 0; i < 7; i++) {
                        CityModel cityModel = new CityModel();
                        cityModel.setCatchCity(i == 2);
                        cityModels.add(cityModel);
                    }
                    cityAdapter1.setData(cityModels);
                } else {
                    ll_city_list.setVisibility(View.GONE);
                }
            }
        });
        ll_city_list = (LinearLayout) findViewById(R.id.ll_city_list);
       // tv_city_gps = (TextView) findViewById(R.id.tv_city_gps);
        lv_city = (ListView) findViewById(R.id.lv_city);
        cityAdapter1 = new CityAdapter(MyDataActivity.this);
        lv_city.setAdapter(cityAdapter1);
        lv_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sv_tab2.requestDisallowInterceptTouchEvent(false);
                } else {
                    sv_tab2.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_ChildTab2City.setText(CityAdapter.testCity[position]);
                ll_city_list.setVisibility(View.GONE);
            }
        });
        //setGpsCity(HaojiajiaoApplication.city);

    }

    private void initTabTeacher2() {
        et_TeacherTab2Name = (EditText) findViewById(R.id.et_TeacherTab2Name);
        et_TeacherTab2Sax = (EditText) findViewById(R.id.et_TeacherTab2Sax);
        et_TeacherTab2Age = (EditText) findViewById(R.id.et_TeacherTab2Age);
        et_TeacherTab2Email = (EditText) findViewById(R.id.et_TeacherTab2Email);
        et_TeacherTab2Phone = (EditText) findViewById(R.id.et_TeacherTab2Phone);
        et_TeacherTab2City = (TextView) findViewById(R.id.et_TeacherTab2City);
        et_TeacherTab2ZPay = (EditText) findViewById(R.id.et_TeacherTab2ZPay);
        et_TeacherTab2Sulf = (EditText) findViewById(R.id.et_TeacherTab2Sulf);
        et_ChildTab2City = (TextView) findViewById(R.id.et_ChildTab2City);
        ll_teacher_city = (LinearLayout) findViewById(R.id.ll_teacher_city);
        et_money = (EditText) findViewById(R.id.et_money);
        ll_teacher_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_city_list_teacher.getVisibility() == View.GONE) {
                    ll_city_list_teacher.setVisibility(View.VISIBLE);
                    cityAdapter.clearData();
                    ArrayList<CityModel> cityModels = new ArrayList<>();
                    for (int i = 0; i < 7; i++) {
                        CityModel cityModel = new CityModel();
                        cityModel.setCatchCity(i == 2);
                        cityModels.add(cityModel);
                    }
                    cityAdapter.setData(cityModels);
                } else {
                    ll_city_list_teacher.setVisibility(View.GONE);
                }
            }
        });
        ll_city_list_teacher = (LinearLayout) findViewById(R.id.ll_city_list_teacher);
        lv_city_teacher = (ListView) findViewById(R.id.lv_city_teacher);
        lv_city_teacher.setAdapter(cityAdapter = new CityAdapter(MyDataActivity.this));
        lv_city_teacher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sv_tab1.requestDisallowInterceptTouchEvent(false);
                } else {
                    sv_tab1.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        lv_city_teacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                et_TeacherTab2City.setText(CityAdapter.testCity[position]);
                ll_city_list_teacher.setVisibility(View.GONE);
            }
        });
        //setGpsCity(HaojiajiaoApplication.city);

    }

    private void initTabTeacher3() {
        et_TeacherTab3Add = (ImageView) findViewById(R.id.et_TeacherTab3Add);
        et_TeacherTab3Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseDialog courseDialog = new CourseDialog(MyDataActivity.this, new CourseLisenner() {
                    @Override
                    public void sendList(Course1Model course1Model) {
                    }
                });
                courseDialog.setGetPositionListener(new CourseDialog.GetPositionListener() {

                    @Override
                    public void onPosition(int fristPosition) {
                        //获得大类和小类
                        bigLei = Course1Adapter.testCourse[fristPosition];
                        int temp = positionList.size();
                        boolean isHave = false;
                        for (int i = 0; i < temp; i++) {
                            if (positionList.get(i).equals(fristPosition + "" )) {
                                isHave = true;
                                break;
                            }
                        }
                        if (isHave == false) {
                            positionList.add(fristPosition + "" );
                            CourseModel courseModel = new CourseModel();
                            courseModel.tv_classJX = bigLei;
                            courseModel.tv_class = Course1Adapter.testCourse1[fristPosition];
                            models.add(courseModel);
                            if (isFrist) {
                                isFrist = false;
                                courseAdapter = new CourseAdapter(models, MyDataActivity.this);
                                courseAdapter.setWhitchWillBeDel(new CourseAdapter.WhitchWillBeDel() {
                                    @Override
                                    public void onPosition(int Position) {
                                        //哪个Position将要被清除
                                        models.remove(Position);
                                        courseAdapter.notifyDataSetChanged();
                                    }
                                });
                                et_TeacherTab3List.setAdapter(courseAdapter);
                            } else {
                                courseAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
                courseDialog.show();
            }
        });
        et_TeacherTab3List = (ListView) findViewById(R.id.et_TeacherTab3List);
        et_TeacherTab3List.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sv_tab1.requestDisallowInterceptTouchEvent(false);
                } else {
                    sv_tab1.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        et_TeacherTab3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new CourseDialog(MyDataActivity.this, new CourseLisenner() {
                    @Override
                    public void sendList(Course1Model course1Model) {

                    }
                }).show();
            }
        });
        et_TeacherTab3List.setAdapter(courseListAdapter = new CourseListAdapter(MyDataActivity.this));
        ArrayList<String> strings = new ArrayList<>();
        courseListAdapter.setData(strings);

    }





    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MyDataActivity.class.getCanonicalName().equals(intent.getAction())) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void setGpsCity(String city) {
        if (tv_city_gps != null) {
            if (city != null) {
                tv_city_gps.setText("Current location city:：" + city);
            } else {
                tv_city_gps.setText("Location information can not be obtained!");

            }

        }

    }

    public void getParentInfoByUserName() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("userName", HaojiajiaoApplication.userName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getParentInfoByUserName, stringMap, RequestTag.getParentInfoByUserName);
    }

    public void getTeacherInfoByUserName() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("TeacherUserName", HaojiajiaoApplication.userName);
//        stringMap.put("TeacherUserName", "fsdf@qq.com");
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherInfoByUserName, stringMap, RequestTag.getTeacherInfoByUserName);
    }

    /*public void updateParent() {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("ParentUserName", HaojiajiaoApplication.userName);
        ifStrTrue(dataParas, "ParentName", et_parentTab2Name);
        ifStrTrue(dataParas, "ParentGender", et_parentTab2Sax);
        ifStrTrue(dataParas, "ParentEmail", et_parentTab2Email);
        ifStrTrue(dataParas, "ParentTel", et_parentTab2Phone);
        ifStrTrue(dataParas, "StudentName", et_ChildTab2Name);
        if (et_ChildTab2City.getText().toString() != null && et_ChildTab2City.getText().toString().length() > 0) {
            dataParas.put("StudentName", et_ChildTab2City.getText().toString());
        }
        ifStrTrue(dataParas, "StudentAge", et_ChildTab2Age);
        ifStrTrue(dataParas, "StudentGender", et_ChildTab2Sax);
        ifStrTrue(dataParas, "StudentEmail", et_ChildTab2Email);
        requestHandler.sendHttpRequestWithParamByGet(GoodTeacherURL.updateParent, dataParas, RequestTag.updateParent);
    }*/

    private boolean ifCompleteTeacherInfo1(){
        if ((et_TeacherTab2Name == null || (et_TeacherTab2Name.getText().toString().equals("")))
                || (et_TeacherTab2Phone == null || (et_TeacherTab2Phone.getText().toString().equals("")))
                || (et_TeacherTab2Age == null || (et_TeacherTab2Age.getText().toString().equals("")))
                || (et_TeacherTab2City.getText().toString() == null || (et_TeacherTab2City.getText().toString().equals("")))
                || (et_TeacherTab2ZPay.getText().toString() == null || (et_TeacherTab2ZPay.getText().toString().equals("")))
                || (et_TeacherTab2Sax.getText().toString() == null || (et_TeacherTab2Sax.getText().toString().equals("")))
                || (et_TeacherTab2Sulf == null || (et_TeacherTab2Sulf.getText().toString().equals("")) || (et_TeacherTab2Sulf.getText().toString().equals("Please write down your past learning and education experience!")))){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean ifCompleteTeacherInfo2(){
        String str = et_money.getText().toString();
        if ((str == null || (str.equals("")))
                || (models == null || (models.size()==0))){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean ifCompleteParentInfo(){
        if ((et_parentTab2Name == null || (et_parentTab2Name.getText().toString().equals("")))
                || (et_parentTab2Phone == null || (et_parentTab2Phone.getText().toString().equals("")))
                || (et_ChildTab2Name == null || (et_ChildTab2Name.getText().toString().equals("")))
                || (et_parentTab2Sax == null || (et_parentTab2Sax.getText().toString().equals("")))
                || (et_ChildTab2Email == null || (et_ChildTab2Email.getText().toString().equals("")))
                || (et_ChildTab2Sax == null || (et_ChildTab2Sax.getText().toString().equals("")))
                || (et_ChildTab2City.getText().toString() == null || (et_ChildTab2City.getText().toString().equals("")))
                || (et_ChildTab2Age == null || (et_ChildTab2Age.getText().toString().equals("")))){
            return false;
        }
        else{
            return true;
        }
    }

    public void updateParent() {
        if(ifCompleteParentInfo()){
            String uu = GoodTeacherURL.updateParent + "?";
            uu = uu + "ParentUserName=" + HaojiajiaoApplication.userName.replace(" ","+") + "&";
            uu = uu + "ParentName=" + et_parentTab2Name.getText().toString().replace(" ","+") + "&";
            uu = uu + "ParentGender=" + et_parentTab2Sax.getText().toString().replace(" ","+") + "&";
            uu = uu + "ParentTel=" + et_parentTab2Phone.getText().toString().replace(" ","+") + "&";
            uu = uu + "StudentName=" + et_ChildTab2Name.getText().toString().replace(" ","+") + "&";
            uu = uu + "StudentCity=" + et_ChildTab2City.getText().toString().replace(" ","+") + "&";
            uu = uu + "StudentAge=" + et_ChildTab2Age.getText().toString().replace(" ","+") + "&";
            uu = uu + "StudentGender=" + et_ChildTab2Sax.getText().toString().replace(" ","+") + "&";
            uu = uu + "StudentEmail=" + et_ChildTab2Email.getText().toString().replace(" ","+");
            URL url =null;
            try{
                url = new URL(uu);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            sendRequestWithHttpURLConnection(url);
        }
        else{
            ToastUtil.showShort(MyDataActivity.this, "You cannot leave required fields blank.");
        }

    }

    public void updateTeacher() {
        if(ifCompleteTeacherInfo1() && ifCompleteTeacherInfo2()){
            String uu = GoodTeacherURL.updateTeacher + "?";
            uu = uu + "TeacherUserName=" + HaojiajiaoApplication.userName.replace(" ","+") + "&";
            uu = uu + "TeacherGender=" + et_TeacherTab2Sax.getText().toString().replace(" ","+") + "&";
            uu = uu + "TeacherTel=" + et_TeacherTab2Phone.getText().toString().replace(" ","+") + "&";
            uu = uu + "TeacherAge=" + et_TeacherTab2Age.getText().toString().replace(" ","+") + "&";
            uu = uu + "TeacherCity=" + et_TeacherTab2City.getText().toString().replace(" ","+") + "&";
            uu = uu + "TeacherPaypalAccount=" + et_TeacherTab2ZPay.getText().toString().replace(" ","+") + "&";
            uu = uu + "TeacherSelfCv=" + et_TeacherTab2Sulf.getText().toString().replace(" ","+") + "&";
            uu = uu + "TeacherName=" + et_TeacherTab2Name.getText().toString().replace(" ","+") + "&";
            uu = uu + "TeacherCharge=" + et_money.getText().toString().replace(" ","+") + "&";
            uu = uu + "TeacherLesson=" + getCourseModel(models).replace(" ","+");
            //dataParas.put("TeacherLesson", getCourseModel(models));
            Log.e("MyDataAct","!!!!!!!!!course:"+getCourseModel(models));
            URL url =null;
            try{
                url = new URL(uu);
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            sendRequestWithHttpURLConnection(url);
        }
        else{
            ToastUtil.showShort(MyDataActivity.this, "You cannot leave required fields blank.");
        }

    }

    private void sendRequestWithHttpURLConnection(final URL url) {
        Log.e("MYDATAact","url: "+url);
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    //URL url = new URL("http://121.42.140.239/hjj/php/getParentToken?parentUserName=pe@qq.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    message.what = GET_TOKEN;
                    String jsondata = response.toString();
                    String result = null;
                    jsondata = jsondata.replace("\\", "").replace("u003d", "=");
                    try {
                        JSONObject total1 = new JSONObject(jsondata);
                        result = total1.optString("result");
                        if(result.equals("1")){
                            Log.e("MyDataAct:"," update info by sendRequestWithHttpURLConnection success!");
                        }
                        else{
                            Log.e("MyDataAct:"," update info by sendRequestWithHttpURLConnection failed!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MyDataAct:","update info by sendRequestWithHttpURLConnection~ error!");
                    }
                    // 将服务器返回的结果存放到Message中
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_TOKEN:
                    String response = (String) msg.obj;
                    Log.e("MyDataAct:"," update info by sendRequestWithHttpURLConnection: "+response);
                    ToastUtil.showLong(MyDataActivity.this, "Successful modification");
                    if(HaojiajiaoApplication.ISSTATE){
                        RongIM.getInstance().refreshUserInfoCache(
                                new UserInfo(et_TeacherTab2Email.getText().toString(),
                                        et_TeacherTab2Name.getText().toString(),
                                        Uri.parse(HaojiajiaoApplication.picUrl)));
                    }
                    else{
                        RongIM.getInstance().refreshUserInfoCache(
                                new UserInfo(et_parentTab2Email.getText().toString(),
                                        et_parentTab2Name.getText().toString(),
                                        Uri.parse(HaojiajiaoApplication.picUrl)));
                    }
                    // 在这里进行UI操作，将结果显示到界面上
                    /*Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);*/
                    finish();
                    break;
            }
        }

    };

    public static String getCourseModel(List<CourseModel> user) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < user.size(); i++) {
            stringBuffer.append(user.get(i).tv_class);// + user.get(i).tv_subjectJX
            if (i != user.size() - 1) {
                stringBuffer.append("-");
            }
        }
        return stringBuffer.toString();
    }

    private void ifStrTrue(Map<String, String> dataParas, String type, EditText string) {
        String str = string.getText().toString();
        if (str != null && str.length() > 0) {
            dataParas.put(type, str);
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
        if (response.requestTag.toString().equals("getTeacherInfoByUserName")) {
            String dataStr = response.responseString;
            Log.d("data", dataStr);
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);

                id = total1.getInt("id");
                setEditString(et_TeacherTab2Name, total1.optString("teacherName"));
                setEditString(et_TeacherTab2Sax, total1.optString("teacherGender"));
                setEditString(et_TeacherTab2Email, total1.optString("teacherEmail"));
                setEditString(et_TeacherTab2Phone, total1.optString("teacherTel"));
                et_TeacherTab2City.setText(total1.optString("teacherCity") == null ? "" : total1.optString("teacherCity"));
                setEditString(et_TeacherTab2ZPay, total1.optString("teacherPaypalAccount"));
                setEditString(et_TeacherTab2Sulf, total1.optString("teacherSelfCv"));
                setEditString(et_money, total1.optString("teacherCharge"));
                setEditString(et_TeacherTab2Age,total1.optString("teacherAge"));
                lessons = total1.optString("teacherLesson");
                Log.e("MyDataAct","init lessons: "+lessons);
                if (lessons != null && lessons.length() > 2) {
                    Log.e("MyDataAct","lesson != null");
                    if (lessons.indexOf("-") != -1) {
                        Log.e("MyDataAct","lesson.indexOf(-)!=-1");
                        String[] res = lessons.split("-");
                        StringBuffer stringBuffer1 = new StringBuffer();
                        for (int i = 0; i < res.length; i++) {
                            Log.e("MyDataAct","chuli lessons: "+res[i]);
                            CourseModel courseModel = new CourseModel();
                            for (int b = 0; b < Course1Adapter.testCourse1.length; b++) {
                                if (Course1Adapter.testCourse1[b].equals(res[i])) {
                                    courseModel.tv_classJX = Course1Adapter.testCourse[b];
                                    courseModel.tv_class = Course1Adapter.testCourse1[b];
                                }

                            }
                            /*for (int b = 0; b < Course2Adapter.testCourseType.length; b++) {
                                if (Course2Adapter.testCourseType[b].equals(res[i].substring(res[i].length() - 2, res[i].length()))) {
                                    courseModel.tv_subjectJX = Course2Adapter.testCourseType[b];
                                    courseModel.tv_subject = Course2Adapter.testCourse[b];
                                }
                            }*/
                            models.add(courseModel);
                        }
                    } else {
                        CourseModel courseModel = new CourseModel();
                        for (int b = 0; b < Course1Adapter.testCourse1.length; b++) {
                            if (Course1Adapter.testCourse1[b].equals(lessons)) {
                                courseModel.tv_classJX = Course1Adapter.testCourse[b];
                                courseModel.tv_class = Course1Adapter.testCourse1[b];
                            }
                        }
                        /*for (int b = 0; b < Course2Adapter.testCourseType.length; b++) {
                            if (Course2Adapter.testCourseType[b].equals(lessons.substring(lessons.length() - 2, lessons.length()))) {
                                courseModel.tv_subjectJX = Course2Adapter.testCourseType[b];
                                courseModel.tv_subject = Course2Adapter.testCourse[b];
                            }
                        }*/
                        models.add(courseModel);
                    }
                    courseAdapter = new CourseAdapter(models, MyDataActivity.this);
                    et_TeacherTab3List.setAdapter(courseAdapter);
                    courseAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (response.requestTag.toString().equals("getParentInfoByUserName")) {
            String dataStr = response.responseString;
            Log.d("data", dataStr);
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                JSONObject jsonArray = total1.getJSONObject("result");
                id = jsonArray.getInt("id");
                setEditString(et_parentTab2Name, jsonArray.optString("parentName"));
                setEditString(et_parentTab2Sax, jsonArray.optString("parentGender"));
                setEditString(et_parentTab2Email, jsonArray.optString("parentUserName"));
                setEditString(et_parentTab2Phone, jsonArray.optString("parentTel"));
                et_ChildTab2City.setText(jsonArray.optString("studentCity") == null ? "" : jsonArray.optString("studentCity"));
                setEditString(et_ChildTab2Name, jsonArray.optString("studentName"));
                setEditString(et_ChildTab2Email, jsonArray.optString("studentEmail"));
                setEditString(et_ChildTab2Sax, jsonArray.optString("studentGender"));
                setEditString(et_ChildTab2Age, jsonArray.optString("studentAge"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (response.requestTag.toString().equals("updateParent")) {
            String status = response.responseStatus + "";
            if (status.equals("1")) {
                ToastUtil.showLong(MyDataActivity.this, "Successful modification!");
                RongIM.getInstance().refreshUserInfoCache(
                        new UserInfo(et_TeacherTab2Email.getText().toString(),
                                et_TeacherTab2Name.getText().toString(),
                                Uri.parse(HaojiajiaoApplication.picUrl)));
            } else {
                ToastUtil.showLong(MyDataActivity.this, "Modify failed");
            }
        } else if (response.requestTag.toString().equals("updateTeacher")) {

            String status = response.responseStatus + "";
            if (status.equals("1")) {
                ToastUtil.showLong(MyDataActivity.this, "Successful modification");
                RongIM.getInstance().refreshUserInfoCache(
                        new UserInfo(et_parentTab2Email.getText().toString(),
                                et_parentTab2Name.getText().toString(),
                                Uri.parse(HaojiajiaoApplication.picUrl)));
            } else {
                ToastUtil.showLong(MyDataActivity.this, "Modify failed");
            }
        }
    }

    public static String getCourse1(String[] testCourse, String[] strs, String str) {

        if (str != null) {
            for (int i = 0; i < strs.length; i++) {
                if (strs[i].equals(str.substring(0, 2))) {
                    return testCourse[i];
                }
            }
        } else {
            return "";
        }

        return "";

    }

    public static String getCourse2(String[] testCourse, String[] strs, String str) {
        if (str != null) {
            for (int i = 0; i < strs.length; i++) {
                if (strs[i].equals(str.substring(str.length() - 2, str.length()))) {
                    return testCourse[i];
                }
            }
        } else {
            return "";
        }

        return "";
    }

    private void setEditString(EditText editString, String str) {
        editString.setText(str);
    }
}

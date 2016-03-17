package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.CityAdapter;
import com.shanghai.haojiajiao.adapter.Course1Adapter;
import com.shanghai.haojiajiao.adapter.CourseAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.model.CityModel;
import com.shanghai.haojiajiao.model.Course1Model;
import com.shanghai.haojiajiao.model.CourseModel;
import com.shanghai.haojiajiao.util.BitmapUtil;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.ToastUtil;
import com.shanghai.haojiajiao.weight.CourseDialog;
import com.shanghai.haojiajiao.weight.CourseLisenner;
import com.shanghai.haojiajiao.weight.ImgSwitchDialog;
import com.shanghai.haojiajiao.weight.LoadingDialog;
import com.shanghai.haojiajiao.weight.MsgDialog;
import com.shanghai.haojiajiao.weight.MsgLisenner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    //tab
    private LinearLayout ll_tab1, ll_parent_tab2, ll_tab_end, ll_tab_teacher2, ll_tab_teacher3;
    private ScrollView sv_parent_tab2,sv_tab_teacher2;
    //tab1
    private EditText et_tab1_userEmail, et_tab1_userPassword,et_tab1_userPassword1;
    //tab2Parent
    private EditText et_parentTab2Name, et_parentTab2Email, et_parentTab2Phone;
    private RadioGroup et_parentTab2Sax, et_ChildTab2Sax;
    private EditText et_ChildTab2Name, et_ChildTab2Age, et_ChildTab2Email;
    private TextView et_ChildTab2City;
    private LinearLayout ll_city_listShow;
    //城市
    private int pCityPos = 0;
    private int tCityPos = 0;
    private LinearLayout ll_city_list;
    private ListView lv_city;
    private CityAdapter cityAdapter;
    private TextView tv_city_gps;
    private LinearLayout ll_city_list_teacher;
    private ListView lv_city_teacher;
    private CityAdapter cityAdapter1;
    private TextView tv_city_gps_teacher;
    //tab2Teacher
    private EditText et_TeacherTab2Name, et_TeacherTab2Age, et_TeacherTab2Email, et_TeacherTab2Phone, et_TeacherTab2ZPay, et_TeacherTab2Sulf;
    private RadioGroup et_TeacherTab2Sax;
    private TextView et_TeacherTab2City;
    private LinearLayout ll_teacher_city;
    //tab3Teacher
    private ImageView et_TeacherTab3Add;
    private ListView et_TeacherTab3List;
    //    private CourseListAdapter courseListAdapter;
    //tabEnd
    private ImageView iv_userIcon;
    //other
    private ImageView iv_back;
    private TextView tv_signUp;
    private String email = null;
    private String password = null;
    private String picPath = null;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;
    private String bigLei = "";//大类,比方说  高年级 低年级
    private String smallLei = "";//小类,各种学科
    private CourseAdapter courseAdapter;
    List<CourseModel> models = new ArrayList<>();
    List<String> positionList = new ArrayList<>();
    private boolean isFrist = true;
    private EditText et_money;
    public boolean ifHasIMG = false;
    private boolean isTeacherSexSelcted = false;
    private boolean isParentSexSelcted = false;
    private boolean isChildSexSelcted = false;

    public static final int GET_TOKEN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        receiver = new FinishReceiver();
        loadingDialog = new LoadingDialog(SignUpActivity.this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SignUpActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        initView();
        initTab();
        initTab1();
        initTabPrarent2();
        initTabTeacher2();
        initTabTeacher3();
        initTabEnd();
        initFlow();


    }

    @Override
    public void onBackPressed() {
        backFlow();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                backFlow();
                break;
            case R.id.tv_signUp:
                Log.e("signup",HaojiajiaoApplication.ISSTATE?"Tea":"par");
                nextFlow();
                break;
        }
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(this);
    }

    private void initTab() {
        ll_tab1 = (LinearLayout) findViewById(R.id.ll_tab1);
        ll_parent_tab2 = (LinearLayout) findViewById(R.id.ll_parent_tab2);
        sv_parent_tab2 = (ScrollView) findViewById(R.id.sv_parent_tab2);
        ll_tab_end = (LinearLayout) findViewById(R.id.ll_tab_end);
        ll_tab_teacher2 = (LinearLayout) findViewById(R.id.ll_tab_teacher2);
        sv_tab_teacher2 = (ScrollView) findViewById(R.id.sv_tab_teacher2);
        ll_tab_teacher3 = (LinearLayout) findViewById(R.id.ll_tab_teacher3);

    }

    private void initTab1() {
        et_tab1_userEmail = (EditText) findViewById(R.id.et_tab1_userEmail);
        et_tab1_userPassword = (EditText) findViewById(R.id.et_tab1_userPassword);
        et_tab1_userPassword1 = (EditText) findViewById(R.id.et_tab1_userPassword1);
    }

    private void initTabPrarent2() {
        //parent
        et_parentTab2Name = (EditText) findViewById(R.id.et_parentTab2Name);
        et_parentTab2Sax = (RadioGroup) findViewById(R.id.et_parentTab2Sax);
        et_parentTab2Sax.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isParentSexSelcted = true;
            }
        });
        et_parentTab2Email = (EditText) findViewById(R.id.et_parentTab2Email);
        //email = getIntent().getStringExtra("email");
        et_parentTab2Phone = (EditText) findViewById(R.id.et_parentTab2Phone);
        //child
        et_ChildTab2Name = (EditText) findViewById(R.id.et_ChildTab2Name);
        et_ChildTab2City = (TextView) findViewById(R.id.et_ChildTab2City);
        ll_city_listShow = (LinearLayout) findViewById(R.id.ll_city_listShow);
        ll_city_listShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_city_list.getVisibility() == View.GONE) {
                    ll_city_list.setVisibility(View.VISIBLE);
                    cityAdapter1.clearData();
                    ArrayList<CityModel> cityModels = new ArrayList();
                    for (int i = 0; i < 7; i++) {
                        CityModel cityModel = new CityModel();
                        cityModel.setCatchCity(i == pCityPos);
                        cityModels.add(cityModel);
                    }
                    cityAdapter1.setData(cityModels);
                } else {
                    ll_city_list.setVisibility(View.GONE);
                }
            }
        });
        et_ChildTab2Sax = (RadioGroup) findViewById(R.id.et_ChildTab2Sax);
        et_ChildTab2Sax.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isChildSexSelcted = true;
            }
        });
        et_ChildTab2Age = (EditText) findViewById(R.id.et_ChildTab2Age);
        et_ChildTab2Email = (EditText) findViewById(R.id.et_ChildTab2Email);
        ll_city_list = (LinearLayout) findViewById(R.id.ll_city_list);
       // tv_city_gps = (TextView) findViewById(R.id.tv_city_gps);
        lv_city = (ListView) findViewById(R.id.lv_city_parent);
        lv_city.setAdapter(cityAdapter1 = new CityAdapter(SignUpActivity.this));
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pCityPos = position;
                et_ChildTab2City.setText(CityAdapter.testCity[position]);
                ll_city_list.setVisibility(View.GONE);
            }
        });
        //setGpsCity(HaojiajiaoApplication.city);
    }

    private void initTabTeacher2() {
        et_TeacherTab2Name = (EditText) findViewById(R.id.et_TeacherTab2Name);
        et_TeacherTab2Sax = (RadioGroup) findViewById(R.id.et_TeacherTab2Sax);
        et_TeacherTab2Sax.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isTeacherSexSelcted = true;
            }
        });
        et_TeacherTab2Age = (EditText) findViewById(R.id.et_TeacherTab2Age);
        et_TeacherTab2Email = (EditText) findViewById(R.id.et_TeacherTab2Email);
        //email = getIntent().getStringExtra("email");
        et_TeacherTab2Phone = (EditText) findViewById(R.id.et_TeacherTab2Phone);
        et_TeacherTab2City = (TextView) findViewById(R.id.et_TeacherTab2City);
        et_TeacherTab2ZPay = (EditText) findViewById(R.id.et_TeacherTab2ZPay);
        et_TeacherTab2Sulf = (EditText) findViewById(R.id.et_TeacherTab2Sulf);
        ll_teacher_city = (LinearLayout) findViewById(R.id.ll_teacher_city);
        ll_teacher_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_city_list_teacher.getVisibility() == View.GONE) {
                    ll_city_list_teacher.setVisibility(View.VISIBLE);
                    cityAdapter.clearData();
                    ArrayList<CityModel> cityModels = new ArrayList<CityModel>();
                    for (int i = 0; i < 7; i++) { //~~~~~~~~~~~~~~~~~~~~ 6 - 7 by husai 2016.2.29
                        CityModel cityModel = new CityModel();
                        cityModel.setCatchCity(i == tCityPos);
                        cityModels.add(cityModel);
                    }
                    cityAdapter.setData(cityModels);
                } else {
                    ll_city_list_teacher.setVisibility(View.GONE);
                }
            }
        });
        ll_city_list_teacher = (LinearLayout) findViewById(R.id.ll_city_list_teacher);
        tv_city_gps_teacher = (TextView) findViewById(R.id.tv_city_gps_teacher);
        lv_city_teacher = (ListView) findViewById(R.id.lv_city_teacher);
        lv_city_teacher.setAdapter(cityAdapter = new CityAdapter(SignUpActivity.this));
        lv_city_teacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tCityPos = position;
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
                CourseDialog courseDialog = new CourseDialog(SignUpActivity.this, new CourseLisenner() {
                    @Override
                    public void sendList(Course1Model course1Model) {
                        Course1Model course1Model1 = course1Model;

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
                            if (positionList.get(i).equals(fristPosition + "")) {
                                isHave = true;
                                break;
                            }
                        }
                        if (isHave == false) {
                            positionList.add(fristPosition + "" );
                            CourseModel courseModel = new CourseModel();
                            courseModel.tv_class = bigLei;
                            courseModel.tv_classJX = Course1Adapter.testCourse1[fristPosition];
                            models.add(courseModel);
                            if (isFrist) {
                                isFrist = false;
                                courseAdapter = new CourseAdapter(models, SignUpActivity.this);
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
        et_TeacherTab3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new CourseDialog(SignUpActivity.this, new CourseLisenner() {
                    @Override
                    public void sendList(Course1Model course1Model) {

                    }
                }).show();
            }
        });
//        et_TeacherTab3List.setAdapter(courseListAdapter = new CourseListAdapter(SignUpActivity.this));
//        ArrayList<String> strings = new ArrayList<String>();
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        courseListAdapter.setData(strings);
    }

// initial code
/*    private void initTabTeacher3() {
        et_TeacherTab3Add = (ImageView) findViewById(R.id.et_TeacherTab3Add);
        et_TeacherTab3Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseDialog courseDialog = new CourseDialog(SignUpActivity.this, new CourseLisenner() {
                    @Override
                    public void sendList(Course1Model course1Model, Course2Model course2Model) {
                        Course1Model course1Model1 = course1Model;
                        Course2Model course2Model1 = course2Model;

                    }
                });
                courseDialog.setGetPositionListener(new CourseDialog.GetPositionListener() {

                    @Override
                    public void onPosition(int fristPosition, int secondPosition) {
                        //获得大类和小类
                        bigLei = Course1Adapter.testCourse[fristPosition];
                        smallLei = Course2Adapter.testCourse[secondPosition];
                        int temp = positionList.size();
                        boolean isHave = false;
                        for (int i = 0; i < temp; i++) {
                            if (positionList.get(i).equals(fristPosition + "" + secondPosition + "")) {
                                isHave = true;
                                break;
                            }
                        }
                        if (isHave == false) {
                            positionList.add(fristPosition + "" + secondPosition + "");
                            CourseModel courseModel = new CourseModel();
                            courseModel.tv_class = bigLei;
                            courseModel.tv_classJX = Course1Adapter.testCourse1[fristPosition];
                            courseModel.tv_subject = Course2Adapter.testCourse[secondPosition];
                            courseModel.tv_subjectJX = Course2Adapter.testCourseType[secondPosition];
                            models.add(courseModel);
                            if (isFrist) {
                                isFrist = false;
                                courseAdapter = new CourseAdapter(models, SignUpActivity.this);
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
        et_TeacherTab3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new CourseDialog(SignUpActivity.this, new CourseLisenner() {
                    @Override
                    public void sendList(Course1Model course1Model, Course2Model course2Model) {

                    }
                }).show();
            }
        });
//        et_TeacherTab3List.setAdapter(courseListAdapter = new CourseListAdapter(SignUpActivity.this));
//        ArrayList<String> strings = new ArrayList<String>();
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        strings.add("");
//        courseListAdapter.setData(strings);
    }*/


    public static String getCourseModel(List<CourseModel> user) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < user.size(); i++) {
            stringBuffer.append(user.get(i).tv_classJX + user.get(i).tv_subjectJX);
            if (i != user.size() - 1) {
                stringBuffer.append("-");
            }
        }
        return stringBuffer.toString();
    }

    private void initTabEnd() {
        iv_userIcon = (ImageView) findViewById(R.id.iv_userIcon);
        iv_userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImgSwitchDialog(SignUpActivity.this, new ImgSwitchDialog.ImgSwitchLisenner() {
                    @Override
                    public void album() {
                        BitmapUtil.doPickPhotoFromGallery(SignUpActivity.this);
                    }

                    @Override
                    public void cam() {
                        BitmapUtil.doTakePhoto(SignUpActivity.this);
                    }
                }).show();
            }
        });
    }

    private void initFlow() {
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        if (email != null) {
            et_tab1_userEmail.setText(email);
        }
//        if (password != null) {
//            et_tab1_userPassword.setText(MD5(password));
//        }
    }


    //MD5加密，32位
    public static String MD5(String str){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for(int i = 0; i < charArray.length; i++){
            byteArray[i] = (byte)charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for( int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int)md5Bytes[i])&0xff;
            if(val < 16)
            {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    public void checkRegister(String email) {
        Map<String, String> dataParas = new HashMap<String, String>();
        dataParas.put("userName", email);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.checkRegister, dataParas, RequestTag.checkRegister);
    }

    //    public void parentRegister(String email) {
//        Map<String, String> dataParas = new HashMap<>();
//        dataParas.put("userName", email);
//        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.parentRegister, dataParas, RequestTag.parentRegister);
//    }
    //用户民  手机号码  邮箱             老师 课程必须设置
    private void teacherRegister() {

        Map<String, String> dataParas = new HashMap<String, String>();
        if (et_tab1_userEmail != null && (!et_tab1_userEmail.equals(""))) {
            ifStrTrue(dataParas, "TeacherUserName", et_tab1_userEmail);
        } else {
            ToastUtil.showShort(SignUpActivity.this, "teacher's name cannot be empty!");
            return;
        }
        if (et_tab1_userEmail != null && et_tab1_userEmail.getText().toString().length() > 4) {
            ifStrTrue(dataParas, "TeacherEmail", et_TeacherTab2Email);
        } else {
            ToastUtil.showShort(SignUpActivity.this, "teacher's mailbox cannot be empty!");
            return;
        }
        RadioButton userSax = (RadioButton) findViewById(et_TeacherTab2Sax.getCheckedRadioButtonId());
        dataParas.put("TeacherGender", userSax.getText().toString());
        password = MD5(et_tab1_userPassword.getText().toString());
        dataParas.put("TeacherPassword",password);
        //ifStrTrue(dataParas, "TeacherPassword", password);
        if (et_TeacherTab2Phone != null && (!et_TeacherTab2Phone.equals(""))) {
            ifStrTrue(dataParas, "TeacherTel", et_TeacherTab2Phone);
        } else {
            ToastUtil.showShort(SignUpActivity.this, "teacher's cell phone number cannot be empty!");
            return;
        }
        ifStrTrue(dataParas, "TeacherAge", et_TeacherTab2Age);
        if (et_TeacherTab2City.getText().toString() != null && et_TeacherTab2City.getText().toString().length() > 0) {
            dataParas.put("TeacherCity", et_TeacherTab2City.getText().toString().replace(" ","+"));
        }
        ifStrTrue(dataParas, "TeacherPaypalAccount", et_TeacherTab2ZPay);
        ifStrTrue(dataParas, "TeacherSelfCv", et_TeacherTab2Sulf);
        ifStrTrue(dataParas, "TeacherName", et_TeacherTab2Name);
        String str = ((EditText) findViewById(R.id.et_money)).getText().toString();
        if (str != null) {
            dataParas.put("TeacherCharge", str.replace(" ","+"));
        }
        if (models != null && models.size() > 0) {
            dataParas.put("TeacherLesson", getCourseModel(models).replace(" ","+"));
        }
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.teacherRegister, dataParas, RequestTag.teacherRegister);

    }

    private void parentRegister() {
        Map<String, String> dataParas = new HashMap<String, String>();
        if (et_parentTab2Name != null && (!et_parentTab2Name.equals(""))) {
            ifStrTrue(dataParas, "ParentName", et_parentTab2Name);
        } else {
            ToastUtil.showShort(SignUpActivity.this, "your name cannot be empty!");
            return;
        }
        if (et_tab1_userEmail != null && (!et_tab1_userEmail.equals(""))) {
            ifStrTrue(dataParas, "ParentUserName", et_tab1_userEmail);
        } else {
            ToastUtil.showShort(SignUpActivity.this, "your mailbox cannot be empty!");
            return;
        }

        password = MD5(et_tab1_userPassword.getText().toString());
        dataParas.put("ParentPassword",password);
        //ifStrTrue(dataParas, "ParentPassword", et_tab1_userPassword);

        RadioButton userSax = (RadioButton) findViewById(et_parentTab2Sax.getCheckedRadioButtonId());
        dataParas.put("ParentGender", userSax.getText().toString().replace(" ","+"));
        ifStrTrue(dataParas, "ParentEmail", et_parentTab2Email);
        if (et_parentTab2Phone != null && (!et_parentTab2Phone.equals(""))) {
            ifStrTrue(dataParas, "ParentTel", et_parentTab2Phone);
        } else {
            ToastUtil.showShort(SignUpActivity.this, "your cell phone number cannot be empty!");
            return;
        }
        ifStrTrue(dataParas, "StudentName", et_ChildTab2Name);
        if (et_ChildTab2City.getText().toString() != null && et_ChildTab2City.getText().toString().length() > 0) {
            dataParas.put("StudentCity", et_ChildTab2City.getText().toString().replace(" ","+"));
        }
        RadioButton userSax1 = (RadioButton) findViewById(et_ChildTab2Sax.getCheckedRadioButtonId());
        dataParas.put("StudentGender", userSax1.getText().toString().replace(" ","+"));
        ifStrTrue(dataParas, "StudentAge", et_ChildTab2Age);
        ifStrTrue(dataParas, "StudentEmail", et_ChildTab2Email);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.parentRegister, dataParas, RequestTag.parentRegister);
    }

    private void ifStrTrue(Map<String, String> dataParas, String type, EditText string) {
        String str = string.getText().toString();
        if (str != null && str.length() > 0) {
            dataParas.put(type, str.replace(" ","+"));
        }
    }

    private boolean ifCompleteTeacherInfo1(){
        if ((et_TeacherTab2Name == null || (et_TeacherTab2Name.getText().toString().equals("")))
                || (et_tab1_userEmail == null || (et_tab1_userEmail.getText().toString().equals("")))
                || (et_tab1_userPassword == null || (et_tab1_userPassword.getText().toString().equals("")))
                || (et_TeacherTab2Phone == null || (et_TeacherTab2Phone.getText().toString().equals("")))
                || (et_TeacherTab2Age == null || (et_TeacherTab2Age.getText().toString().equals("")))
                || (et_TeacherTab2City.getText().toString() == null || (et_TeacherTab2City.getText().toString().equals("")))
                || (et_TeacherTab2Sulf == null || (et_TeacherTab2Sulf.getText().toString().equals("")) || (et_TeacherTab2Sulf.getText().toString().equals("Please write down your past learning and education experience!")))){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean ifCompleteTeacherInfo2(){
        String str = ((EditText) findViewById(R.id.et_money)).getText().toString();
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
                || (et_tab1_userEmail == null || (et_tab1_userEmail.getText().toString().equals("")))
                || (et_tab1_userPassword == null || (et_tab1_userPassword.getText().toString().equals("")))
                || (et_parentTab2Phone == null || (et_parentTab2Phone.getText().toString().equals("")))
                || (et_ChildTab2Name == null || (et_ChildTab2Name.getText().toString().equals("")))
                || (et_ChildTab2City.getText().toString() == null || (et_ChildTab2City.getText().toString().equals("")))
                || (et_ChildTab2Age == null || (et_ChildTab2Age.getText().toString().equals("")))){
            return false;
        }
        else{
            return true;
        }
    }

    private void nextFlow() {
        Log.e("Signup",HaojiajiaoApplication.ISSTATE?"Tea":"Par");
        if (!HaojiajiaoApplication.ISSTATE) {
            if (ll_tab1.getVisibility() == View.VISIBLE) {
                if(et_tab1_userEmail.getText().toString().equals("")
                        || et_tab1_userPassword.getText().toString().equals("")
                        || et_tab1_userPassword1.getText().toString().equals("")) {
                    ToastUtil.showShort(SignUpActivity.this, "You cannot leave required fields blank.");
                }else if(et_tab1_userPassword.getText() == null || et_tab1_userPassword.getText().length() <6){
                    ToastUtil.showShort(SignUpActivity.this, "Your password must be at least 6 characters.");
                }else if(!et_tab1_userPassword.getText().toString().equals(et_tab1_userPassword1.getText().toString())){
                    ToastUtil.showShort(SignUpActivity.this, "Please input same password.");
                }else if (et_tab1_userEmail.getText() != null && et_tab1_userEmail.getText().length() > 0) {
                    checkRegister(et_tab1_userEmail.getText().toString());
                    //ll_tab1.setVisibility(View.GONE);
                    //ll_parent_tab2.setVisibility(View.VISIBLE);
                }
            } else if (sv_parent_tab2.getVisibility() == View.VISIBLE) {
                if(ifCompleteParentInfo() && isParentSexSelcted && isChildSexSelcted){
                    sv_parent_tab2.setVisibility(View.GONE);
                    ll_tab_end.setVisibility(View.VISIBLE);
                    tv_signUp.setText("SUBMIT");
                }
                else{
                    ToastUtil.showShort(SignUpActivity.this, "You cannot leave required fields blank.");
                }
            } else if (ll_tab_end.getVisibility() == View.VISIBLE) {
                parentRegister();
                //getParentToken();
            }
        } else {
            if (ll_tab1.getVisibility() == View.VISIBLE) {
                if(et_tab1_userEmail.getText().toString().equals("")
                        || et_tab1_userPassword.getText().toString().equals("")
                        || et_tab1_userPassword1.getText().toString().equals("")) {
                    ToastUtil.showShort(SignUpActivity.this, "You cannot leave required fields blank.");
                }else if(et_tab1_userPassword.getText().length() <6){
                    ToastUtil.showShort(SignUpActivity.this, "Your password must be at least 6 characters.");
                }else if(!et_tab1_userPassword.getText().toString().equals(et_tab1_userPassword1.getText().toString())){
                    ToastUtil.showShort(SignUpActivity.this, "Please input same password.");
                }else if (et_tab1_userEmail.getText() != null && et_tab1_userEmail.getText().length() > 0) {
                    checkRegister(et_tab1_userEmail.getText().toString());
                    //ll_tab1.setVisibility(View.GONE);
                    //ll_tab_teacher2.setVisibility(View.VISIBLE);
                }

            } else if (sv_tab_teacher2.getVisibility() == View.VISIBLE) {
                if(ifCompleteTeacherInfo1() && isTeacherSexSelcted){
                    sv_tab_teacher2.setVisibility(View.GONE);
                    ll_tab_teacher3.setVisibility(View.VISIBLE);
                }
                /*else if(isTeacherSexSelcted){
                    ToastUtil.showShort(SignUpActivity.this, "You should select gender.");
                }*/
                else {
                    ToastUtil.showShort(SignUpActivity.this, "You cannot leave required fields blank.");
                }
            } else if (ll_tab_teacher3.getVisibility() == View.VISIBLE) {
                if(ifCompleteTeacherInfo2()){
                    ll_tab_teacher3.setVisibility(View.GONE);
                    ll_tab_end.setVisibility(View.VISIBLE);
                    tv_signUp.setText("SUBMIT");
                }
                else{
                    ToastUtil.showShort(SignUpActivity.this, "You cannot leave required fields blank.");
                }
            } else if (ll_tab_end.getVisibility() == View.VISIBLE) {
                teacherRegister();
                //getTeacherToken();
            }
        }
    }

    private void backFlow() {
        if (!HaojiajiaoApplication.ISSTATE) {
            if (ll_tab1.getVisibility() == View.VISIBLE) {
                Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else if (sv_parent_tab2.getVisibility() == View.VISIBLE) {
                sv_parent_tab2.setVisibility(View.GONE);
                ll_city_list.setVisibility(View.GONE);
                ll_tab1.setVisibility(View.VISIBLE);
            } else if (ll_tab_end.getVisibility() == View.VISIBLE) {
                ll_tab_end.setVisibility(View.GONE);
                sv_parent_tab2.setVisibility(View.VISIBLE);
                tv_signUp.setText("SAVE, NEXT");
            }
        } else {
            if (ll_tab1.getVisibility() == View.VISIBLE) {
                Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else if (sv_tab_teacher2.getVisibility() == View.VISIBLE) {
                sv_tab_teacher2.setVisibility(View.GONE);
                ll_city_list_teacher.setVisibility(View.GONE);
                ll_tab1.setVisibility(View.VISIBLE);
            } else if (ll_tab_teacher3.getVisibility() == View.VISIBLE) {
                ll_tab_teacher3.setVisibility(View.GONE);
                sv_tab_teacher2.setVisibility(View.VISIBLE);
            } else if (ll_tab_end.getVisibility() == View.VISIBLE) {
                ll_tab_end.setVisibility(View.GONE);
                ll_tab_teacher3.setVisibility(View.VISIBLE);
                tv_signUp.setText("SAVE, NEXT");
            }
        }
    }


    //get token after signup and store the token by SharedPreference.   --husai
    //public void storeToken

    private void getTeacherTokenPhp(){
        String s = "http://121.42.140.239/hjj/php/getTeacherToken?" + "teacherUserName=" + et_tab1_userEmail.getText().toString();
        URL url =null;
        try{
            url = new URL(s);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        sendRequestWithHttpURLConnection(url);
    }
    private void getParentTokenPhp(){
        String s = "http://121.42.140.239/hjj/php/getParentToken?" + "parentUserName=" + et_tab1_userEmail.getText().toString();
        URL url =null;
        try{
            url = new URL(s);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        sendRequestWithHttpURLConnection(url);
    }

    private void sendRequestWithHttpURLConnection(final URL url) {
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
                    String token = null;
                    jsondata = jsondata.replace("\\", "").replace("u003d", "=");
                    try {
                        JSONObject total1 = new JSONObject(jsondata);
                        token = total1.optString("token");
                        if(!token.equals("403")){
                            HaojiajiaoApplication.token = token;
                            SharedPreferences.Editor editor = getSharedPreferences("UserToken",MODE_PRIVATE).edit();
                            editor.putString(et_tab1_userEmail.getText().toString(),token);
                            editor.commit();
                            Log.e("SignUpAct:"," get token by sendRequestWithHttpURLConnection: "+token);
                        }
                        else{
                            HaojiajiaoApplication.token = token;
                            Log.e("SignUpAct:","get token by sendRequestWithHttpURLConnection 403!!! ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        HaojiajiaoApplication.token = "-1";
                        Log.e("SignUpActivity:","get token by sendRequestWithHttpURLConnection~get token error!");
                    }
                    // 将服务器返回的结果存放到Message中
                    message.obj = token;
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
                    Log.e("SignUpActivity:","get token by sendRequestWithHttpURLConnection~ "+response);
                    // 在这里进行UI操作，将结果显示到界面上
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }

    };

    //获取教师用户的token
    private void getTeacherToken() {
        Map<String, String> dataParas = new HashMap<>();
        //设置用户名
        dataParas.put("teacherUserName", et_tab1_userEmail.getText().toString());
        requestHandler.sendHttpRequestWithParamByGet("http://121.42.140.239/hjj/php/getTeacherToken", dataParas, RequestTag.getTeacherToken);
    }

    //获取家长用户的token
    private void getParentToken() {
        Map<String, String> dataParas = new HashMap<>();
        //设置用户名
        Log.e("sss","username: "+et_tab1_userEmail.getText().toString());
        dataParas.put("parentUserName", et_tab1_userEmail.getText().toString());
        requestHandler.sendHttpRequestWithParamByGet("http://121.42.140.239/hjj/php/getParentToken", dataParas, RequestTag.getParentToken);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case BitmapUtil.UPLOAD_IMAGE:

                // BitmapUtil.doCropPhoto(data,SignUpActivity.this);
                Uri url = null;
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap == null) {
                        try {
                            BitmapUtil.saveBitmapToFileForCompress(BitmapUtil.decodeUriAsBitmap(SignUpActivity.this, data.getData()));
                            iv_userIcon.setImageBitmap(BitmapUtil.decodeUriAsBitmap(SignUpActivity.this, data.getData()));
                            ifHasIMG = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        picPath = data.getData().toString();
                    } else {
                        try {
                            BitmapUtil.saveBitmapToFileForCompress(bitmap);
                            iv_userIcon.setImageBitmap(bitmap);
                            ifHasIMG = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        try {
                            url = Uri.parse(BitmapUtil.saveBitmapToFile(bitmap));
                            picPath = url.toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case BitmapUtil.CAMERA_WITH_DATA:
                BitmapUtil.doCropPhoto(data, SignUpActivity.this);
                break;
        }
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SignUpActivity.class.getCanonicalName().equals(intent.getAction())) {
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
        if (tv_city_gps != null || tv_city_gps_teacher != null) {
            if (city != null) {
                if (tv_city_gps != null) {
                    tv_city_gps.setText("当前定位城市：" + city);
                }
                if (tv_city_gps_teacher != null) {
                    tv_city_gps_teacher.setText("当前定位城市：" + city);
                }
            } else {
                tv_city_gps.setText("定位信息无法获取");
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
        if (response.requestTag.toString().equals("checkRegister")) {//检查邮件是否可用
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);
                String responsestatus = total1.optString("result");
                if (responsestatus.equals("1")) {
                    new MsgDialog(SignUpActivity.this, et_tab1_userEmail.getText().toString(), true, "Please confirm that your login account is:", new MsgLisenner() {
                        @Override
                        public void goback() {
                            if (!HaojiajiaoApplication.ISSTATE) {
                                ll_tab1.setVisibility(View.GONE);
                                sv_parent_tab2.setVisibility(View.VISIBLE);
                                email = et_tab1_userEmail.getText().toString();
                                Log.e("Signup",email);
                                et_parentTab2Email.setText(email);
                            } else {
                                ll_tab1.setVisibility(View.GONE);
                                sv_tab_teacher2.setVisibility(View.VISIBLE);
                                email = et_tab1_userEmail.getText().toString();
                                et_TeacherTab2Email.setText(email);
                            }
                        }
                    }).show();
                } else {
                    new MsgDialog(SignUpActivity.this, "Account already exists", true, "Sorry!", new MsgLisenner() {
                        @Override
                        public void goback() {
                        }
                    }).show();
                }
            } catch (Exception e) {
                e.printStackTrace();


            }

        } else if (response.requestTag.toString().equals("teacherRegister")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                String responsestatus = total1.optString("result");
                if (responsestatus.equals("1")) {
                    HaojiajiaoApplication.IFLOGIN = true;
                    ToastUtil.showLong(SignUpActivity.this, "Congratulations! Registration success.");
                    HaojiajiaoApplication.userName = et_tab1_userEmail.getText().toString();
                    HaojiajiaoApplication.ISSTATE = true;
                    if (ifHasIMG) {
                        HaojiajiaoApplication.ifIsSignUp = true;
                    }
                    /*Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();*/
                    getTeacherTokenPhp();

                } else {
                    ToastUtil.showLong(SignUpActivity.this, "Sorry, Registration failure.");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (response.requestTag.toString().equals("parentRegister")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                String responsestatus = total1.optString("result");
                if (responsestatus.equals("1")) {
                    HaojiajiaoApplication.IFLOGIN = true;
                    if (ifHasIMG) {
                        HaojiajiaoApplication.ifIsSignUp = true;
                    }

                    ToastUtil.showLong(SignUpActivity.this, "Congratulations! Registration success.");
                    HaojiajiaoApplication.userName = et_tab1_userEmail.getText().toString();
                    HaojiajiaoApplication.ifIsSignUp = true;
                    HaojiajiaoApplication.ISSTATE = false;
                    Log.e("SignUp","username:"+HaojiajiaoApplication.userName);
                    /*Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();*/
                    getParentTokenPhp();
                } else {
                    ToastUtil.showLong(SignUpActivity.this, "Sorry, Registration failure.");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (response.requestTag.toString().equals("getTeacherToken")) {
            String dataStr = response.responseString;
            dataStr = dataStr.replace("\\", "").replace("u003d", "=");
            try {
                JSONObject total1 = new JSONObject(dataStr);
                String code = total1.optString("token");
                if(!code.equals("403")){
                    HaojiajiaoApplication.token = code;
                    SharedPreferences.Editor editor = getSharedPreferences("UserToken",MODE_PRIVATE).edit();
                    editor.putString(et_tab1_userEmail.getText().toString(),code);
                    editor.commit();
                    Log.e("SignUpAct:","~~~~~get token and localize: "+code);
                }
                else{
                    HaojiajiaoApplication.token = code;
                    Log.e("SignUpAct:","~~~~~get token 403!!! ");
                }
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                HaojiajiaoApplication.token = "403";
                Log.e("SignUpActivity:","~~~~~~~~~~get token error!~~~~~~~");
            }
        }else if (response.requestTag.toString().equals("getParentToken")) {
            String dataStr = response.responseString;
            Log.e("signup","data1: "+dataStr);
            dataStr = dataStr.replace("\\", "").replace("u003d", "=");
            Log.e("signup","data2: "+dataStr);
            try {
                JSONObject total1 = new JSONObject(dataStr);
                String code = total1.optString("token");
                if(!code.equals("403")){
                    HaojiajiaoApplication.token = code;
                    SharedPreferences.Editor editor = getSharedPreferences("UserToken",MODE_PRIVATE).edit();
                    editor.putString(et_tab1_userEmail.getText().toString(),code);
                    editor.commit();
                    Log.e("SignUpAct:","~~~~~get token and localize: "+code);
                }
                else{
                    HaojiajiaoApplication.token = code;
                    Log.e("SignUpAct:","~~~~~get token 403!!! ");
                }
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
                HaojiajiaoApplication.token = "403";
                Log.e("SignUpActivity:","~~~~~~~~~~get token error!~~~~~~~");
            }
        }
    }
}

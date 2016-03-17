package com.shanghai.haojiajiao.fragment.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.activity.SearchActivity;
import com.shanghai.haojiajiao.activity.TeacherActivity;
import com.shanghai.haojiajiao.adapter.CityAdapter;
import com.shanghai.haojiajiao.adapter.Course1Adapter;
import com.shanghai.haojiajiao.adapter.Course2Adapter;
import com.shanghai.haojiajiao.adapter.SortAdapter;
import com.shanghai.haojiajiao.adapter.TeacherPageAdatper;
import com.shanghai.haojiajiao.base.BaseFragment;
import com.shanghai.haojiajiao.model.CityModel;
import com.shanghai.haojiajiao.model.Course1Model;
import com.shanghai.haojiajiao.model.SortModel;
import com.shanghai.haojiajiao.model.TeacherListModel;
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


/**
 * 首页主界面
 *
 * @author cyj
 */
public class HomePageMainFM extends BaseFragment implements OnClickListener{
    private View contentView = null;
    //城市，教师查看，搜索
    private LinearLayout ll_city;
    private TextView tv_teacher, tv_city_gps;
    private LinearLayout ll_search, ll_iv2;
    private ImageView iv1, iv2, iv3, iv21, iv22;
    //教师查看，搜索，
    private LinearLayout ll_tab2;
    //年级，课程，排序，搜索框
    private LinearLayout ll_subject, ll_sort;
    private ListView listview;
    //private LinearLayout ll_blank;              // added by husai
    private TeacherPageAdatper teacherPageAdatper;
    //城市
    private TextView tv_city;
    private LinearLayout ll_city_list;
    private ListView lv_city;
    private CityAdapter cityAdapter;
    //课程
    private TextView tv_subject;
    private LinearLayout ll_course_list;
    private ListView lv_course1_list, lv_course2_list;
    private Course1Adapter course1Adapter;
    private Course2Adapter course2Adapter;
    //排序
    private TextView tv_sort;
    private LinearLayout ll_sort_list,ll_teacher_list;
    private ListView lv_sort_list;
    private SortAdapter sortAdapter;
    private int courselist1 = 0;
    private int courselist2 = 0;
    private int sort = 0;
    private int cityPos = 3;
    private int coursePos = 5;
    private int filterPos = 1;
    private LoadingDialog loadingDialog = null;
    public ArrayList<TeacherListModel> teacherListModels = null;
    int[] str = {4, 7};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragement_home_page_main, container, false);
            Log.e("HomePageMainFM","come to HomePageMainFM!");
            initView();
            listview = (ListView) contentView.findViewById(R.id.listview);
            teacherPageAdatper = new TeacherPageAdatper(getActivity());
            listview.setAdapter(teacherPageAdatper);
//            ArrayList<String> strings = new ArrayList();
//            for (int a = 0; a < 9; a++) {
//                strings.add("111");
//            }
//            teacherPageAdatper.setData(strings);

            getTeacherByRate();
            listview();
        }
        return contentView;
    }

    public void setGpsCity(String city) {
        if (tv_city_gps != null) {
            if (city != null) {
                tv_city_gps.setText("Current location city:：" + city);
                if (tv_city != null) {
                    tv_city.setText(city);
                }

            } else {
                tv_city_gps.setText("Location information can not be obtained!");
            }

        }

    }

    /**
     * 初始化view
     */
    private void initView() {
        teacherListModels = new ArrayList<>();
        tv_city = (TextView) contentView.findViewById(R.id.tv_city);
        ll_city = (LinearLayout) contentView.findViewById(R.id.ll_city);
//        ll_city.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setGpsCity(HaojiajiaoApplication.city);
//            }
//        });
        loadingDialog = new LoadingDialog(getActivity());
        //tv_city_gps = (TextView) contentView.findViewById(R.id.tv_city_gps);
        lv_city = (ListView) contentView.findViewById(R.id.lv_city);
        lv_city.setAdapter(cityAdapter = new CityAdapter(getActivity()));
        tv_teacher = (TextView) contentView.findViewById(R.id.tv_teacher);
        ll_search = (LinearLayout) contentView.findViewById(R.id.ll_search);
        //ll_blank = (LinearLayout) contentView.findViewById(R.id.ll_blank);
        iv1 = (ImageView) contentView.findViewById(R.id.iv1);
        iv2 = (ImageView) contentView.findViewById(R.id.iv2);
        iv21 = (ImageView) contentView.findViewById(R.id.iv21);
        iv22 = (ImageView) contentView.findViewById(R.id.iv22);
        iv3 = (ImageView) contentView.findViewById(R.id.iv3);
        ll_iv2 = (LinearLayout) contentView.findViewById(R.id.ll_iv2);
        ll_tab2 = (LinearLayout) contentView.findViewById(R.id.ll_tab2);
        ll_subject = (LinearLayout) contentView.findViewById(R.id.ll_subject);
        ll_subject.setOnClickListener(this);
        ll_sort = (LinearLayout) contentView.findViewById(R.id.ll_sort);
        ll_city_list = (LinearLayout) contentView.findViewById(R.id.ll_city_list);
        ll_course_list = (LinearLayout) contentView.findViewById(R.id.ll_course_list);
        lv_course1_list = (ListView) contentView.findViewById(R.id.lv_course1_list);
        lv_course1_list.setAdapter(course1Adapter = new Course1Adapter(getActivity()));
        //lv_course2_list = (ListView) contentView.findViewById(R.id.lv_course2_list);
        //lv_course2_list.setAdapter(course2Adapter = new Course2Adapter(getActivity()));
        tv_subject = (TextView) contentView.findViewById(R.id.tv_subject);
        ll_sort_list = (LinearLayout) contentView.findViewById(R.id.ll_sort_list);
        ll_teacher_list = (LinearLayout) contentView.findViewById(R.id.ll_teacher_list);
        lv_sort_list = (ListView) contentView.findViewById(R.id.lv_sort_list);
        lv_sort_list.setAdapter(sortAdapter = new SortAdapter(getActivity()));
        tv_sort = (TextView) contentView.findViewById(R.id.tv_sort);
        lv_sort_list();
        lv_city();
        lv_course1_list();
        //lv_course2_list();
        ll_sort.setOnClickListener(this);
        ll_city.setOnClickListener(this);
        tv_teacher.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        //ll_blank.setOnClickListener(this);
        ivShow(1);
        //setGpsCity(HaojiajiaoApplication.city);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_city:
                ivShow(0);
                break;
            case R.id.tv_teacher:
                ivShow(1);
                break;
            case R.id.ll_search:
                ivShow(2);
                break;
            case R.id.ll_subject:
                if (ll_city_list.getVisibility() == View.VISIBLE) {
                    ll_city_list.setVisibility(View.GONE);
                    iv1.setVisibility(View.INVISIBLE);
                }
                if (ll_sort_list.getVisibility() == View.VISIBLE) {
                    ll_sort_list.setVisibility(View.GONE);
                    iv22.setVisibility(View.INVISIBLE);
                }
                if (ll_course_list.getVisibility() == View.VISIBLE) {
                    ll_course_list.setVisibility(View.GONE);
                    iv21.setVisibility(View.INVISIBLE);
                } else {
                    ll_course_list.setVisibility(View.VISIBLE);
                    iv21.setVisibility(View.VISIBLE);
                    course1Adapter.clearData();
                    ArrayList<Course1Model> course1Models = new ArrayList<>();
                    for (int i = 0; i < 11; i++) {          //注意！！！目前是有11门课程~~~~~~~~~~~~~~~~~~
                        Course1Model course1Model = new Course1Model();
                        course1Model.setCatchCourse(i == coursePos);
                        course1Models.add(course1Model);
                    }
                    course1Adapter.setData(course1Models);
                }
                break;
            case R.id.ll_sort:
                if (ll_city_list.getVisibility() == View.VISIBLE) {
                    ll_city_list.setVisibility(View.GONE);
                    iv1.setVisibility(View.INVISIBLE);
                }
                ll_city_list.setVisibility(View.GONE);
                iv1.setVisibility(View.INVISIBLE);
                iv21.setVisibility(View.INVISIBLE);
                ll_course_list.setVisibility(View.GONE);
                if (ll_sort_list.getVisibility() == View.VISIBLE) {
                    ll_sort_list.setVisibility(View.GONE);
                    iv22.setVisibility(View.INVISIBLE);
                } else {
                    ll_sort_list.setVisibility(View.VISIBLE);
                    iv22.setVisibility(View.VISIBLE);
                    sortAdapter.clearData();
                    ArrayList<SortModel> list = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        SortModel sortModel = new SortModel();
                        sortModel.setCatchSort(i == filterPos);
                        list.add(sortModel);
                    }
                    sortAdapter.setData(list);
                }
                iv21.setVisibility(View.INVISIBLE);
                break;
            /*case R.id.ll_blank:
                Log.e("HomePageMainFM","~~~~~~~~~~~~~~click ll_blank~~~~~~~~~~~~~~");
                if (ll_city_list.getVisibility() == View.VISIBLE) {
                    ll_city_list.setVisibility(View.GONE);
                    iv1.setVisibility(View.INVISIBLE);
                }
                if (ll_sort_list.getVisibility() == View.VISIBLE) {
                    ll_sort_list.setVisibility(View.GONE);
                    iv22.setVisibility(View.INVISIBLE);
                }
                if (ll_course_list.getVisibility() == View.VISIBLE) {
                    ll_course_list.setVisibility(View.GONE);
                    iv21.setVisibility(View.INVISIBLE);
                }
                ll_sort_list.setVisibility(View.GONE);
                iv22.setVisibility(View.INVISIBLE);
                break;*/
        }
    }

    /***
     * 切换选项
     *
     * @param number
     */
    public void ivShow(int number) {
        switch (number) {
            case 0:
                iv2.setVisibility(View.INVISIBLE);
                iv3.setVisibility(View.INVISIBLE);
                if (ll_city_list.getVisibility() == View.VISIBLE) {
                    ll_city_list.setVisibility(View.GONE);
                    iv1.setVisibility(View.INVISIBLE);
                } else {
                    ll_city_list.setVisibility(View.VISIBLE);
                    iv1.setVisibility(View.VISIBLE);
                    cityAdapter.clearData();
                    ArrayList<CityModel> cityModels = new ArrayList<>();
                    for (int i = 0; i < CityAdapter.testCity.length; i++) {
                        CityModel cityModel = new CityModel();
                        cityModel.setCatchCity(i == cityPos);
                        cityModels.add(cityModel);
                    }
                    cityAdapter.setData(cityModels);
                }
                if (ll_course_list.getVisibility() == View.VISIBLE) {
                    ll_course_list.setVisibility(View.GONE);
                    iv21.setVisibility(View.INVISIBLE);
                }
                if (ll_sort_list.getVisibility() == View.VISIBLE) {
                    ll_sort_list.setVisibility(View.GONE);
                    iv22.setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                iv1.setVisibility(View.INVISIBLE);
                iv2.setVisibility(View.VISIBLE);
                iv3.setVisibility(View.INVISIBLE);
                ll_iv2.setVisibility(View.VISIBLE);
                ll_tab2.setVisibility(View.VISIBLE);
                if (ll_city_list.getVisibility() == View.VISIBLE) {
                    ll_city_list.setVisibility(View.GONE);
                    iv1.setVisibility(View.INVISIBLE);
                }
                if (ll_course_list.getVisibility() == View.VISIBLE) {
                    ll_course_list.setVisibility(View.GONE);
                    iv21.setVisibility(View.INVISIBLE);
                }
                if (ll_sort_list.getVisibility() == View.VISIBLE) {
                    ll_sort_list.setVisibility(View.GONE);
                    iv22.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:

                if (ll_city_list.getVisibility() == View.VISIBLE) {
                    ll_city_list.setVisibility(View.GONE);
                    iv1.setVisibility(View.INVISIBLE);
                }
                if (ll_course_list.getVisibility() == View.VISIBLE) {
                    ll_course_list.setVisibility(View.GONE);
                    iv21.setVisibility(View.INVISIBLE);
                }
                if (ll_sort_list.getVisibility() == View.VISIBLE) {
                    ll_sort_list.setVisibility(View.GONE);
                    iv22.setVisibility(View.INVISIBLE);
                }
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    private void listview() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ll_city_list.getVisibility() == View.VISIBLE){
                    ll_city_list.setVisibility(View.GONE);
                    iv1.setVisibility(View.INVISIBLE);
                }
                else if(ll_course_list.getVisibility() == View.VISIBLE){
                    ll_course_list.setVisibility(View.GONE);
                    iv21.setVisibility(View.INVISIBLE);
                }else if(ll_sort_list.getVisibility() == View.VISIBLE) {
                    ll_sort_list.setVisibility(View.GONE);
                    iv22.setVisibility(View.INVISIBLE);
                }
                else {
                    TeacherListModel teacherListModel = (TeacherListModel) teacherPageAdatper.getItem(position);
                    Intent intent = new Intent(getActivity(), TeacherActivity.class);
                    intent.putExtra("tab", 0);
                    intent.putExtra("TeacherUserName", teacherListModel.getTeacherUserName());
                    intent.putExtra("TeacherId", teacherListModel.getId());
                    intent.putExtra("TeacherName",teacherListModel.getTeacherName());
                    Log.e("HomePage-TeacherAct","teacherName: "+teacherListModel.getTeacherName());
                    getActivity().startActivity(intent);
                }


            }
        });

        ll_teacher_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_city_list.setVisibility(View.GONE);
                iv1.setVisibility(View.GONE);
                ll_course_list.setVisibility(View.GONE);
                iv21.setVisibility(View.GONE);
                //ll_sort_list.setVisibility(View.GONE);
                iv22.setVisibility(View.GONE);
            }
        });

    }

    private void lv_city() {
        lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setCityColor(position);
                if (ll_city_list.getVisibility() == View.VISIBLE) {
                    cityPos = position;
                    tv_city.setText(CityAdapter.testCity[position]);
                    ll_city_list.setVisibility(View.GONE);
                    getTeacherByCity(CityAdapter.testCity[position]);
                    iv1.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void lv_course1_list() {
        lv_course1_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ll_course_list.getVisibility() == View.VISIBLE) {
                    ll_course_list.setVisibility(View.GONE);
                    iv21.setVisibility(View.INVISIBLE);

                }
                coursePos = position;
                getTeacherByLesson(Course1Adapter.testCourse[position]);
                tv_subject.setText(Course1Adapter.testCourse1[position]);
            }
        });
    }

    /*private void lv_course1_list() {
        lv_course1_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                course1Adapter.setCourse1Color(position);
                course2Adapter.clearData();
                ArrayList<Course2Model> course2Models = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    Course2Model course2Model = new Course2Model();
                    course2Model.setCatchCourse(i == 0);
                    course2Models.add(course2Model);
                }
                course2Adapter.setData(course2Models);
                course2Adapter.setCourse2Color(0);
                courselist1 = position;
            }
        });
    }*/

/*    private void lv_course2_list() {
        lv_course2_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ll_course_list.getVisibility() == View.VISIBLE) {
                    ll_course_list.setVisibility(View.GONE);
                    iv21.setVisibility(View.INVISIBLE);
                    courselist2 = position;
                    getTeacherByLesson(Course2Adapter.testCourseType[position]);
                    tv_subject.setText(Course1Adapter.testCourse[courselist1] + "-" + Course2Adapter.testCourse[position]);
                }
            }
        });

    }*/

    private void lv_sort_list() {
        lv_sort_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ll_sort_list.getVisibility() == View.VISIBLE) {
                    ll_sort_list.setVisibility(View.GONE);
                    sort = position;
                    filterPos = position;
                    tv_sort.setText(SortAdapter.testCourse[position]);
                    switch (position) {
                        case 0:
                            getTeacherByRate();
                            break;
                        case 1:
                            getTeacherByCharge(true);
                            break;
                        case 2:
                            getTeacherByCharge(false);
                            break;
                    }
                    iv22.setVisibility(View.INVISIBLE);
                } else {
                    sortAdapter.clearData();
                    ArrayList<SortModel> sortModels = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        SortModel sortModel = new SortModel();
                        sortModel.setCatchSort(i == filterPos);
                        sortModels.add(sortModel);
                    }
                    sortAdapter.setData(sortModels);
                    sortAdapter.setSortColor(0);
                }
            }
        });
    }

    private void getTeacherByRate() {
        Map<String, String> dataParas = new HashMap<>();
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherByRate, dataParas, RequestTag.getTeacherByRate);
    }

    private void getTeacherByCharge(Boolean boo) {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("Type", boo ? "Up" : "Down");
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherByCharge, dataParas, RequestTag.getTeacherByRate);
    }


    private void getTeacherByCity(String city) {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("City", city);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherByCity, dataParas, RequestTag.getTeacherByRate);
    }

    private void getTeacherByLesson(String lesson) {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("Lesson", lesson);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherByLesson, dataParas, RequestTag.getTeacherByRate);
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        loadingDialog.dismiss();
        super.onRequestError(response);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        loadingDialog.dismiss();
        teacherPageAdatper.clearData();
        if (response.requestTag.toString().equals("getTeacherByRate")) {
            String dataStr = response.responseString;
            try {
                teacherListModels.clear();
                JSONObject total1 = new JSONObject(dataStr);
                JSONArray jsonArray = total1.getJSONArray("result");
                if (jsonArray != null) {
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        TeacherListModel teacherListModel = new TeacherListModel();
                        teacherListModel.setTeacherName(jsonObject.optString("teacherName"));
                        //Log.e("","name: "+ jsonObject.optString("teacherName"));
                        teacherListModel.setTeacherEmail(jsonObject.optString("teacherEmail"));
                        teacherListModel.setTeacherGender(jsonObject.optString("teacherGender"));
                        teacherListModel.setTeacherUserName(jsonObject.optString("teacherUserName"));
                        teacherListModel.setId(jsonObject.optInt("id"));
                        teacherListModel.setTeacherLessen(jsonObject.optString("teacherLesson"));
                        teacherListModel.setTeacherRate(jsonObject.optDouble("teacherRate"));
                        teacherListModel.setPicUrl(jsonObject.optString("teacherPortrait"));
                        teacherListModel.setTeacherMoney(jsonObject.optString("teacherCharge"));
                        teacherListModels.add(teacherListModel);

//                        UserList userList =new UserList();
//                        userList.setId(jsonObject.optInt("id"));
//                        userList.setUserName(jsonObject.optString("teacherUserName"));
//                        userList.setName(jsonObject.optString("teacherName"));
//                        userList.setPicUrl(jsonObject.optString("teacherPortrait"));
//                        HaojiajiaoApplication.UserList.add(userList);
                    }
                    teacherPageAdatper.setData(teacherListModels);
//                    UserList userList =new UserList();
//                    userList.setId(HaojiajiaoApplication.userId);
//                    userList.setName(HaojiajiaoApplication.name);
//                    userList.setUserName(HaojiajiaoApplication.userName);
//                    userList.setPicUrl(HaojiajiaoApplication.picUrl);
//                    Log.e("HomePageMainFM","add user into userlist,user Url:"+HaojiajiaoApplication.picUrl);
//                    Log.e("HomePageMainFM","add user into userlist,user name:"+HaojiajiaoApplication.name);
//                    Log.e("HomePageMainFM","add user into userlist,user userName:"+HaojiajiaoApplication.userName);
//                    HaojiajiaoApplication.UserList.add(userList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}

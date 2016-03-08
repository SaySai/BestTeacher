package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.SearchHistorydapter;
import com.shanghai.haojiajiao.adapter.TeacherPageAdatper;
import com.shanghai.haojiajiao.base.BaseActivity;
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

public class SearchActivity extends BaseActivity {
    private ImageView iv_back;
    private TextView tv_search, tv_text;
    private EditText et_search;
    private ListView lv_history, listview;
    private TeacherPageAdatper teacherPageAdatper;
    private LinearLayout layout;
    private SearchHistorydapter stringSearchHistorydapter;
    private ScrollView sv_history;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private SharedPreferences preferences = null;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;
    private ArrayList<TeacherListModel> teacherListModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SearchActivity.class.getCanonicalName());
        loadingDialog = new LoadingDialog(SearchActivity.this);
        teacherListModels = new ArrayList<>();
        registerReceiver(receiver, intentFilter);
        preferences = getSharedPreferences("Search", 1);
        layout = (LinearLayout) findViewById(R.id.layout);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        sv_history = (ScrollView) findViewById(R.id.sv_history);
        tv_search = (TextView) findViewById(R.id.tv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        lv_history = (ListView) findViewById(R.id.lv_history);
        listview = (ListView) findViewById(R.id.listview);
        tv_text = (TextView) findViewById(R.id.tv_text);
        teacherPageAdatper = new TeacherPageAdatper(SearchActivity.this);
        stringSearchHistorydapter = new SearchHistorydapter(SearchActivity.this, preferences);
        lv_history.setAdapter(stringSearchHistorydapter);
        lv_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_search.setText(stringSearchHistorydapter.getItem(Math.abs(stringSearchHistorydapter.getCount() - 1 - position)).toString());
            }
        });
        listview.setAdapter(teacherPageAdatper);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TeacherListModel teacherListModel = (TeacherListModel) teacherPageAdatper.getItem(position);
                Intent intent = new Intent(SearchActivity.this, TeacherActivity.class);
                intent.putExtra("tab", 0);
                intent.putExtra("TeacherUserName", teacherListModel.getTeacherUserName());
                intent.putExtra("TeacherId", teacherListModel.getId());
                startActivity(intent);
            }
        });
        tv_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringSearchHistorydapter.clearData();
            }
        });
        final ArrayList<String> historyStrings = getSpThingJson(preferences.getString("history", ""));
        if (historyStrings.size() > 1) {
            stringSearchHistorydapter.setData(historyStrings);
        }
//        ArrayList<String> strings = new ArrayList();
//        for (int a = 0; a < 9; a++) {
//            strings.add("111");
//        }
//        teacherPageAdatper.setData(strings);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_search.getText() != null && et_search.getText().length() > 0) {
                    historyStrings.clear();
                    historyStrings.add(et_search.getText().toString());
                    searchTeacher(et_search.getText().toString());
                    stringSearchHistorydapter.setData(historyStrings);
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (v.getText() != null && v.getText().length() > 0) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                    /*隐藏软键盘*/
                        historyStrings.add(event.toString());
                        stringSearchHistorydapter.setData(historyStrings);
                        InputMethodManager imm = (InputMethodManager) v
                                .getContext().getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        searchTeacher(event.toString());
                        if (imm.isActive()) {
                            imm.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                        sv_history.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void searchTeacher(String city) {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("Keyword", city);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.searchTeacher, dataParas, RequestTag.getTeacherByRate);
        loadingDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //添加layout大小发生改变监听器
        layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

                    if (stringSearchHistorydapter.getCount() > 0) {
                        sv_history.setVisibility(View.VISIBLE);
                    }
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    sv_history.setVisibility(View.GONE);

                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("history", getListData(stringSearchHistorydapter.getList()));
        editor.commit();
        super.onDestroy();
    }

    private String getListData(ArrayList<String> items) {
        JSONArray array = null;
        array = new JSONArray();
        if (items.size() > 0) {
            for (int a = 0; a < items.size(); a++) {
                String item = items.get(a);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("text", item);
                    array.put(a, jsonObject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return array.toString();
    }

    private ArrayList<String> getSpThingJson(String json) {

        ArrayList<String> items = new ArrayList<String>();
        try {
            JSONArray array = new JSONArray(json);
            for (int a = 0; a < array.length(); a++) {
                JSONObject object2 = array.getJSONObject(a);
                String item2 = object2.getString("text1");
                items.add(item2);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return items;
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SearchActivity.class.getCanonicalName().equals(intent.getAction())) {
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
        if (response.requestTag.toString().equals("getTeacherByRate")) {
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);
                teacherListModels.clear();
                teacherPageAdatper.clearData();
                JSONArray jsonArray = total1.getJSONArray("result");
                if (jsonArray != null) {
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        TeacherListModel teacherListModel = new TeacherListModel();
                        teacherListModel.setTeacherName(jsonObject.getString("teacherName"));
                        teacherListModel.setTeacherEmail(jsonObject.getString("teacherEmail"));
                        teacherListModel.setTeacherGender(jsonObject.getString("teacherGender"));
                        teacherListModel.setTeacherUserName(jsonObject.getString("teacherUserName"));
                        teacherListModel.setTeacherLessen(jsonObject.getString("teacherLesson"));
                        teacherListModel.setId(jsonObject.getInt("id"));
                        teacherListModel.setTeacherRate(jsonObject.getDouble("teacherRate"));
                        teacherListModel.setPicUrl(jsonObject.optString("teacherPortrait"));
                        teacherListModels.add(teacherListModel);
                    }
                    teacherPageAdatper.setData(teacherListModels);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

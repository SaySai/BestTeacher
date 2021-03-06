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
import com.shanghai.haojiajiao.adapter.TeacherEvaluateAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.model.TeacherEvaluateModel;
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

public class EvaluateListActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private ListView listview;
    private TeacherEvaluateAdapter teacherEvaluateAdapter;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        loadingDialog = new LoadingDialog(EvaluateListActivity.this);
        intentFilter.addAction(EvaluateListActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        listview = (ListView) findViewById(R.id.listview);
        teacherEvaluateAdapter = new TeacherEvaluateAdapter(this);
        listview.setAdapter(teacherEvaluateAdapter);
//        teacherEvaluateAdapter.setData(strings);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getTeacherEvaluation();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void getTeacherEvaluation() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("TeacherId", HaojiajiaoApplication.userId + "");
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherEvaluation, stringMap, RequestTag.getTeacherEvaluation);
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        loadingDialog.dismiss();
        super.onRequestError(response);
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (EvaluateListActivity.class.getCanonicalName().equals(intent.getAction())) {
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
    public void onRequestSuccess(ResponseOwn response) {
        if (response.requestTag.toString().equals("getTeacherEvaluation")) {
            loadingDialog.dismiss();
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);
                JSONArray jsonArray = total1.getJSONArray("result");
                if (jsonArray != null) {
                    ArrayList<TeacherEvaluateModel> arrayList = new ArrayList<>();
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        TeacherEvaluateModel model = new TeacherEvaluateModel();
                        model.setId(jsonObject.getInt("id"));
                        model.setEvaluationContent(jsonObject.getString("evaluationContent"));
                        model.setEvaluationRate(jsonObject.getInt("evaluationRate"));
                        model.setParentId(jsonObject.getInt("parentId"));
                        model.setTeacherId(jsonObject.getInt("teacherId"));
                        model.setEvaluationTime(jsonObject.getString("evaluationTime"));
                        arrayList.add(model);
                    }
                    teacherEvaluateAdapter.setData(arrayList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

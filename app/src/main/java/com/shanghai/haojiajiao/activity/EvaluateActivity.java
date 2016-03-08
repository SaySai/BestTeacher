package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.ToastUtil;
import com.shanghai.haojiajiao.weight.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EvaluateActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private ImageView iv_userIcon;
    private TextView tv_username;
    private TextView tv_times;
    private ImageView ratingBar0, ratingBar1, ratingBar2, ratingBar3, ratingBar4;
    private EditText et_content;
    private TextView tv_up;
    private FinishReceiver receiver;
    private String ParentId;
    private String TeacherId;
    private int Evaluate = 0;
    private LoadingDialog loadingDialog = null;
    private String OrderDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_evaluate);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EvaluateActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        ParentId = getIntent().getStringExtra("ParentId");
        TeacherId = getIntent().getStringExtra("TeacherId");
        OrderDay = getIntent().getStringExtra("OrderDay");
        loadingDialog = new LoadingDialog(EvaluateActivity.this);
        iv_userIcon = (ImageView) findViewById(R.id.iv_userIcon);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_times = (TextView) findViewById(R.id.tv_times);
        tv_times.setText("预约时间：" + OrderDay);
        ratingBar0 = (ImageView) findViewById(R.id.ratingBar0);
        ratingBar0.setOnClickListener(this);
        ratingBar1 = (ImageView) findViewById(R.id.ratingBar1);
        ratingBar1.setOnClickListener(this);
        ratingBar2 = (ImageView) findViewById(R.id.ratingBar2);
        ratingBar2.setOnClickListener(this);
        ratingBar3 = (ImageView) findViewById(R.id.ratingBar3);
        ratingBar3.setOnClickListener(this);
        ratingBar4 = (ImageView) findViewById(R.id.ratingBar4);
        ratingBar4.setOnClickListener(this);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_up = (TextView) findViewById(R.id.tv_up);
        tv_up.setOnClickListener(this);
        getTeacherById();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switchRatingBar(v.getId());
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_up:
                addEvaluation(ParentId, TeacherId, et_content.getText().toString(), Evaluate + "");
                break;

        }
    }

    private void setBar(float number) {
        if (number < 0.5) {
            ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
        } else if (number >= 0.5 && number < 1.5) {
            ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));

        } else if (number >= 1.5 && number < 2.5) {
            ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));

        }
        if (number >= 2.5 && number < 3.5) {
            ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
            ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
        }
        if (number >= 3.5 && number < 4.5) {
            ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
        }
        if (number >= 4.5 && number <= 5) {
            ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
            ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
        }
    }

    private void switchRatingBar(int id) {
        switch (id) {
            case R.id.ratingBar0:
                Evaluate = 1;
                ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                break;
            case R.id.ratingBar1:
                Evaluate = 2;
                ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                break;
            case R.id.ratingBar2:
                Evaluate = 3;
                ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                break;
            case R.id.ratingBar3:
                Evaluate = 4;
                ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_empty));
                break;
            case R.id.ratingBar4:
                Evaluate = 5;
                ratingBar0.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar1.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar3.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                ratingBar4.setImageDrawable(getResources().getDrawable(R.mipmap.ic_star_fill));
                break;
        }
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (EvaluateActivity.class.getCanonicalName().equals(intent.getAction())) {
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

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        if (response.requestTag.toString().equals("addEvaluation")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                String result = total1.optString("result");
                if (!result.trim().equals("")) {
                    if (result.equals("1")) {
                        ToastUtil.showShort(EvaluateActivity.this, "评价完成!");
                        EvaluateActivity.this.finish();
                    } else {
                        ToastUtil.showShort(EvaluateActivity.this, "评价失败!");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (response.requestTag.toString().equals("getTeacherById")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                JSONObject result = total1.getJSONObject("result");
                tv_username.setText(result.optString("teacherName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void addEvaluation(String ParentId, String TeacherId, String EvaluationContent, String EvaluationRate) {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("ParentId", ParentId);
        dataParas.put("TeacherId", TeacherId);
        dataParas.put("EvaluationContent", EvaluationContent);
        dataParas.put("EvaluationRate", EvaluationRate);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.addEvaluation, dataParas, RequestTag.addEvaluation);
    }

    private void getTeacherById() {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("TeacherId", TeacherId);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherById, dataParas, RequestTag.getTeacherById);
    }

}

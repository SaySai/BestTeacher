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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpDatePwordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_up;
    private EditText et_userEmail;
    private EditText et_userPassword;
    private EditText et_newPassword;
    private EditText et_ageinPassword;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_up_date_pword);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpDatePwordActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        loadingDialog = new LoadingDialog(UpDatePwordActivity.this);
        tv_up = (TextView) findViewById(R.id.tv_up);
        tv_up.setOnClickListener(this);
        //et_userEmail = (EditText) findViewById(R.id.et_userEmail);
        et_userPassword = (EditText) findViewById(R.id.et_userPassword);
        et_newPassword = (EditText) findViewById(R.id.et_newPassword);
        et_ageinPassword = (EditText) findViewById(R.id.et_ageinPassword);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_up:
                changePassword();
                break;
        }
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpDatePwordActivity.class.getCanonicalName().equals(intent.getAction())) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void changePassword() {
        Map<String, String> dataParas = new HashMap<>();
        if (et_userPassword.getText().toString() != null && et_userPassword.getText().length() > 0) {
            dataParas.put("oldPassword", et_userPassword.getText().toString());
        }
//        if (et_userEmail.getText().toString() != null && et_userEmail.getText().length() > 0) {
//            dataParas.put("userName", et_userEmail.getText().toString());
//        }
        if (et_newPassword.getText().toString() != null && et_newPassword.getText().length() > 0) {
            if (et_ageinPassword.getText() != null && et_ageinPassword.getText().length() > 0) {
                if (et_newPassword.getText().toString().equals(et_ageinPassword.getText()) && !et_userPassword.getText().toString().equals(et_ageinPassword.getText())) {
                    dataParas.put("newPassword", et_newPassword.getText().toString());
                }
            }
        }
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.changePassword, dataParas, RequestTag.changePassword);
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
                    finish();
                    ToastUtil.showLong(UpDatePwordActivity.this, "Successful modification！");
                } else {
                    ToastUtil.showLong(UpDatePwordActivity.this, "Modify failed！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

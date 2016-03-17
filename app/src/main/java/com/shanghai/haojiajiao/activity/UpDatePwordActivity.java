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
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.ToastUtil;
import com.shanghai.haojiajiao.weight.LoadingDialog;

import org.json.JSONObject;

import java.security.MessageDigest;
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void changePassword() {
        Map<String, String> dataParas = new HashMap<>();
        if(et_userPassword.getText().toString().equals("") ||
                et_newPassword.getText().toString().equals("") ||
                et_ageinPassword.getText().toString().equals("")){
            ToastUtil.showShort(UpDatePwordActivity.this, "You cannot leave required fields blank.");
        }else if(et_newPassword.getText().toString().length()<6){
            ToastUtil.showShort(UpDatePwordActivity.this, "Your password must be at least 6 characters.");
        } else if(!et_newPassword.getText().toString().equals(et_ageinPassword.getText().toString())){
            ToastUtil.showShort(UpDatePwordActivity.this, "Please type the same password twice.");
        }
        else {
            dataParas.put("oldPassword", MD5(et_userPassword.getText().toString()));
            dataParas.put("userName", HaojiajiaoApplication.userName);
            dataParas.put("newPassword", MD5(et_newPassword.getText().toString()));
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
        if (response.requestTag.toString().equals("changePassword")) {//检查邮件是否可用
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

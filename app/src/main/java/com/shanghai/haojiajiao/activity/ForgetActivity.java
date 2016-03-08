package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.shanghai.haojiajiao.weight.MsgDialog;
import com.shanghai.haojiajiao.weight.MsgLisenner;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;

import cn.smssdk.SMSSDK;

public class ForgetActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private EditText et_tab1_userEmail, et_tab1_phones, et_tab1_yanzheng, et_tab1_password1, et_tab1_password2;
    private TextView tv_finish, tv_signUp;
    private boolean ifNext = true;
    private SharedPreferences preferences;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        receiver = new FinishReceiver();
        loadingDialog = new LoadingDialog(ForgetActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ForgetActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        SMSSDK.initSDK(this, "df88811a3201", "8f8c3268808c73f18a79c503acb722ea");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        et_tab1_userEmail = (EditText) findViewById(R.id.et_tab1_userEmail);
        et_tab1_phones = (EditText) findViewById(R.id.et_tab1_phones);
        et_tab1_yanzheng = (EditText) findViewById(R.id.et_tab1_yanzheng);
        et_tab1_password1 = (EditText) findViewById(R.id.et_tab1_password1);
        et_tab1_password2 = (EditText) findViewById(R.id.et_tab1_password2);
        tv_finish = (TextView) findViewById(R.id.tv_finish123);
        tv_finish.setOnClickListener(this);
        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(this);
        preferences = getSharedPreferences("forget", 1);
        Calendar c = new GregorianCalendar();
        long user = preferences.getLong("endTime", 0);
        if (c.getTimeInMillis() >= user) {
            ifNext = true;
            tv_finish.setText("获取验证码");
        } else {
            ifNext = false;
            tv_finish.setText((int) (Math.abs(c.getTimeInMillis() - user)) / 1000 + "秒");
            handler.sendMessageDelayed(handler.obtainMessage(0), 1000);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_finish123:

                String phone = et_tab1_phones.getText().toString();
                if (ifNext) {
                    if (phone != null && phone.length() > 0) {
                        ifNext = false;
                        Calendar c = new GregorianCalendar();
                        SharedPreferences.Editor edit = preferences.edit();
                        c.setTimeInMillis(c.getTimeInMillis() + 60 * 1000);
                        edit.putLong("endTime", c.getTimeInMillis());
                        edit.commit();
                        SMSSDK.getVerificationCode("86", phone);
                        tv_finish.setText(60 + "second(s)");
                        handler.sendMessageDelayed(handler.obtainMessage(0), 1000);
                    } else {
                        ToastUtil.showLong(this, "请输入您注册本应用的时候使用的手机号！");
                    }
                }

                break;
            case R.id.tv_signUp:
                resetPassword();

                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (0 == msg.what) {
                Calendar c = new GregorianCalendar();
                long user = preferences.getLong("endTime", 0);
                if (c.getTimeInMillis() >= user) {
                    ifNext = true;
                    tv_finish.setText("获取验证码");
                    ToastUtil.showLong(ForgetActivity.this, "Verification code has been sent to your phone and your SMS should arrive in 60 second(s)");
                } else {
                    tv_finish.setText((int) (Math.abs(c.getTimeInMillis() - user)) / 1000 + "second(s)");
                    handler.sendMessageDelayed(handler.obtainMessage(0), 1000);
                }
            }
            super.handleMessage(msg);

        }
    };

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ForgetActivity.class.getCanonicalName().equals(intent.getAction())) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void resetPassword() {
        if (et_tab1_userEmail.getText() == null || et_tab1_userEmail.getText().length() == 0) {
            ToastUtil.showLong(ForgetActivity.this, "Sorry, you cannot leave required fields blank.");
            return;
        }
        if (et_tab1_yanzheng.getText() == null || et_tab1_yanzheng.getText().length() == 0) {
            ToastUtil.showLong(ForgetActivity.this, "Sorry, you cannot leave required fields blank.");
            return;
        }
        if (et_tab1_password1.getText().toString() == null && et_tab1_password1.getText().length() == 0) {
            ToastUtil.showLong(ForgetActivity.this, "Sorry, you cannot leave required fields blank.");
            return;
        }
        if (et_tab1_password2.getText() == null && et_tab1_password2.getText().length() == 0) {
            ToastUtil.showLong(ForgetActivity.this, "Sorry, you cannot leave required fields blank.");
            return;
        }
        if (!et_tab1_password1.getText().equals(et_tab1_password2.getText())) {
            ToastUtil.showLong(ForgetActivity.this, "The passwords you entered are not the same. Please re-enter your password.");
            return;
        }
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("userName", et_tab1_userEmail.getText().toString());
        dataParas.put("newPassword", MD5(et_tab1_password1.getText().toString()));
        dataParas.put("checkCode", et_tab1_yanzheng.getText().toString());
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.resetPassword, dataParas, RequestTag.resetPassword);
        loadingDialog.show();
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
    public void onRequestError(ResponseOwn response) {
        loadingDialog.dismiss();
        super.onRequestError(response);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        loadingDialog.dismiss();
        if (response.requestTag.toString().equals("resetPassword")) {//检查邮件是否可用
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);
                String responsestatus = total1.optString("result");
                if (responsestatus.equals("1")) {
                    new MsgDialog(ForgetActivity.this, "密码修改成功！", false, "", new MsgLisenner() {
                        @Override
                        public void goback() {
                            finish();
                        }
                    }).show();
                } else {
                    new MsgDialog(ForgetActivity.this, "Error, please check the login account.", false, "", new MsgLisenner() {
                        @Override
                        public void goback() {
                        }
                    }).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

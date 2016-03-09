package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import cn.jpush.android.api.JPushInterface;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_wellcome;
    private TextView tv_wellcome;
    private TextView tv_parent;
    private TextView tv_child;
    private TextView tv_friend;
    private  boolean  isClick=true;//是否点击接受按钮
    private LinearLayout ll_tab1, ll_tab2, ll_wc1, ll_wc2;

    private EditText et_username, et_password;

    private TextView tv_login, tv_signUp, tv_forget, tv_law;
    private ImageView checkBox;
    private boolean FALV = true;
    private int index = 0;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;
    private TextView accept_tv;
    @Override
    protected void onResume() {
        Log.e("WelcomeActivity","welcomeActivity:onResume()");
        JPushInterface.onResume(WelcomeActivity.this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("WelcomeActivity","welcomeActivity:onPause()");
        JPushInterface.onPause(WelcomeActivity.this);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("WelcomeActivity","进入welcomeActivity。");
        super.onCreate(savedInstanceState);
        Log.e("WelcomeActivity","开始setContentView。");
        setContentView(R.layout.activity_welcome);
        Log.e("WelcomeActivity","new FinishReceiver");
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        Log.e("WelcomeActivity","新建IntentFilter结束。");
        intentFilter.addAction(WelcomeActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        Log.e("WelcomeActivity","registerReceiver建立。");
        iv_wellcome = (ImageView) findViewById(R.id.iv_wellcome);
        tv_wellcome = (TextView) findViewById(R.id.tv_wellcome);
        accept_tv= (TextView) findViewById(R.id.accept_tv);
        accept_tv.setOnClickListener(this);
        tv_parent = (TextView) findViewById(R.id.tv_parent);
        tv_parent.setOnClickListener(this);
        tv_child = (TextView) findViewById(R.id.tv_child);
        tv_child.setOnClickListener(this);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
        tv_friend.setOnClickListener(this);
        ll_tab1 = (LinearLayout) findViewById(R.id.ll_tab1);
        ll_tab2 = (LinearLayout) findViewById(R.id.ll_tab2);
        ll_wc1 = (LinearLayout) findViewById(R.id.ll_wc1);
        ll_wc2 = (LinearLayout) findViewById(R.id.ll_wc2);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
//        et_username.setText("admin00@163.com");
//        et_password.setText("e10adc3949ba59abbe56e057f20f883e");
        //et_username.setText("TestParent@qq.com");
        //et_password.setText("123456");
        tv_login = (TextView) findViewById(R.id.tv_login);
        checkBox = (ImageView) findViewById(R.id.checkBox);
        checkBox.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(this);
        tv_law = (TextView) findViewById(R.id.tv_law);
        tv_law.setOnClickListener(this);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
        Log.e("WelcomeActivity","界面控件find结束。");
        HaojiajiaoApplication application = (HaojiajiaoApplication) getApplication();
        //application.initLocation();
        //application.startLocation();
        Log.e("WelcomeActivity","new loadingDialog开始。");
        loadingDialog = new LoadingDialog(WelcomeActivity.this);
        Log.e("WelcomeActivity","new loadingDialog结束。");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switchNext(v.getId());
        switchUp(v.getId());
    }

    private void switchNext(int id) {

        switch (id) {
            case R.id.tv_parent:
                index = 0;
                ll_tab1.setVisibility(View.GONE);
                ll_wc1.setVisibility(View.GONE);
                ll_wc2.setVisibility(View.VISIBLE);
                ll_tab2.setVisibility(View.VISIBLE);
                HaojiajiaoApplication.ISSTATE = false;
                Log.e("welcome",HaojiajiaoApplication.ISSTATE?"Tea":"Par");
                break;
            case R.id.tv_child:
                index = 1;
                ll_tab1.setVisibility(View.GONE);
                ll_wc1.setVisibility(View.GONE);
                ll_tab2.setVisibility(View.VISIBLE);
                ll_wc2.setVisibility(View.VISIBLE);
                HaojiajiaoApplication.ISSTATE = true;
                Log.e("welcome",HaojiajiaoApplication.ISSTATE?"Tea":"Par");
                break;
            case R.id.tv_friend:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                HaojiajiaoApplication.IFLOGIN = false;
                finish();
                break;
        }
    }

    private void switchUp(int id) {

        switch (id) {
            case R.id.tv_login:
                if(isClick==false)
                {
                    ToastUtil.showShort(this,"please accept legal terms");
                    return;
                }
                String strUsername = et_username.getText().toString();
                String strPassword = et_password.getText().toString();
                if (strUsername == null || strUsername.length() <= 5) {
                    ToastUtil.showLong(WelcomeActivity.this, "Sorry! Invalid Format！");
                    return;
                }
                if (strPassword == null && strPassword.length() <= 4) {
                    ToastUtil.showLong(WelcomeActivity.this, "Your password must be at least six characters and may contain numbers, uppercase and lowercase letters, and standard symbols. Do not include spaces.");
                    return;
                }
                login(strUsername, MD5(strPassword));
                loadingDialog.show();
                break;
            case R.id.tv_signUp:
                if(isClick==false)
                {
                    ToastUtil.showShort(this,"please accept legal terms");
                    return;
                }
                Intent intent1 = new Intent(WelcomeActivity.this, SignUpActivity.class);
                if (et_username.getText() != null) {
                    intent1.putExtra("email", et_username.getText().toString());
                }
                if (et_password.getText() != null) {
                    intent1.putExtra("password", et_password.getText().toString());
                }
                startActivity(intent1);
                finish();
                break;
            case R.id.tv_law:
                final Uri uri = Uri.parse("http://www.google.com");
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it); //执行
                break;
            case R.id.tv_forget:
                Intent intent2 = new Intent(this, ForgetActivity.class);
                startActivity(intent2);
                break;
            case R.id.checkBox:
                isClick =!isClick;
                if (FALV) {
                    FALV = false;
                    checkBox.setImageDrawable(getResources().getDrawable(R.drawable.checkbox));
                } else {
                    FALV = true;
                    checkBox.setImageDrawable(getResources().getDrawable(R.mipmap.checkbox));
                }
                break;
            case R.id.accept_tv:
                /*isClick =!isClick;
                if(isClick)
                {
                    checkBox.setVisibility(View.VISIBLE);
                }else{
                    checkBox.setVisibility(View.GONE);
                }*/

                break;
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


    public class FinishReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("WelcomeActivity","welcomeActivity:FinishReceiver-onReceive");
            if (WelcomeActivity.class.getCanonicalName().equals(intent.getAction())) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (ll_tab1.getVisibility() == View.VISIBLE && ll_wc1.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            ll_tab1.setVisibility(View.VISIBLE);
            ll_wc1.setVisibility(View.VISIBLE);
            ll_wc2.setVisibility(View.GONE);
            ll_tab2.setVisibility(View.GONE);
        }

    }

    public void login(String email, String password) {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("userName", email);
        dataParas.put("password", password);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.login, dataParas, RequestTag.userLogin);
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        loadingDialog.dismiss();
        super.onRequestError(response);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        loadingDialog.dismiss();
        if (response.requestTag.toString().equals("userLogin")) {
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);
                String responsestatus = total1.optString("errorCode");
                if (responsestatus.equals("1")) {
                    ToastUtil.showShort(this, "Login failed");
                } else {
                    Toast.makeText(WelcomeActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                    HaojiajiaoApplication.userName = et_username.getText().toString();
                    String ifTeacher = total1.optString("userType");
                    if (ifTeacher.equals("teacher")) {
                        index = 1;
                        HaojiajiaoApplication.ISSTATE = true;
                    } else {
                        HaojiajiaoApplication.ISSTATE = false;
                        index = 0;
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    HaojiajiaoApplication.IFLOGIN = true;
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

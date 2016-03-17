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
import com.shanghai.haojiajiao.model.BroadCastAction;

import io.rong.imkit.RongIM;

public class SettingsActivity extends BaseActivity {
    private ImageView iv_back;
    private TextView tv_signUp, tv_forget;
    private EditText et_tab1_userEmail, et_tab1_userPassword;
    private FinishReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SettingsActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        et_tab1_userEmail = (EditText) findViewById(R.id.et_tab1_userEmail);
        et_tab1_userEmail.setText(HaojiajiaoApplication.userName);
        et_tab1_userPassword = (EditText) findViewById(R.id.et_tab1_userPassword);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().disconnect();
                Intent ntent = new Intent(SettingsActivity. this, WelcomeActivity.class);
                SettingsActivity. this.startActivity(ntent);
                SettingsActivity.this.finish();
                Intent msgIntent = new Intent(BroadCastAction.MAINACTIVITY);
                SettingsActivity.this.sendBroadcast(msgIntent);

            }
        });
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SettingsActivity.this, UpDatePwordActivity.class);
                startActivity(intent2);
            }
        });
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SettingsActivity.class.getCanonicalName().equals(intent.getAction())) {
                finish();
            }
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}

package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.BaseActivity;

/**
 * Created by isay on 3/7/2016.
 */
public class ContactUsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_TName;
    private TextView tv_content1,tv_content2;
    private ImageView iv_img;
    private FinishReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NoticeActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_TName = (TextView) findViewById(R.id.tv_TName);
        tv_content1 = (TextView) findViewById(R.id.tv_content1);
        tv_content2 = (TextView) findViewById(R.id.tv_content2);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NoticeActivity.class.getCanonicalName().equals(intent.getAction())) {
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

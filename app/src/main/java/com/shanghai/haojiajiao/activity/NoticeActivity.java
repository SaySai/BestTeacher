package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.BaseActivity;

public class NoticeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_TName;
    private TextView tv_titleName, tv_Times, tv_content;
    private ImageView iv_img;
    private FinishReceiver receiver;
    private String  title="",date="",picUrl="",content="";
    private DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NoticeActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
//        title=this.getIntent().getStringExtra("title");
//        date=this.getIntent().getStringExtra("date");
//        picUrl=this.getIntent().getStringExtra("picUrl");
//        content=this.getIntent().getStringExtra("content");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_TName = (TextView) findViewById(R.id.tv_TName);
        tv_titleName = (TextView) findViewById(R.id.tv_titleName);
        //tv_titleName.setText(title);
        //tv_Times = (TextView) findViewById(R.id.tv_Times);
        //tv_Times.setText(date);
        tv_content = (TextView) findViewById(R.id.tv_content);
        //tv_content.setText(content);
        iv_img = (ImageView) findViewById(R.id.iv_img);
//        options = UilUtil.getOptions();
//        ImageLoader.getInstance().displayImage(
//                picUrl, iv_img, options);
//        if (getIntent().getIntExtra("about", 0) == 0) {
//            tv_TName.setText("About");
//        } else {
//            tv_TName.setText("About");
//        }
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

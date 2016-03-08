package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.MsgLeaveAdapter;

import java.util.ArrayList;

public class LeaveActivity extends ActionBarActivity implements View.OnClickListener {
    private ImageView iv_back;
    private ListView listview;
    private MsgLeaveAdapter<String> msgLeaveAdapter;
    private FinishReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LeaveActivity.class.getCanonicalName());
        registerReceiver(receiver, intentFilter);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        listview = (ListView) findViewById(R.id.listview);
        msgLeaveAdapter = new MsgLeaveAdapter<String>(this);
        listview.setAdapter(msgLeaveAdapter);
        ArrayList<String> strings = new ArrayList();
        for (int a = 0; a < 9; a++) {
            strings.add("111");
        }
        msgLeaveAdapter.setData(strings);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LeaveActivity.class.getCanonicalName().equals(intent.getAction())) {
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

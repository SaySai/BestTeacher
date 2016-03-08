package com.shanghai.haojiajiao.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;

/**
 * Created by Administrator on 2016/1/6.
 */
public class MsgDialog extends Dialog {
    private MsgLisenner lisenner;
    private String text;
    private boolean ifHasTitle;
    private String title;

    public MsgDialog(Context context, String text, boolean ifHasTitle, String title, final MsgLisenner lisenner) {
        super(context, R.style.MyDialog);
        this.lisenner = lisenner;
        this.text = text;
        this.ifHasTitle = ifHasTitle;
        this.title = title;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_dialog);
        getWindow().setLayout(HaojiajiaoApplication.deviceWidth - 80, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView hold_layout = (TextView) findViewById(R.id.hold_layout);
        hold_layout.setVisibility(ifHasTitle ? View.VISIBLE : View.GONE);
        if (ifHasTitle) {
            hold_layout.setText(title);
        }
        TextView dialog_content_tv = (TextView) findViewById(R.id.dialog_content_tv);
        dialog_content_tv.setText(text);
        TextView tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                lisenner.goback();
            }
        });
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}

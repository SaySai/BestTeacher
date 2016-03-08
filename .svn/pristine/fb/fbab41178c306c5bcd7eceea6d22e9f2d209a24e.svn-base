package com.shanghai.haojiajiao.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.weight.ProgressBar.TrueOrFalseInterface;

/**
 * Created by Administrator on 2015/12/25.
 */
public class BookingDialog extends Dialog {
    private TrueOrFalseInterface orFalseInterface;

    public BookingDialog(Context context, TrueOrFalseInterface orFalseInterface) {
        super(context, R.style.MyDialog);
        this.orFalseInterface = orFalseInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* requestWindowFeature(Window.FEATURE_NO_TITLE);  */
        setContentView(R.layout.booking_dialog);
        getWindow().setLayout(HaojiajiaoApplication.deviceWidth - 80, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv_login = (TextView) findViewById(R.id.tv_login);
        TextView tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orFalseInterface.toTrue();
                dismiss();
            }
        });
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orFalseInterface.toFalse();
                dismiss();
            }
        });

    }
}

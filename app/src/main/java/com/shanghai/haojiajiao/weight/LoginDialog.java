package com.shanghai.haojiajiao.weight;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.activity.WelcomeActivityForVisitor;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.model.BroadCastAction;

/**
 * Created by Administrator on 2015/12/25.
 */
public class LoginDialog extends Dialog {
    private LoginLisenner loginLisenner;
    private String text;
    private Activity context;
    public LoginDialog(Activity context, String text, LoginLisenner loginLisenner) {
        super(context, R.style.MyDialog);
        this.loginLisenner = loginLisenner;
        this.text = text;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* requestWindowFeature(Window.FEATURE_NO_TITLE);  */
        setContentView(R.layout.login_or_sign_dialog);

        getWindow().setLayout(HaojiajiaoApplication.deviceWidth - 80, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView dialog_content_tv = (TextView) findViewById(R.id.dialog_content_tv);
        LinearLayout ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        dialog_content_tv.setText(text);
        TextView tv_login = (TextView) findViewById(R.id.tv_login);
        TextView tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
//                loginLisenner.login();
                Intent ntent = new Intent(LoginDialog. this.context, WelcomeActivityForVisitor.class);
                ntent.putExtra("isLogin","Login");
                LoginDialog. this.context.startActivity(ntent);
                Intent msgIntent = new Intent(BroadCastAction.MAINACTIVITY);
                LoginDialog. this.context.sendBroadcast(msgIntent);
                LoginDialog.this.context.finish();

            }
        });
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent ntent = new Intent(LoginDialog. this.context, WelcomeActivityForVisitor.class);
                ntent.putExtra("isLogin","signUp");
                LoginDialog. this.context.startActivity(ntent);
                Intent msgIntent = new Intent(BroadCastAction.MAINACTIVITY);
                LoginDialog. this.context.sendBroadcast(msgIntent);
                LoginDialog.this.context.finish();
            }
        });

    }
}

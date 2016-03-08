package com.shanghai.haojiajiao.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.shanghai.haojiajiao.R;

/**
 * Created by Administrator on 2016/1/22.
 */
public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
    }
}

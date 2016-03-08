package com.shanghai.haojiajiao.weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import com.shanghai.haojiajiao.app.HaojiajiaoApplication;


/**
 * 自定义的Dialog
 * @author wqb
 *
 */
public class CustomDialog extends Dialog {
	private int layouId;
    Context context;
    public CustomDialog(Context context,int layoutId) {
        super(context);
         this.context = context;
         this.layouId = layoutId;
    }
    public CustomDialog(Context context, int theme,int layoutId){
        super(context, theme);
        this.context = context;
        this.layouId = layoutId;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        /* requestWindowFeature(Window.FEATURE_NO_TITLE);  */
        this.setContentView(layouId);
        getWindow().setLayout(HaojiajiaoApplication.deviceWidth-80, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    
}

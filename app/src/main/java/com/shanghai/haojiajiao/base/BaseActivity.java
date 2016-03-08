package com.shanghai.haojiajiao.base;

import android.content.Context;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.util.HttpUtil.RequestHandler;
import com.shanghai.haojiajiao.util.HttpUtil.RequestListener;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.weight.EmptyLayout;

/*
* 功能:所有activity集成该类,用于进行统一管理
 */
public class BaseActivity extends FragmentActivity implements RequestListener, View.OnClickListener {
    protected Context context;
    protected RequestHandler requestHandler;
    private EmptyLayout mEmptyLayout; // this is required to show different layouts (loading or empty or error)

    public BaseActivity() {
        super();
        requestHandler = new RequestHandler();
        requestHandler.listener = this;
        context = HaojiajiaoApplication.context;

    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {

    }

    @Override
    public void onRequestError(ResponseOwn response) {

        if ("".equals(response.errorMessage) || response.errorMessage == null || response.errorMessage.equals("")) {
            Toast.makeText(context, "The network connection is lost, please try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, response.errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    //设置EmptyLayout
    protected void setEmptyLayout(GridView mGridView) {
        mEmptyLayout = new EmptyLayout(this, mGridView);
        mEmptyLayout.setErrorButtonClickListener(this);
    }


    //当程序出现错误时对程序进行错误处理则在该onClick方法中写
    @Override
    public void onClick(View v) {

    }

    // Triggered when "Empty" button is clicked
    public void onShowEmpty(View view) {
        mEmptyLayout.showEmpty();
    }

    // Triggered when "Loading" button is clicked
    public void onShowLoading(View view) {
        mEmptyLayout.showLoading();
    }

    // 展示所有的信息
    public void onShowError(View view) {
        mEmptyLayout.showError();
    }

    public void onShowList(View view) {
        mEmptyLayout.stopAnimation();
    }

    //设置状态栏是否显示与高度设置,每一个activity都要重写该方法
    public void setStatusBar(View view, int colorResources) {
        if (HaojiajiaoApplication.isCanFullScreen) {
            // 显示texiview默认的是 隐藏
            view.setVisibility(View.VISIBLE);
            // 给textview设置手机状态栏的高度
            view.getLayoutParams().height = HaojiajiaoApplication.statusBarHeight;
            if (colorResources != -1) {
                view.setBackgroundResource(colorResources);
            }
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.isFinishing() && mEmptyLayout != null) {
            mEmptyLayout.stopAnimation();
        }
    }

    // 获取点击事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

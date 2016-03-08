package com.shanghai.haojiajiao.util.HttpUtil;

/**
 * Created by Administrator on 2016/2/27.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}

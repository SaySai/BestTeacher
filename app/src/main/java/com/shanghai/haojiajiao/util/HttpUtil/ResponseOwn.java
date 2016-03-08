package com.shanghai.haojiajiao.util.HttpUtil;

import com.shanghai.haojiajiao.util.JsonParseUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 自己封装的网络返回结果的类
 *
 * @author ahnw_01
 */
public class ResponseOwn {
    public final static int REQUEST_SUCCESS = 1; // 访问成功

    public Map<String, Object> data;
    public RequestTag requestTag;
    public boolean requestSuccess = false;
    public String responseString; // 网络访问返回的原始数据
    public int responseStatus; // 服务器返回的这次请求的状态
    public String errorMessage; // 错误信息


    public ResponseOwn() {

    }

    public ResponseOwn(String stringData) {
        try {
            //stringData = new String(stringData.getBytes("ISO8859_1"),"UTF-8");
            stringData = new String(stringData.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        /**
         * 真实环境下的写法
         */
        responseString = stringData;
            if (stringData != null && stringData.length() > 0) { // 请求成功
                responseStatus=REQUEST_SUCCESS;
                requestSuccess = true;
            } else {
                requestSuccess = false;
            }

        // 返回成功
// 网络请求成功，但是处理失败，例如密码错误
        requestSuccess = responseStatus == REQUEST_SUCCESS;

    }

    /**
     * 从缓存中得到response对象
     *
     * @param stringData
     * @return
     */
    public static ResponseOwn initFromCache(String stringData) {
        ResponseOwn responseOwn = new ResponseOwn();

        responseOwn.responseString = stringData;
        /**
         * 真实环境下的写法
         */
        responseOwn.responseString = stringData;


        try {
            responseOwn.data = JsonParseUtil.parseJsonToCollect(stringData);
            if (responseOwn.data != null && responseOwn.data.size() > 0) { // 请求成功
                responseOwn.requestTag = RequestTag.valueOf(responseOwn.data.get("action") + "");

                responseOwn.requestSuccess = true;
                try {
                    responseOwn.data = (Map<String, Object>) responseOwn.data
                            .get("data"); // 剥一层，直接返回data的数据回去
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseOwn.requestSuccess = false;
            responseOwn.errorMessage = "json格式错误，无法解析";
            return responseOwn;
        }
        if (responseOwn.responseStatus == REQUEST_SUCCESS) { // 返回成功
            // requestSuccess = true;

        } else { // 网络请求成功，但是处理失败，例如密码错误
            responseOwn.errorMessage = (String) responseOwn.data.get("message");
            responseOwn.requestSuccess = false;
        }

        return responseOwn;

    }


}

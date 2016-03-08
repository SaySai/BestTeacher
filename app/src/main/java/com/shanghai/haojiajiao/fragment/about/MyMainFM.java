package com.shanghai.haojiajiao.fragment.about;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.activity.BookingSettingActivity;
import com.shanghai.haojiajiao.activity.ContactUsActivity;
import com.shanghai.haojiajiao.activity.MyDataActivity;
import com.shanghai.haojiajiao.activity.MyOrderListActivity;
import com.shanghai.haojiajiao.activity.NoticeActivity;
import com.shanghai.haojiajiao.activity.SettingsActivity;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseFragment;
import com.shanghai.haojiajiao.util.BitmapUtil;
import com.shanghai.haojiajiao.util.FileBase64Code;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.HttpUtils;
import com.shanghai.haojiajiao.util.ToastUtil;
import com.shanghai.haojiajiao.util.UilUtil;
import com.shanghai.haojiajiao.weight.CircleImageView;
import com.shanghai.haojiajiao.weight.ImgSwitchDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 我的 主界面
 *
 * @author zjy
 */
public class MyMainFM extends BaseFragment implements View.OnClickListener {
    private View contentView = null;
    private CircleImageView iv_userIcon;
    private TextView tv_userName;
    private ImageView iv_sex,iv_line;
    private TextView tv_userCity;
    private LinearLayout ll_myData, ll_TimeSetting, ll_booking, ll_about, ll_settings, ll_CustomerService;
    private DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragement_my_main, container, false);
        iv_userIcon = (CircleImageView) contentView.findViewById(R.id.iv_userIcon);
        iv_line = (ImageView) contentView.findViewById(R.id.iv_line);
        tv_userName = (TextView) contentView.findViewById(R.id.tv_userName);
        iv_sex = (ImageView) contentView.findViewById(R.id.iv_sex);
        tv_userCity = (TextView) contentView.findViewById(R.id.tv_userCity);
        ll_myData = (LinearLayout) contentView.findViewById(R.id.ll_myData);
        ll_myData.setOnClickListener(this);
        ll_TimeSetting = (LinearLayout) contentView.findViewById(R.id.ll_TimeSetting);
        ll_TimeSetting.setOnClickListener(this);
        ll_booking = (LinearLayout) contentView.findViewById(R.id.ll_booking);
        ll_booking.setOnClickListener(this);
        ll_about = (LinearLayout) contentView.findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);
        ll_settings = (LinearLayout) contentView.findViewById(R.id.ll_settings);
        ll_CustomerService = (LinearLayout) contentView.findViewById(R.id.ll_CustomerService);
        ll_CustomerService.setOnClickListener(this);
        ll_settings.setOnClickListener(this);
        if (HaojiajiaoApplication.ISSTATE) {
            ll_TimeSetting.setVisibility(View.VISIBLE);
            iv_line.setVisibility(View.VISIBLE);
        } else {
            ll_TimeSetting.setVisibility(View.GONE);
            iv_line.setVisibility(View.GONE);
        }
        options = UilUtil.getOptions();
        iv_userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImgSwitchDialog(getActivity(), new ImgSwitchDialog.ImgSwitchLisenner() {
                    @Override
                    public void album() {
//                        BitmapUtil.doPickPhotoFromGallery(getActivity());
//                        AV = 0;
                        try {
                            //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
                            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 2);
                        } catch (ActivityNotFoundException e) {

                        }
                    }

                    @Override
                    public void cam() {
//                        BitmapUtil.doTakePhoto(getActivity());
//                        AV = 0;
                        try {
                            //拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
                            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).show();

            }
        });


        if (HaojiajiaoApplication.ISSTATE) {
            getTeacherInfoByUserName();
        } else {
            getParentInfoByUserName();
        }
        return contentView;
    }
    @Override
    public void onResume() {
        super.onResume();
        //发送头像请求
        if (HaojiajiaoApplication.IFLOGIN) {
            getPicUrl();
        }
    }

    public void getParentInfoByUserName() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("userName", HaojiajiaoApplication.userName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getParentInfoByUserName, stringMap, RequestTag.getParentInfoByUserName);
    }

    public void getTeacherInfoByUserName() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("TeacherUserName", HaojiajiaoApplication.userName);
//        stringMap.put("TeacherUserName", "fsdf@qq.com");
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherInfoByUserName, stringMap, RequestTag.getTeacherInfoByUserName);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switchView(v.getId());
    }

    public void switchView(int id) {
        switch (id) {
            case R.id.ll_myData:
                Intent intent = new Intent(getActivity(), MyDataActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.ll_TimeSetting:
                Intent intent1 = new Intent(getActivity(), BookingSettingActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.ll_booking:
                Intent intent2 = new Intent(getActivity(), MyOrderListActivity.class);
                getActivity().startActivity(intent2);
                break;
            case R.id.ll_about:
                Intent intent4 = new Intent(getActivity(), NoticeActivity.class);
                getActivity().startActivity(intent4);
                break;
            case R.id.ll_settings:
                Intent intent5 = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(intent5);
                break;
            case R.id.ll_CustomerService:
                Intent intent6 = new Intent(getActivity(), ContactUsActivity.class);
                getActivity().startActivity(intent6);
                break;
        }
    }

    public void getRequestCodeFromActivity(final Bitmap bitmap) {
        iv_userIcon.setImageBitmap(bitmap);
        // BitmapUtil.doCropPhoto(data,SignUpActivity.this);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //将bitmap保存到指定文件夹
                    BitmapUtil.saveBitmapToFileForCompress(bitmap);
                    mHandler.sendEmptyMessage(1);
                } catch (IOException e) {
                    mHandler.sendEmptyMessage(0);
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtil.showShort(MyMainFM.this.getActivity(), "保存头像失败,请重新选择!");
                    break;
                case 1:
                    ToastUtil.showShort(MyMainFM.this.getActivity(), "保存头像成功!");
                    new Thread(networkTask).start();
                    break;
                case 2:
                    ToastUtil.showShort(MyMainFM.this.getActivity(), "上传图片结束!");
//                    new Thread(networkTask).start();
                    break;
                case 3:
                    ToastUtil.showShort(MyMainFM.this.getActivity(), "上传图片错误!");
//                    new Thread(networkTask).start();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    public void uploadFile(String imageFile) {
        Map<String, String> params = new HashMap<String, String>();
        String requestUrl = "http://121.42.140.239:8080/HaoJiaJiao/iOSFileUpload.action";
        try {
            params.put("img", FileBase64Code.encodeBase64File(imageFile));
//            // 请求普通信息
            params.put("name", "张三");
            params.put("UserName", HaojiajiaoApplication.userName);
            params.put("FigType", "jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtils.submitPostData(requestUrl, params, "utf-8");
        mHandler.sendEmptyMessage(2);

    }

    //新增上传文件接口
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
//            Message msg = new Message();
//            msg.what=1;
//            handler.sendMessage(msg);
            String picPath = com.shanghai.haojiajiao.util.CrashHandlerUtils.FileUtil.getImagePath() + "/crop_photo.jpg";
//            File picFile=new File(picPath);
            uploadFile(picPath);
        }
    };

    public void getPicUrl() {
        Map<String, String> dataParas = new HashMap<String, String>();
        dataParas.put("UserName", HaojiajiaoApplication.userName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getPicUrl, dataParas, RequestTag.getPorPath);
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        super.onRequestError(response);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        if (response.requestTag.toString().equals("getPorPath")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                String result = total1.optString("result");
                if (!result.trim().equals("")) {
                    ImageLoader.getInstance().displayImage(
                            result, iv_userIcon, options);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (response.requestTag.toString().equals("getTeacherInfoByUserName")) {
            String dataStr = response.responseString;
            Log.d("data", dataStr);
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                tv_userName.setText(total1.optString("teacherName"));
                String sex = total1.optString("teacherGender");
                tv_userCity.setText(total1.optString("teacherCity"));
                if(sex.equals("male")){
                    iv_sex.setImageDrawable(getResources().getDrawable(R.mipmap.male_icon));
                }
                else {
                    iv_sex.setImageDrawable(getResources().getDrawable(R.mipmap.female_icon));
                }
            } catch (Exception e) {

            }

        } else if (response.requestTag.toString().equals("getParentInfoByUserName")) {
            String dataStr = response.responseString;
            Log.d("data", dataStr);
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                JSONObject jsonArray = total1.getJSONObject("result");
                tv_userName.setText(jsonArray.optString("parentName"));
                tv_userCity.setText(jsonArray.optString("studentCity"));
                String sex = jsonArray.optString("teacherGender");
                if(sex.equals("male")){
                    iv_sex.setImageDrawable(getResources().getDrawable(R.mipmap.male_icon));
                }
                else {
                    iv_sex.setImageDrawable(getResources().getDrawable(R.mipmap.female_icon));
                }
            } catch (Exception e) {

            }
        }
    }
}

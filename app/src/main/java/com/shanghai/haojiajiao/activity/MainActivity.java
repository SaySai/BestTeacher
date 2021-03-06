package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.MyFragmentPagerAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.fragment.about.MyMainFM;
import com.shanghai.haojiajiao.fragment.homepage.HomePageMainFM;
import com.shanghai.haojiajiao.fragment.message.MessageMainFM;
import com.shanghai.haojiajiao.model.BroadCastAction;
import com.shanghai.haojiajiao.util.ExampleUtil;
import com.shanghai.haojiajiao.util.FileBase64Code;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.HttpUtils;
import com.shanghai.haojiajiao.weight.CustomDialog;
import com.shanghai.haojiajiao.weight.LoginDialog;
import com.shanghai.haojiajiao.weight.LoginLisenner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends BaseActivity implements View.OnClickListener,RongIM.UserInfoProvider {
    private LinearLayout homepage_ll, message_ll, my_ll;
    public final static int HOME_PAGETAG = 0;
    public final static int MESSAGETAG = 1;
    public final static int MYTAG = 2;
    private HomePageMainFM homePageMainFM;//作业
    private MessageMainFM messageMainFM;//个人中心
    private MyMainFM myMainFM;//关于我们
    private android.support.v4.app.FragmentManager fragmentManager;
    private CustomDialog mDialog = null;
    private TextView status_bar_tv, dialogContent;
    private LinearLayout dialogOK, dialogCancel;
    private ImageView main_page_iv, category_iv, my_package_iv;
    private String picPath = null;
    //for receive customer msg from jpush server
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static int IF_MSG_IS_INIT_FIRST = 0;
    public ViewPager viewPager = null;
    public static final int GET_TOKEN = 0;

    //    private TextView textView1,textView2,textView3;
//    private int color_pressed,color_no_pressed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("Mainactivity","进入MainActivity。");
        // TODO 自动生成的方法存根
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JPushInterface.init(getApplicationContext());
        fragmentManager = getSupportFragmentManager();
        initColor();
        initView();
        registerMessageReceiver();
        //getLocalToken();
//        initViewpager();
        //获取当前用户的token
        if( HaojiajiaoApplication.IFLOGIN ==false){
            initViewpager();
        }else {
            checkLocalToken();
            if(HaojiajiaoApplication.token.equals("") || HaojiajiaoApplication.token.equals("403")
                    || HaojiajiaoApplication.token.equals("-1")){
                //获取当前用户的token
                Log.e("MainAct:","~~~~~~~~~do getToken()~~~~~~~~~~~~~~");
                getToken();
            }
            else{
                initViewpager();
            }

        }
        RongIM.setUserInfoProvider(this, true);
    }



    @Override
    public UserInfo getUserInfo(String s) {
        Log.e("MainActivity:","userName:"+s);
        HaojiajiaoApplication.response=false;
        getTeacherInfoByUserNameForRY(s);
        getParentInfoByUserNameForRY(s);
        while(true){
            if(HaojiajiaoApplication.response){
                break;
            }
        }
        Log.e("MainActivity:","getUserInfo, PicUrl: " + HaojiajiaoApplication.R_picUrl);
        RongContext.getInstance().getUserInfoCache().put(HaojiajiaoApplication.R_userName,
                new UserInfo(HaojiajiaoApplication.R_userName,
                        HaojiajiaoApplication.R_name,
                        Uri.parse(HaojiajiaoApplication.R_picUrl)));
        return new UserInfo(HaojiajiaoApplication.R_userName,HaojiajiaoApplication.R_name,
                Uri.parse(HaojiajiaoApplication.R_picUrl));
    }

    private void getTeacherInfoByUserNameForRY(String TeacherUserName) {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("TeacherUserName", TeacherUserName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherInfoByUserName, stringMap, RequestTag.getTeacherInfoByUserNameForRY);
    }

    private void getParentInfoByUserNameForRY(String parentUserName) {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("userName", parentUserName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getParentInfoByUserName, stringMap, RequestTag.getParentInfoByUserNameForRY);
    }

    private void checkLocalToken(){
        SharedPreferences pref = getSharedPreferences("UserToken",MODE_PRIVATE);
        String token = pref.getString(HaojiajiaoApplication.userName,"");
        if(token.equals("")){
            Log.e("MainAct","Local not have this user's token.");
        }
        else{
            Log.e("MainAct","   Local have this user's token!!!!!");
            HaojiajiaoApplication.token = token;
        }
    }

    private void getTeacherTokenPhp(){
        String s = "http://121.42.140.239/hjj/php/getTeacherToken?" + "teacherUserName=" + HaojiajiaoApplication.userName;
        URL url =null;
        try{
            url = new URL(s);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        sendRequestWithHttpURLConnection(url);
    }
    private void getParentTokenPhp(){
        String s = "http://121.42.140.239/hjj/php/getParentToken?" + "parentUserName=" + HaojiajiaoApplication.userName;
        URL url =null;
        try{
            url = new URL(s);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        sendRequestWithHttpURLConnection(url);
    }

    private void sendRequestWithHttpURLConnection(final URL url) {
        // 开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    //URL url = new URL("http://121.42.140.239/hjj/php/getParentToken?parentUserName=pe@qq.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    message.what = GET_TOKEN;
                    String jsondata = response.toString();
                    String token = null;
                    jsondata = jsondata.replace("\\", "").replace("u003d", "=");
                    try {
                        JSONObject total1 = new JSONObject(jsondata);
                        token = total1.optString("token");
                        if(!token.equals("403")){
                            HaojiajiaoApplication.token = token;
                            SharedPreferences.Editor editor = getSharedPreferences("UserToken",MODE_PRIVATE).edit();
                            editor.putString(HaojiajiaoApplication.userName,token);
                            editor.commit();
                            Log.e("MainAct:"," get token by sendRequestWithHttpURLConnection: "+token);
                        }
                        else{
                            HaojiajiaoApplication.token = token;
                            Log.e("MainAct:","get token by sendRequestWithHttpURLConnection 403!!! ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        HaojiajiaoApplication.token = "-1";
                        Log.e("MainActivity:","get token by sendRequestWithHttpURLConnection~get token error!");
                    }
                    // 将服务器返回的结果存放到Message中
                    message.obj = token;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_TOKEN:
                    String response = (String) msg.obj;
                    Log.e("MainActivity:","get token by sendRequestWithHttpURLConnection~ "+response);
                    // 在这里进行UI操作，将结果显示到界面上
                    initViewpager();
                    break;
            }
        }

    };

    private void initViewpager() {
        viewPager = (ViewPager) findViewById(R.id.content_view);
        if (HaojiajiaoApplication.IFLOGIN) {
            viewPager.setOffscreenPageLimit(2);
        } else {
            viewPager.setOffscreenPageLimit(1);
        }
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(homePageMainFM = new HomePageMainFM());
        fragmentList.add(messageMainFM = new MessageMainFM());
        if (HaojiajiaoApplication.IFLOGIN) {
            fragmentList.add(myMainFM = new MyMainFM());
        }
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(HaojiajiaoApplication.screenBack);
        screenChange(HaojiajiaoApplication.screenBack);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                screenChange(HaojiajiaoApplication.screenBack = position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
        Log.e("MainAct","Destory");
        //RongIM.getInstance().disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //初始化控件
    private void initView() {
        homepage_ll = (LinearLayout) findViewById(R.id.homepage_ll);
        message_ll = (LinearLayout) findViewById(R.id.message_ll);
        my_ll = (LinearLayout) findViewById(R.id.my_ll);
        homepage_ll.setOnClickListener(this);
        message_ll.setOnClickListener(this);
        my_ll.setOnClickListener(this);
        status_bar_tv = (TextView) findViewById(R.id.status_bar_tv);
        main_page_iv = (ImageView) findViewById(R.id.main_page_iv);
        category_iv = (ImageView) findViewById(R.id.category_iv);
        my_package_iv = (ImageView) findViewById(R.id.my_package_iv);
//        textView1=(TextView)findViewById(R.id.textView1);
//        textView2=(TextView)findViewById(R.id.textView2);
//        textView3=(TextView)findViewById(R.id.textView3);
        //每个继承的activity都要写该方法,第一个参数为需要设置的view,
        // 第二个参数为要设置的view的颜色,颜色从color.xm两种取,不设置颜色则设置为-1
        setStatusBar(status_bar_tv, R.color.theme_color);
        if (HaojiajiaoApplication.ifIsSignUp) {
            new Thread(networkTask).start();
        }

        if (HaojiajiaoApplication.IFLOGIN) {
            Log.e("MainAct","ifLogin: true");
            if (HaojiajiaoApplication.ISSTATE) {
                getTeacherInfoByUserName();
                Log.e("MainAct","get teacherInfobyName");
            } else {
                getParentInfoByUserName();
                Log.e("MainAct","get parentInfobyName");
            }
        }
        else{
            Log.e("MainAct","ifLogin: false");
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

    private void showDialog() {
        if (mDialog == null) {
            mDialog = new CustomDialog(this, R.style.MyDialog,
                    R.layout.layout_dialog);
            mDialog.show();
            dialogContent = (TextView) mDialog
                    .findViewById(R.id.dialog_content_tv);
            dialogContent.setText("Sure to exit?");
            dialogOK = (LinearLayout) mDialog
                    .findViewById(R.id.ok_btn_layout);
            dialogCancel = (LinearLayout) mDialog
                    .findViewById(R.id.cancle_btn_layout);
            dialogOK.setOnClickListener(this);
            dialogCancel.setOnClickListener(this);
        } else {
            mDialog.show();
        }
    }

    // 初始化颜色值
    private void initColor() {
//        color_pressed = Color.parseColor("#4CB6D8");
//        color_no_pressed = Color.parseColor("#8A8C98");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homepage_ll:// 首页
                if (HOME_PAGETAG == viewPager.getCurrentItem()) {
                } else {
                    screenChange(HOME_PAGETAG);
                    viewPager.setCurrentItem(HOME_PAGETAG);
                }
                break;

            case R.id.message_ll:// 消息
                if (MESSAGETAG == viewPager.getCurrentItem()) {
                } else {
                    screenChange(MESSAGETAG);
                    viewPager.setCurrentItem(MESSAGETAG);
                }
                break;

            case R.id.my_ll://我的
                if (HaojiajiaoApplication.IFLOGIN) {
                    if (MYTAG == viewPager.getCurrentItem()) {
                    } else {
                        screenChange(MYTAG);
                        viewPager.setCurrentItem(MYTAG);
                    }
                } else {
                    new LoginDialog(this, "Sorry! Please login before make an appointment.", new LoginLisenner() {
                        @Override
                        public void login() {
                            Intent intent = new Intent(BroadCastAction.MAINACTIVITY);
                            sendBroadcast(intent);
                            Intent intenta = new Intent(MainActivity.this, WelcomeActivity.class);
                            startActivity(intenta);
                        }

                        @Override
                        public void sign() {
                            Intent intent = new Intent(BroadCastAction.MAINACTIVITY);
                            sendBroadcast(intent);
                            Intent intenta = new Intent(MainActivity.this, WelcomeActivity.class);
                            startActivity(intenta);
                        }
                    }).show();

                }

                break;
            case R.id.ok_btn_layout:
                finish();
//                JPushInterface.stopPush(getApplicationContext());
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.cancle_btn_layout:
                mDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (myMainFM != null) {
                switch (requestCode) {
                    case 196610:
                        if (data != null) {
                            //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                            Uri mImageCaptureUri = data.getData();
                            //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                            if (mImageCaptureUri != null) {
                                Bitmap image;
                                try {
                                    //这个方法是根据Uri获取Bitmap图片的静态方法
                                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                                    if (image != null) {
//                                        photo.setImageBitmap(image);
                                        myMainFM.getRequestCodeFromActivity(image);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Bundle extras = data.getExtras();
                                if (extras != null) {
                                    //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                                    Bitmap image = extras.getParcelable("data");
                                    if (image != null) {
                                        myMainFM.getRequestCodeFromActivity(image);
                                    }
                                }
                            }

                        }
                        break;
//                        ;
                    case 196609:
//                      if (data != null) {
                        //取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                        Uri mImageCaptureUri = data.getData();
                        //返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                        if (mImageCaptureUri != null) {
                            Bitmap image;
                            try {
                                //这个方法是根据Uri获取Bitmap图片的静态方法
                                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);
                                if (image != null) {
                                    myMainFM.getRequestCodeFromActivity(image);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Bundle extras = data.getExtras();
                            if (extras != null) {
                                //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                                Bitmap image = extras.getParcelable("data");
                                if (image != null) {
                                    myMainFM.getRequestCodeFromActivity(image);
                                }
                            }
                        }
                        break;
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    // 管理主界面屏幕切换。
    public void screenChange(int num) {
        switch (num) {
            case HOME_PAGETAG://首页
                setColor(1);

                HaojiajiaoApplication.screenBack = HOME_PAGETAG;
                break;
            case MESSAGETAG://消息
                setColor(2);
                HaojiajiaoApplication.screenBack = MYTAG;
                break;
            case MYTAG:// 我的
                setColor(3);
                HaojiajiaoApplication.screenBack = MYTAG;
                break;
        }
    }

    //设置颜色
    private void setColor(int position) {
        switch (position) {
            case 1:
                homepage_ll.setBackgroundColor(getResources().getColor(R.color.tab_press_color));
                message_ll.setBackgroundColor(getResources().getColor(R.color.tab_no_press_color));
                my_ll.setBackgroundColor(getResources().getColor(R.color.tab_no_press_color));
//                main_page_iv.setBackgroundResource(R.mipmap.tab12);
//                category_iv.setBackgroundResource(R.mipmap.tab21);
//                my_package_iv.setBackgroundResource(R.mipmap.tab31);
//                textView1.setTextColor(color_pressed);
//                textView2.setTextColor(color_no_pressed);
//                textView3.setTextColor(color_no_pressed);
                break;
            case 2:
                homepage_ll.setBackgroundColor(getResources().getColor(R.color.tab_no_press_color));
                message_ll.setBackgroundColor(getResources().getColor(R.color.tab_press_color));
                my_ll.setBackgroundColor(getResources().getColor(R.color.tab_no_press_color));
//                main_page_iv.setBackgroundResource(R.mipmap.tab11);
//                category_iv.setBackgroundResource(R.mipmap.tab22);
//                my_package_iv.setBackgroundResource(R.mipmap.tab31);
//                textView1.setTextColor(color_no_pressed);
//                textView2.setTextColor(color_pressed);
//                textView3.setTextColor(color_no_pressed);
                break;
            case 3:
                homepage_ll.setBackgroundColor(getResources().getColor(R.color.tab_no_press_color));
                message_ll.setBackgroundColor(getResources().getColor(R.color.tab_no_press_color));
                my_ll.setBackgroundColor(getResources().getColor(R.color.tab_press_color));
//                main_page_iv.setBackgroundResource(R.mipmap.tab11);
//                category_iv.setBackgroundResource(R.mipmap.tab21);
//                my_package_iv.setBackgroundResource(R.mipmap.tab32);
//                textView1.setTextColor(color_no_pressed);
//                textView2.setTextColor(color_no_pressed);
//                textView3.setTextColor(color_pressed);
                break;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showDialog();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            } else if (BroadCastAction.MAINACTIVITY.equals(intent.getAction())) {
                finish();
//                Intent intenta = new Intent(MainActivity.this, WelcomeActivity.class);
//                startActivity(intenta);
            }

        }
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction(BroadCastAction.MAINACTIVITY);
        registerReceiver(mMessageReceiver, filter);
    }

    private void setCostomMsg(String msg) {
//        if (null != msgText) {
//            msgText.setText(msg);
//            msgText.setVisibility(android.view.View.VISIBLE);
//        }
    }

    //获取用户的token
    private void getToken() {
        Map<String, String> dataParas = new HashMap<>();
        //设置用户名
        dataParas.put("UserName", HaojiajiaoApplication.userName);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getToken, dataParas, RequestTag.getToken);
    }

    //获取教师用户的token
    private void getTeacherToken() {
        Map<String, String> dataParas = new HashMap<>();
        //设置用户名
        dataParas.put("teacherUserName", HaojiajiaoApplication.userName);
        requestHandler.sendHttpRequestWithParam("http://121.42.140.239/hjj/php/getTeacherToken", dataParas, RequestTag.getTeacherToken);
    }

    //获取家长用户的token
    private void getParentToken() {
        Map<String, String> dataParas = new HashMap<>();
        //设置用户名
        dataParas.put("parentUserName", HaojiajiaoApplication.userName);
        requestHandler.sendHttpRequestWithParam("http://121.42.140.239/hjj/php/getParentToken", dataParas, RequestTag.getParentToken);
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        super.onRequestError(response);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        if (response.requestTag.toString().equals("getToken")) {
            String dataStr = response.responseString;
            dataStr = dataStr.replace("\\", "").replace("u003d", "=");
            try {
                JSONObject total1 = new JSONObject(dataStr);
                String code = total1.optString("result");
                if(code.equals("403")){
                    Log.e("MainActivity:","~~~~~~~~~~get token 403!~~~~~~~");
                    if(HaojiajiaoApplication.ISSTATE){
                        getTeacherTokenPhp();
                    }
                    else {
                        getParentTokenPhp();
                    }
                }
                else{
                    HaojiajiaoApplication.token = code;
                    Log.e("MainAct:","~~~~~~~get token: "+code);
                    initViewpager();
                    SharedPreferences.Editor editor = getSharedPreferences("UserToken",MODE_PRIVATE).edit();
                    editor.putString(HaojiajiaoApplication.userName,code);
                    editor.commit();
                }

            } catch (Exception e) {
                e.printStackTrace();
                HaojiajiaoApplication.token = "-1";
                Log.e("MainActivity:","~~~~~~~~~~get token error!~~~~~~~");
                if(HaojiajiaoApplication.ISSTATE){
                    getTeacherTokenPhp();
                }
                else {
                    getParentTokenPhp();
                }
            }
        } else if(response.requestTag.toString().equals("getTeacherToken")){
            String dataStr = response.responseString;
            dataStr = dataStr.replace("\\", "").replace("u003d", "=");
            try {
                JSONObject total1 = new JSONObject(dataStr);
                String code = total1.optString("token");
                HaojiajiaoApplication.token = code;
                Log.e("MainAct:","~~~~~~~get teacher token: "+code);
            } catch (Exception e) {
                e.printStackTrace();
                HaojiajiaoApplication.token = "-1";
                Log.e("MainActivity:","~~~~~~~~~~get teacher token error!~~~~~~~");
            }
            initViewpager();
        }else if(response.requestTag.toString().equals("getParentToken")){
            String dataStr = response.responseString;
            //Log.e("MainAct:","dataStr:"+dataStr);
            dataStr = dataStr.replace("\\", "").replace("u003d", "=");
            try {
                JSONObject total1 = new JSONObject(dataStr);
                String code = total1.optString("token");
                HaojiajiaoApplication.token = code;
                Log.e("MainAct:","~~~~~~~get parent token: "+code);
            } catch (Exception e) {
                e.printStackTrace();
                HaojiajiaoApplication.token = "403";
                Log.e("MainActivity:","~~~~~~~~~~get parent token error!~~~~~~~");
                Log.e("MainAct:",dataStr);
            }
            initViewpager();
        } else if (response.requestTag.toString().equals("getTeacherInfoByUserName")) {
            String dataStr = response.responseString;
            Log.d("data", dataStr);
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                HaojiajiaoApplication.userId = total1.getInt("id");
                HaojiajiaoApplication.picUrl = total1.getString("teacherPortrait");
                HaojiajiaoApplication.name = total1.getString("teacherName");
                Log.e("MainActivity","init teacher Url: "+total1.getString("teacherPortrait"));

            } catch (Exception e) {

            }

        } else if (response.requestTag.toString().equals("getParentInfoByUserName")) {
            String dataStr = response.responseString;
            Log.d("data", dataStr);
            Log.e("MainAct","get parent response!");
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                JSONObject jsonArray = total1.getJSONObject("result");
                HaojiajiaoApplication.userId = jsonArray.getInt("id");
                HaojiajiaoApplication.picUrl = jsonArray.getString("parentPortrait");
                Log.e("MainActivity","init parent Url:"+jsonArray.getString("parentPortrait"));
                HaojiajiaoApplication.name = jsonArray.getString("parentName");
            } catch (Exception e) {

            }
        }else if (response.requestTag.toString().equals("getTeacherInfoByUserNameForRY")) {
            String dataStr = response.responseString;
            try {
                JSONObject total1 = new JSONObject(dataStr);

                Log.e("response:", "teacher: R_userName:" + HaojiajiaoApplication.R_userName);
                if(total1!=null && (!total1.optString("teacherUserName").equals(""))){
                    HaojiajiaoApplication.R_name = total1.optString("teacherName");
                    HaojiajiaoApplication.R_userName = total1.optString("teacherUserName");
                    HaojiajiaoApplication.R_picUrl = total1.optString("teacherPortrait");
                    HaojiajiaoApplication.response = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("MainActivity", "get teacherIcon error!!!");
            }
        }else if (response.requestTag.toString().equals("getParentInfoByUserNameForRY")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                JSONObject jsonArray = total1.getJSONObject("result");
                if (jsonArray!=null && (!jsonArray.getString("parentUserName").equals(""))) {
                    HaojiajiaoApplication.R_name = jsonArray.getString("parentName");
                    HaojiajiaoApplication.R_picUrl = jsonArray.getString("parentPortrait");
                    HaojiajiaoApplication.R_userName = jsonArray.getString("parentUserName");
                    Log.e("response:", "parent: R_userName:" + HaojiajiaoApplication.R_userName);
                    HaojiajiaoApplication.response = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("MainActivity", "get parentIcon error!!!");
            }
        }

    }

    //新增上传文件接口
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            String picPath = com.shanghai.haojiajiao.util.CrashHandlerUtils.FileUtil.getImagePath() + "/crop_photo.jpg";
            uploadFile(picPath);
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

    }

}

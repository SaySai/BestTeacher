package com.shanghai.haojiajiao.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.DayAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseActivity;
import com.shanghai.haojiajiao.model.BroadCastAction;
import com.shanghai.haojiajiao.util.DateUtil;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.util.ToastUtil;
import com.shanghai.haojiajiao.weight.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * 支付和生成订单
 */
public class OrderSthingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_finish;
    private TextView tv_times;
    private TextView tv_number;
    private TextView tv_teacher;
    private TextView tv_orderTime;
    private TextView tv_money;
    private TextView tv_total;
    private TextView tv_goBooking;
    private TextView tv_backHome;
    private TextView tv_titleName, tv_goPay, tv_photo;
    private ImageView iv_check;
    private LinearLayout ll_pay;
    private FinishReceiver receiver;
    private LoadingDialog loadingDialog;
    private String OrderState = "1";
    //父id  教师id   订单日期    订单          订单日期     费用            时间段
    private String ParentId = "", TeacherId = "", OrderDay = "", OrderCharge = "", TimeSegment = "",userName="";
    //............................................................支付相关............................................................
    private static final String TAG = "paymentExample";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * <p/>
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * <p/>
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "ATm1-as-gxQeQKVVSF33B7qdY5Bmgm6IP9EpMU6urv054mwj3xvzvbgd6W5UbchW87t1hwcoc3pJvu5_";
            //sandbox:"AUop2cRlpPpIBHAxrgMvyXOG-gjrt5DWwSPi6vq2go22Wx8OQII4enF6C1yuggg_sJd_6o2ysxkVReuB";
    //init:"Afrh5m0tdY9n2yJ0I1-DQI67E8gfSib3-ee4KH1LSAFEJ7qV-rem2RBHfuAOQmr4B5JalZ44WxTa52cf";
    private String orderNumber = null;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    //..............................................................................................................
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_thing);
        receiver = new FinishReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastAction.ORDERSTHINGACTIVITY);
        registerReceiver(receiver, intentFilter);
        loadingDialog = new LoadingDialog(OrderSthingActivity.this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        iv_check = (ImageView) findViewById(R.id.iv_check);//是否有支付板块
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_titleName = (TextView) findViewById(R.id.tv_titleName);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        tv_times = (TextView) findViewById(R.id.tv_times);
        tv_photo = (TextView) findViewById(R.id.tv_photo);
        tv_photo.setOnClickListener(this);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_teacher = (TextView) findViewById(R.id.tv_teacher);
        tv_orderTime = (TextView) findViewById(R.id.tv_orderTime);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_goBooking = (TextView) findViewById(R.id.tv_goBooking);
        tv_goPay = (TextView) findViewById(R.id.tv_goPay);
        tv_goPay.setOnClickListener(this);
        if (HaojiajiaoApplication.ISSTATE) {
            tv_goBooking.setVisibility(View.GONE);
        }
        tv_goBooking.setOnClickListener(this);
        tv_backHome = (TextView) findViewById(R.id.tv_backHome);
        tv_backHome.setOnClickListener(this);
        //该页面有两个页面，一种是已经完成订单:订单详情，另一种是未完成订单：支付信息
        //0订单详情
        // 1支付信息
        // 2支付信息
        if (getIntent().getIntExtra("teb", 0) == 0) {
            tv_finish.setVisibility(View.VISIBLE);
            ll_pay.setVisibility(View.GONE);
            if (getIntent().getIntExtra("finish", 0) == 0) {//0已经评价1未平价
                tv_goBooking.setVisibility(View.VISIBLE);
            } else {
                tv_goBooking.setVisibility(View.GONE);
            }
        } else {
            tv_finish.setVisibility(View.GONE);
            ll_pay.setVisibility(View.VISIBLE);
        }
        orderNumber = this.getIntent().getStringExtra("orderNumber");
        ParentId = this.getIntent().getIntExtra("ParentId", 0) + "";
        if (ParentId == null) {
            ParentId = this.getIntent().getStringExtra("ParentId");
        }
        TeacherId = this.getIntent().getIntExtra("TeacherId", 0) + "";
        if (TeacherId == null) {
            TeacherId = this.getIntent().getStringExtra("TeacherId");
        }
        OrderDay = this.getIntent().getStringExtra("OrderDay");
        OrderCharge = this.getIntent().getStringExtra("OrderCharge");
        TimeSegment = this.getIntent().getStringExtra("TimeSegment");
        OrderState = this.getIntent().getStringExtra("OrderState");
        userName=this.getIntent().getStringExtra("userName");
        if (orderNumber == null) {
            orderNumber = DateUtil.getCurrentDate(DateUtil.dateFormatChuo);
            tv_number.setText("Order No: " + orderNumber + ParentId + "" + TeacherId);
        } else {
            tv_number.setText("Order No: " + orderNumber);
        }
        tv_total.setText("Price: " + OrderCharge);
        tv_finish.setVisibility(View.VISIBLE);
        getTeacherById();
        getParentById();
        loadingDialog.show();
        if (OrderState != null) {
            if (OrderState.equals("1")) {
                tv_finish.setText("BOOKING");
            } else if (OrderState.equals("2")) {
                tv_finish.setText("BOOKED");

            } else if (OrderState.equals("3")) {
                tv_finish.setText("Coordination");

            } else if (OrderState.equals("4")) {

                tv_finish.setText("DealFailed");
            } else if (OrderState.equals("5")) {

                tv_finish.setText("DealFailed");
            } else if (OrderState.equals("6")) {
                tv_finish.setText("DealDone");
            } else {
            }
        } else {
            tv_finish.setText("NotDone");
        }
        tv_orderTime.setText("Time: " + OrderDay + getResult(DayAdapter.hours, DayAdapter.abc, TimeSegment));
        //.....................................支付相关.................................................................
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        //..............................................................................................................

        //融云相关start..................................................................................................................
        /**
         * IMKit SDK调用第二步
         *
         * 建立与服务器的连接
         *
         */
        /*Log.e("OrderSthingAct:","token:"+HaojiajiaoApplication.token);
        RongIM.connect(HaojiajiaoApplication.token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
                Log.e("OrderThingAct","---onTokenIncorrect---"+HaojiajiaoApplication.token);
            }

            @Override
            public void onSuccess(String userId) {
                Log.e("MainActivity", "——onSuccess— -" + userId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("MainActivity", "——onError— -" + errorCode);
            }
        });*/
        //融云相关end.................................................................................................................
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switchView(v.getId());
    }

    private void switchView(int id) {
        switch (id) {
            case R.id.tv_goBooking:
                Intent intent = new Intent(this, EvaluateActivity.class);
                intent.putExtra("ParentId", ParentId + "");
                intent.putExtra("TeacherId", TeacherId + "");
                intent.putExtra("OrderDay", OrderDay);
                startActivity(intent);
                break;
            case R.id.tv_backHome:
                Intent msgIntent = new Intent(BroadCastAction.ORDERSTHINGACTIVITY);
                sendBroadcast(msgIntent);
                break;
            case R.id.tv_goPay:
//                tv_finish.setVisibility(View.VISIBLE);
//                ll_pay.setVisibility(View.GONE);
//                tv_goBooking.setVisibility(View.VISIBLE);
                //去支付
                onBuyPressed();
                //在支付的同时调用接口生成订单
                addOrder();
                break;
            case R.id.tv_photo:
                //启动聊天
                if (RongIM.getInstance() != null) {
                    if(userName!=null&&(!userName.equals(""))) {
                        RongIM.getInstance().startPrivateChat(this, userName, "hello");
                    }else {
                        ToastUtil.showShort(OrderSthingActivity.this,"教师用户名为空");
                    }
                }
                break;

        }
    }

    private void showEnd() {

    }


    //.....................................................支付相关.............................................................
    private void onBuyPressed() {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    //客户要求使用澳大利亚币来进行支付
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("1.75"), "AUD", "sample item",
                paymentIntent);
    }

    /*
 * This method shows use of optional payment details and item list.
 */
    private PayPalPayment getStuffToBuy(String paymentIntent) {
        //--- include an item list, payment amount details
        PayPalItem[] items =
                {
                        new PayPalItem("sample item #1", 2, new BigDecimal("87.50"), "USD",
                                "sku-12345678"),
                        new PayPalItem("free sample item #2", 1, new BigDecimal("0.00"),
                                "USD", "sku-zero-price"),
                        new PayPalItem("sample item #3 with a longer name", 6, new BigDecimal("37.99"),
                                "USD", "sku-33333")
                };
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal shipping = new BigDecimal("7.21");
        BigDecimal tax = new BigDecimal("4.67");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, "USD", "sample item", paymentIntent);
        payment.items(items).paymentDetails(paymentDetails);

        //--- set other optional fields like invoice_number, custom field, and soft_descriptor
        payment.custom("This is text that will be associated with the payment that the app can use.");
        return payment;
    }


//...........................................................................................................................

    //增加订单接口
    public void addOrder() {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("ParentId", ParentId);
        dataParas.put("TeacherId", TeacherId);
        dataParas.put("OrderDay", OrderDay);
        dataParas.put("OrderCharge", OrderCharge);
        dataParas.put("TimeSegment", TimeSegment);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.addOrder, dataParas, RequestTag.addOrder);
    }

    private void getTeacherById() {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("TeacherId", TeacherId);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getTeacherById, dataParas, RequestTag.getTeacherById);
    }

    private void getParentById() {
        Map<String, String> dataParas = new HashMap<>();
        dataParas.put("ParentId", ParentId);
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getParentById, dataParas, RequestTag.getParentById);
    }

    @Override
    public void onRequestError(ResponseOwn response) {
        super.onRequestError(response);
        loadingDialog.dismiss();
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        loadingDialog.dismiss();
        if (response.requestTag.toString().equals("addOrder")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                String result = total1.optString("result");
                if (!result.trim().equals("")) {
                    if (result.equals("1")) {
                        ToastUtil.showShort(OrderSthingActivity.this, "生成订单成功!");
                        OrderSthingActivity.this.finish();
                    } else {
                        ToastUtil.showShort(OrderSthingActivity.this, "生成订单失败!");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (response.requestTag.toString().equals("getTeacherById")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                JSONObject result = total1.getJSONObject("result");
                tv_teacher.setText("预约教师：" + result.optString("teacherName"));
                if (!HaojiajiaoApplication.ISSTATE) {
                    tv_photo.setText(result.optString("teacherTel"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (response.requestTag.toString().equals("getParentById")) {
            String dataStr = response.responseString;
            JSONObject total1 = null;
            try {
                total1 = new JSONObject(dataStr);
                JSONObject result = total1.getJSONObject("result");
                if (HaojiajiaoApplication.ISSTATE) {
                    tv_photo.setText(result.optString("parentTel"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadCastAction.ORDERSTHINGACTIVITY.equals(intent.getAction())) {
                Intent intent1 = new Intent(BroadCastAction.BOOKINGSETTINGDAYACTIVITY);
                sendBroadcast(intent1);
                finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public static String getResult(String[] hours, String[] abc, String in) {
        String[] result = new String[in.length()];
        int p = 0;
        for (int i = 0; i < in.length(); i++) {
            for (int j = 0; j < abc.length; j++) {
                if (String.valueOf(in.charAt(i)).equals(abc[j])) {
                    result[p] = hours[j];
                    p++;
                }
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int b = 0; b < result.length; b++) {
            stringBuffer.append(result[b]);
            if (b != result.length - 1) {
                stringBuffer.append(",");
            }

        }
        return stringBuffer.toString();
    }

}

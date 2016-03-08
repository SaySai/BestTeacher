package com.shanghai.haojiajiao.fragment.message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.activity.EvaluateListActivity;
import com.shanghai.haojiajiao.activity.LeaveActivity;
import com.shanghai.haojiajiao.activity.NoticeActivity;
import com.shanghai.haojiajiao.activity.SystemMsgActivity;
import com.shanghai.haojiajiao.adapter.NoticeAdapter;
import com.shanghai.haojiajiao.adapter.NoticesAdapter;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.BaseFragment;
import com.shanghai.haojiajiao.model.NoticeModel;
import com.shanghai.haojiajiao.util.HttpUtil.GoodTeacherURL;
import com.shanghai.haojiajiao.util.HttpUtil.RequestTag;
import com.shanghai.haojiajiao.util.HttpUtil.ResponseOwn;
import com.shanghai.haojiajiao.weight.LoginDialog;
import com.shanghai.haojiajiao.weight.LoginLisenner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * 消息主界面
 *
 * @author cyj
 */
public class MessageMainFM extends BaseFragment implements View.OnClickListener {
    private View contentView = null;
    private TextView tv_notice;
    private TextView tv_message;
    private ListView lv_list;
    private NoticesAdapter noticeAdapter;

    //tab2
    private TextView tv_comments;
    private TextView tv_leave;
    private TextView tv_system;

    private TextView tv_leaveParent;
    private TextView tv_systemParent;
    private int index;
    private LinearLayout ll_teacher, ll_parent;
    //存储数据的arraylist
    private List<NoticeModel> noticeModelArrayList=new ArrayList<>();
    private boolean isFristInto=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragement_message_main, container, false);
            initView();
            switchTab(R.id.tv_notice);
//            noticeAdapter = new NoticesAdapter(noticeModelArrayList,MessageMainFM.this.getActivity());
            lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NoticeModel noticeModel=noticeModelArrayList.get(position);
                    Intent intent = new Intent(getActivity(), NoticeActivity.class);
                    intent.putExtra("title",noticeModel.noticeTitle);
                    intent.putExtra("date",noticeModel.noticeTime);
                    intent.putExtra("picUrl", noticeModel.noticeUrl);
                    intent.putExtra("content",noticeModel.noticeContent);
                    getActivity().startActivity(intent);
                }
            });

            //获得公告信息
            getNotice();
//            if (index == 0) {
//                ll_teacher.setVisibility(View.GONE);
//                ll_parent.setVisibility(View.GONE);
//            } else {
//                ll_teacher.setVisibility(View.GONE);
//                ll_parent.setVisibility(View.GONE);
//            }


            //融云相关start..................................................................................................................
            //String Token = HaojiajiaoApplication.token;//test
            /**
             * IMKit SDK调用第二步
             *
             * 建立与服务器的连接
             *
             */
//            if (MainActivity.IF_MSG_IS_INIT_FIRST == 0) {
//                MainActivity.IF_MSG_IS_INIT_FIRST = 1;
            Log.e("MessageFMAct:","token:"+HaojiajiaoApplication.token);
                RongIM.connect(HaojiajiaoApplication.token, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        //Connect Token 失效的状态处理，需要重新获取 Token
                        Log.e("MessageMainFM","---onTokenIncorrect----"+HaojiajiaoApplication.token);
                    }

                    @Override
                    public void onSuccess(String userId) {
                        Log.e("MessageActivity", "——onSuccess— -" + userId);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.e("MessageActivity", "——onError— -" + errorCode);
                    }
                });
                //融云相关end.................................................................................................................

//            }

        }
        return contentView;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switchTab(v.getId());
        switchTab2Teacher(v.getId());
        switchTab2Parent(v.getId());
    }

    /**
     * 初始化view
     */
    private void initView() {
        tv_notice = (TextView) contentView.findViewById(R.id.tv_notice);
        tv_notice.setOnClickListener(this);
        tv_message = (TextView) contentView.findViewById(R.id.tv_message);
        tv_message.setOnClickListener(this);
        lv_list = (ListView) contentView.findViewById(R.id.lv_list);
        tv_comments = (TextView) contentView.findViewById(R.id.tv_comments);
        tv_comments.setOnClickListener(this);
        tv_leave = (TextView) contentView.findViewById(R.id.tv_leave);
        if (HaojiajiaoApplication.ISSTATE) {
            tv_leave.setVisibility(View.VISIBLE);
        } else {
            tv_leave.setVisibility(View.GONE);
        }
        tv_leave.setOnClickListener(this);
        tv_system = (TextView) contentView.findViewById(R.id.tv_system);
        tv_system.setOnClickListener(this);
        tv_leaveParent = (TextView) contentView.findViewById(R.id.tv_leaveParent);
        tv_leaveParent.setOnClickListener(this);
        tv_systemParent = (TextView) contentView.findViewById(R.id.tv_systemParent);
        tv_systemParent.setOnClickListener(this);
        ll_teacher = (LinearLayout) contentView.findViewById(R.id.ll_teacher);
        ll_parent = (LinearLayout) contentView.findViewById(R.id.ll_parent);
    }

    /**
     * 切换tab
     *
     * @param number
     */
    private void switchTab(int number) {
        switch (number) {
            case R.id.tv_notice:
                //从网络获取公告
                getNotice();
                tv_notice.setTextAppearance(getActivity(), R.style.text_bold);
                tv_notice.setTextColor(getResources().getColor(R.color.tab_no_press_color));
                tv_notice.setBackground(getResources().getDrawable(R.drawable.shape_notice_backgroud_green));
                tv_message.setTextAppearance(getActivity(), R.style.text_normal);
                tv_message.setTextColor(Color.WHITE);
                tv_message.setBackground(getResources().getDrawable(R.drawable.shape_message_backgroud_white));
                lv_list.setVisibility(View.VISIBLE);
                ll_teacher.setVisibility(View.GONE);
                ll_parent.setVisibility(View.GONE);
                break;
            case R.id.tv_message:
                if (HaojiajiaoApplication.IFLOGIN) {
                    tv_notice.setTextAppearance(getActivity(), R.style.text_normal);
                    tv_notice.setTextColor(Color.WHITE);
                    tv_notice.setBackground(getResources().getDrawable(R.drawable.shape_notice_backgroud_white));
                    tv_message.setTextAppearance(getActivity(), R.style.text_bold);
                    tv_message.setTextColor(getResources().getColor(R.color.tab_no_press_color));
                    tv_message.setBackground(getResources().getDrawable(R.drawable.shape_message_backgroud_green));
                    lv_list.setVisibility(View.GONE);
                  if (HaojiajiaoApplication.ISSTATE == true) {
                ll_teacher.setVisibility(View.VISIBLE);
                ll_parent.setVisibility(View.GONE);
            } else {
                      ll_teacher.setVisibility(View.GONE);
                      ll_parent.setVisibility(View.VISIBLE);
            }
                } else {
                    new LoginDialog(getActivity(), "登录后才可以预约哦！", new LoginLisenner() {
                        @Override
                        public void login() {
                            Intent i = getActivity().getPackageManager()
                                    .getLaunchIntentForPackage(getActivity().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }

                        @Override
                        public void sign() {
                            Intent i = getActivity().getPackageManager()
                                    .getLaunchIntentForPackage(getActivity().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }).show();
                }

                break;
        }
    }

    private void switchTab2Teacher(int id) {
        switch (id) {
            case R.id.tv_comments:
                Intent intent = new Intent(getActivity(), EvaluateListActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.tv_leave:
//                Intent intent1 = new Intent(getActivity(), LeaveActivity.class);
//                getActivity().startActivity(intent1);
                //启动会话列表界面
                if (RongIM.getInstance() != null) {

                    RongIM.getInstance().startConversationList(this.getActivity());

                }

                break;
            case R.id.tv_system:
                Intent intent3 = new Intent(getActivity(), SystemMsgActivity.class);
                getActivity().startActivity(intent3);
                break;
        }
    }

    private void switchTab2Parent(int id) {
        switch (id) {
            case R.id.tv_leaveParent:
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startConversationList(this.getActivity());
                }
                break;
            case R.id.tv_systemParent:
                Intent intent3 = new Intent(getActivity(), SystemMsgActivity.class);
                getActivity().startActivity(intent3);
                break;
        }
    }

   //获得公告信息
    public void getNotice() {
        Map<String, String> dataParas = new HashMap<String, String>();
        requestHandler.sendHttpRequestWithParam(GoodTeacherURL.getNotice, dataParas, RequestTag.getNotice);
    }
    @Override
    public void onRequestError(ResponseOwn response) {
        super.onRequestError(response);
    }

    @Override
    public void onRequestSuccess(ResponseOwn response) {
        if (response.requestTag.toString().equals("getNotice")) {
            String dataStr = response.responseString;
            try {
                if(noticeModelArrayList.size()>0)
                {
                    noticeModelArrayList.clear();
                }
                JSONObject total1 = new JSONObject(dataStr);
                JSONArray jSONArray=total1.optJSONArray("result");
                int temp=jSONArray.length();
                for(int i=0;i<temp;i++)
                {
                    JSONObject jsonOne=jSONArray.optJSONObject(i);
                    NoticeModel noticeModel=new NoticeModel();
                    noticeModel.id=jsonOne.optString("id");
                    noticeModel.noticeContent=jsonOne.optString("noticeContent");
                    noticeModel.noticeTime=jsonOne.optString("noticeTime");
                    noticeModel.noticeTitle=jsonOne.optString("noticeTitle");
                    noticeModel.noticeUrl=jsonOne.optString("noticeUrl");
                    noticeModelArrayList.add(noticeModel);
                }
                if(isFristInto) {
                    noticeAdapter = new NoticesAdapter(noticeModelArrayList, MessageMainFM.this.getActivity());
                    lv_list.setAdapter(noticeAdapter);
                    isFristInto=false;
                }else {
                    noticeAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }





}

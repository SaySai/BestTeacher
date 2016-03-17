package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.activity.TeacherActivity;
import com.shanghai.haojiajiao.app.HaojiajiaoApplication;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.TeacherListModel;
import com.shanghai.haojiajiao.util.BitmapUtil;
import com.shanghai.haojiajiao.util.UilUtil;
import com.shanghai.haojiajiao.weight.CircleImageView;
import com.shanghai.haojiajiao.weight.LoginDialog;
import com.shanghai.haojiajiao.weight.LoginLisenner;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2015/12/17.
 */
public class
TeacherPageAdatper extends AdapterBase<TeacherListModel> {
    public TeacherPageAdatper(Activity act) {
        super(act);
    }
    private DisplayImageOptions options;
    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.teacher_list, null);
        options = UilUtil.getOptions();
        TeacherListModel teacherListModel = (TeacherListModel) getItem(position);
        final CircleImageView iv_userIcon = (CircleImageView) convertView.findViewById(R.id.iv_userIcon);
        //ImageView iv_userIcon = (ImageView) convertView.findViewById(R.id.iv_userIcon);
        String uri=teacherListModel.getPicUrl();
        URL Url =null;
        try {
            Url = new URL(uri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BitmapUtil.onLoadImage(this.getAct().getApplicationContext(),Url, new BitmapUtil.OnLoadImageListener() {
            @Override
            public void OnLoadImage(Bitmap bitmap, String bitmapPath) {
                // TODO Auto-generated method stub
                Log.e("TeacherPageAdatper","get bitmap!");
                if(bitmap!=null){
                    iv_userIcon.setImageBitmap(bitmap);
                }
            }
        });
        //iv_userIcon.setImageBitmap(BitmapUtil.returnBitMap(this.getAct().getApplicationContext(),uri));
        /*if(uri!=null){
            ImageLoader.getInstance().displayImage(uri, iv_userIcon, options);
        }*/

        TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
        tv_username.setText(teacherListModel.getTeacherName());
        TextView tv_ratingNumber = (TextView) convertView.findViewById(R.id.tv_ratingNumber);
        tv_ratingNumber.setText(teacherListModel.getTeacherRate() + "");
        TextView tv_class = (TextView) convertView.findViewById(R.id.tv_class);
        TextView tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
        if (teacherListModel.getTeacherLessen() != null) {
            String lesson = teacherListModel.getTeacherLessen();
            if (lesson.indexOf("-") != -1) {
                String[] res = lesson.split("-");
                StringBuffer stringBuffer1 = new StringBuffer();
                for (int i = 0; i < res.length; i++) {
                    stringBuffer1.append(res[i]);
                    //stringBuffer1.append(" ");
                    //stringBuffer1.append(getCourse2(Course2Adapter.testCourse, Course2Adapter.testCourseType, res[i]));
                    if (i != res.length - 1) {
                        stringBuffer1.append(", ");
                    }
                }
                tv_class.setText(stringBuffer1);
            } else {
                tv_class.setText(lesson);
                //tv_subject.setText(getCourse2(Course2Adapter.testCourse, Course2Adapter.testCourseType, lesson));
            }
        }
        TextView tv_evaluate = (TextView) convertView.findViewById(R.id.tv_evaluate);
        LinearLayout ll_booking_layout = (LinearLayout) convertView.findViewById(R.id.ll_booking_layout);
        TextView tv_money = (TextView) convertView.findViewById(R.id.tv_money);
        tv_money.setText("$ "+teacherListModel.getTeacherMoney()+"/H");
        ImageView ratingBar = (ImageView) convertView.findViewById(R.id.ratingBar);

        switchRatingBar(ratingBar, teacherListModel.getTeacherRate());
        tv_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherListModel teacherListModel1 = (TeacherListModel) getItem(position);
                Intent intent = new Intent(getAct(), TeacherActivity.class);
                intent.putExtra("tab", 1);
                intent.putExtra("TeacherUserName", teacherListModel1.getTeacherUserName());
                intent.putExtra("TeacherId", teacherListModel1.getId());
                intent.putExtra("TeacherName",teacherListModel1.getTeacherName());
                getAct().startActivity(intent);
            }
        });
        if (HaojiajiaoApplication.ISSTATE) {
            ll_booking_layout.setVisibility(View.INVISIBLE);
        }
        ll_booking_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HaojiajiaoApplication.IFLOGIN) {
                    if (!HaojiajiaoApplication.ISSTATE) {
                        TeacherListModel teacherListModel1 = (TeacherListModel) getItem(position);
                        Intent intent = new Intent(getAct(), TeacherActivity.class);
                        intent.putExtra("tab", 2);
                        intent.putExtra("TeacherUserName", teacherListModel1.getTeacherUserName());
                        intent.putExtra("TeacherId", teacherListModel1.getId());
                        intent.putExtra("TeacherName",teacherListModel1.getTeacherName());
                        getAct().startActivity(intent);
                    }

                } else {
                    new LoginDialog(getAct(), "Login before you can make an appointment oh!", new LoginLisenner() {
                        @Override
                        public void login() {
                            Intent i = getAct().getPackageManager()
                                    .getLaunchIntentForPackage(getAct().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getAct().startActivity(i);
                        }

                        @Override
                        public void sign() {
                            Intent i = getAct().getPackageManager()
                                    .getLaunchIntentForPackage(getAct().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getAct().startActivity(i);
                        }
                    }).show();
                }

            }
        });
        return convertView;
    }

    private void switchRatingBar(ImageView ratingBar, double number) {
        if (number < 0.5) {
            ratingBar.setImageDrawable(getAct().getResources().getDrawable(R.drawable.star0));
        } else if (number >= 0.5 && number < 1.5) {
            ratingBar.setImageDrawable(getAct().getResources().getDrawable(R.drawable.star1));

        } else if (number >= 1.5 && number < 2.5) {
            ratingBar.setImageDrawable(getAct().getResources().getDrawable(R.drawable.star2));

        }
        if (number >= 2.5 && number < 3.5) {
            ratingBar.setImageDrawable(getAct().getResources().getDrawable(R.drawable.star3));
        }
        if (number >= 3.5 && number < 4.5) {
            ratingBar.setImageDrawable(getAct().getResources().getDrawable(R.drawable.star4));
        }
        if (number >= 4.5 && number <= 5) {
            ratingBar.setImageDrawable(getAct().getResources().getDrawable(R.drawable.star5));
        }
    }

    public static String getCourse1(String[] testCourse, String[] strs, String str) {

        if (str != null && str.length()>2) {
            for (int i = 0; i < strs.length; i++) {
                if (strs[i].equals(str.substring(0, 2))) {
                    return testCourse[i];
                }
            }
        }
        else {
            return "";
        }
        return "";

    }

    public static String getCourse2(String[] testCourse, String[] strs, String str) {
        if (str != null && str.length()>2) {
            for (int i = 0; i < strs.length; i++) {
                if (strs[i].equals(str.substring(str.length() - 2, str.length()))) {
                    return testCourse[i];
                }
            }
        } else {
            return "";
        }

        return "";
    }

    public synchronized String isConnect(String url) {
        int counts = 0;
        String succ = null; if (url == null || url.length() <= 0) { return succ; }
        while (counts < 5) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                int state = connection.getResponseCode();
                if (state == 200) {
                    succ = connection.getURL().toString();
                }
                break;
            } catch (Exception ex) {
                counts++;
                continue;
            }
        }
        return succ;
    }
}

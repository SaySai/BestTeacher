package com.shanghai.haojiajiao.weight;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.adapter.Course1Adapter;
import com.shanghai.haojiajiao.adapter.Course2Adapter;
import com.shanghai.haojiajiao.model.Course1Model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/12.
 */
public class CourseDialog extends Dialog {
    private ListView lv_course1_list, lv_course2_list;
    private Course2Adapter course2Adapter;
    private Course1Adapter course1Adapter;
    private TextView tv_login, tv_signUp;
    private String money;
    private Activity activity;
    private CourseLisenner courseLisenner;
    private int list1 = 0;
    private int list2 = 0;
    private GetPositionListener getPositionListener;

    public CourseDialog(Activity activity, CourseLisenner courseLisenner) {
        super(activity, R.style.Transparent);
        this.activity = activity;
        this.courseLisenner = courseLisenner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_dialog);

        lv_course1_list = (ListView) findViewById(R.id.lv_course1_list);
        lv_course1_list.setAdapter(course1Adapter = new Course1Adapter(activity));
        //lv_course2_list = (ListView) findViewById(R.id.lv_course2_list);
        //lv_course2_list.setAdapter(course2Adapter = new Course2Adapter(activity));
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                courseLisenner.sendList((Course1Model) course1Adapter.getItem(list1), (Course2Model) course2Adapter.getItem(list2));
                if (getPositionListener != null) {
                    getPositionListener.onPosition(list1);//, list2
                }
                dismiss();
            }
        });
        tv_signUp = (TextView) findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        course1Adapter.clearData();
        ArrayList<Course1Model> course1Models = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Course1Model course1Model = new Course1Model();
            //course1Model.setCatchCourse(i == 0);
            course1Models.add(course1Model);
        }
        course1Adapter.setData(course1Models);
        /*course2Adapter.clearData();
        ArrayList<Course2Model> course2Models = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Course2Model course2Model = new Course2Model();
            course2Model.setCatchCourse(i == 0);
            course2Models.add(course2Model);
        }
        course2Adapter.setData(course2Models);
        course2Adapter.setCourse2Color(0);*/
        lv_course1_list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                course1Adapter.setCourse1Color(list1 = position);
               // course2Adapter.setCourse2Color(list2 = 0);
            }
        });
        /*lv_course2_list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                course2Adapter.setCourse2Color(list2 = position);
            }
        });*/

    }

    public GetPositionListener getGetPositionListener() {
        return getPositionListener;
    }

    public void setGetPositionListener(GetPositionListener getPositionListener) {
        this.getPositionListener = getPositionListener;
    }


    //获得第一个选择和第二个选择的
    public interface GetPositionListener {
        public void onPosition(int fristPosition);//int secondPosition
    }

}

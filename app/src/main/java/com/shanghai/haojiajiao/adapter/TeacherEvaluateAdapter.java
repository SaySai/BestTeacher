package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.TeacherEvaluateModel;
import com.shanghai.haojiajiao.util.UilUtil;
import com.shanghai.haojiajiao.weight.CircleImageView;

/**
 * Created by Administrator on 2015/12/17.
 */
public class TeacherEvaluateAdapter extends AdapterBase<TeacherEvaluateModel> {
    public TeacherEvaluateAdapter(Activity act) {
        super(act);
    }
    private DisplayImageOptions options;
    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.teacher_evaluate_list, null);
        options = UilUtil.getOptions();
        TeacherEvaluateModel teacherEvaluateModel = (TeacherEvaluateModel) getItem(position);
        CircleImageView iv_userIcon = (CircleImageView) convertView.findViewById(R.id.iv_userIcon);
        TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
        tv_username.setText("***" + teacherEvaluateModel.getParentId() + "**x");
        TextView tv_times = (TextView) convertView.findViewById(R.id.tv_times);
        tv_times.setText(teacherEvaluateModel.getEvaluationTime()+"");
        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        tv_content.setText(teacherEvaluateModel.getEvaluationContent()+"");
        ImageView ratingBar = (ImageView) convertView.findViewById(R.id.ratingBar);
        switchRatingBar(ratingBar, teacherEvaluateModel.getEvaluationRate());
        //设置用户头像
        ImageLoader.getInstance().displayImage(teacherEvaluateModel.getPicUrl(), iv_userIcon, options);
        return convertView;
    }

    private void switchRatingBar(ImageView ratingBar, float number) {
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
}

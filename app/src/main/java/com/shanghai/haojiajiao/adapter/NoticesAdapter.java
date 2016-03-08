package com.shanghai.haojiajiao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.model.NoticeModel;

import java.util.List;

public class NoticesAdapter extends BaseAdapter {
    private List<NoticeModel> models;
    private ViewHolder holder;
    private Context context;

    public NoticesAdapter(List<NoticeModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (models != null) {
            return models.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int arg0) {

        if (models != null) {
            return models.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View contentView, ViewGroup arg2) {
        NoticeModel noticeModel = this.models.get(position);
        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.msg_notice_list, null);//将layout中的XML文件转换成view显示出来
            holder = new ViewHolder();
            holder.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            holder.tv_times = (TextView) contentView.findViewById(R.id.tv_times);
//            holder.tv_content = (TextView) contentView.findViewById(R.id.tv_content);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        holder.tv_title.setText(noticeModel.noticeTitle);
        holder.tv_times.setText(noticeModel.noticeTime);
//        holder.tv_content.setText(noticeModel.noticeContent + "");
        return contentView;
    }

    static class ViewHolder {
        TextView tv_title, tv_times, tv_content;


    }

}

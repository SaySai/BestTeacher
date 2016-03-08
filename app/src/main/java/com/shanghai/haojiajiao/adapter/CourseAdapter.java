package com.shanghai.haojiajiao.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.model.CourseModel;
import com.shanghai.haojiajiao.model.NoticeModel;

import java.util.List;

public class CourseAdapter extends BaseAdapter{
	private List<CourseModel> models;
	private ViewHolder holder;
    private Context context;
    private WhitchWillBeDel whitchWillBeDel;
	public CourseAdapter(List<CourseModel> models, Context context){
		this.models = models;
        this.context=context;
	}
	
	@Override
	public int getCount() {
		if(models != null){
			return models.size();
		}
 		return 0;
	}

	@Override
	public Object getItem(int arg0) {

		if(models != null){
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
        CourseModel courseModel=this.models.get(position);
		if(contentView == null ){
			contentView = LayoutInflater.from(context).inflate(R.layout.course_list_layout, null);//将layout中的XML文件转换成view显示出来
			holder = new ViewHolder();
            holder.tv_class=(TextView)contentView.findViewById(R.id.tv_class);
            holder.tv_subject=(TextView)contentView.findViewById(R.id.tv_subject);
            holder.tv_booking=(TextView)contentView.findViewById(R.id.tv_booking);
            holder.tv_booking.setTag(position);

//            holder.tv_content=(TextView)contentView.findViewById(R.id.tv_content);
			contentView.setTag(holder);
		}else{
			holder = (ViewHolder)contentView.getTag();
		}
        holder.tv_booking.setTag(position);
        holder.tv_booking.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position1=Integer.parseInt(v.getTag().toString());
                if(whitchWillBeDel!=null)
                {
                    whitchWillBeDel.onPosition(position1);
                }
            }
        });
        holder.tv_class.setText(courseModel.tv_class);
        holder.tv_subject.setText(courseModel.tv_subject);
//        holder.tv_content.setText(noticeModel.noticeContent);
 		return contentView;
	}

    public void setWhitchWillBeDel(WhitchWillBeDel whitchWillBeDel) {
        this.whitchWillBeDel = whitchWillBeDel;
    }

    static class ViewHolder{
		TextView tv_class,tv_subject,tv_booking;


	}

    //哪个将要被清除
    public interface WhitchWillBeDel{
        public void onPosition(int Position);
    }


}

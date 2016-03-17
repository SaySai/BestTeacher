package com.shanghai.haojiajiao.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanghai.haojiajiao.R;
import com.shanghai.haojiajiao.base.AdapterBase;
import com.shanghai.haojiajiao.model.CalendarModel;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by Administrator on 2015/12/23.
 */
public class CalendarAdapter extends AdapterBase<CalendarModel> {
    private java.lang.String[] week = {"Su", "M", "Tu", "W", "Th", "F", "Sa"};

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    private int month = 0;

    public CalendarAdapter(Activity act) {
        super(act);
    }

    public String calender = "2013-08-09 00:00:00.0";


    @Override
    protected View getExView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        convertView = inflater.inflate(R.layout.calendar_list, null);
        CalendarModel calendarModel = (CalendarModel) getItem(position);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        if (!calendarModel.isClickState()) {
            if (position < 7) {
                tv_number.setTextColor(Color.WHITE);
                convertView.setBackgroundColor(Color.rgb(46, 139, 87));
                tv_number.setText(week[position]);

            } else {
                convertView.setBackgroundColor(Color.WHITE);
            }
        } else {
            if (calendarModel.getChoiceState() == 0) {
                tv_number.setTextColor(Color.BLACK);
                convertView.setBackgroundColor(Color.rgb(248, 248, 255));
            } else if (calendarModel.getChoiceState() == 1&&calendarModel.isHasdata() ) {
                tv_number.setTextColor(Color.WHITE);
                convertView.setBackgroundColor(Color.rgb(60, 179, 113));
            }

//            tv_number.setTextColor(Color.WHITE);
//            convertView.setBackgroundColor(Color.rgb(0, 255, 255));

            tv_number.setText("" + (calendarModel.getDay() + 1));

        }


//        tv_number.setText(calendarModel.isClickState());
        return convertView;
    }


    /**
     * 描述：判断是否是闰年()
     * <p>(year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
     *
     * @param year 年代（如2012）
     * @return boolean 是否为闰年
     */
    public static int[] isLeapYear(int mubmer, long month) {
        int[] monthR = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] monthP = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int year = 0;
        if (mubmer == 0) {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy");
            Calendar c = new GregorianCalendar();
            if (month != 0) {
                c = getNextMonth();
            }
            year = Integer.parseInt(mSimpleDateFormat.format(c.getTime()));
        } else {
            year = mubmer;
        }


        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return monthR;
        } else {
            return monthP;
        }
    }

    /**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static java.lang.String getCurrentyyyyMM(long month) {
        java.lang.String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM");
            Calendar c = new GregorianCalendar();
            if (month != 0) {
                c.setTimeInMillis(c.getTimeInMillis() + month);
            }
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }

    /**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static java.lang.String getCurrentyyyy_MM(long month) {
        java.lang.String curDateTime = null;
        try {
            long time=System.currentTimeMillis();
            String returnStr = null;
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
            Date date=new Date(time);
            returnStr = f.format(date);
            return returnStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }

    public static java.lang.String getCurrentyyyy_MMN(long month) {
        java.lang.String curDateTime = null;
        try {
            long time=System.currentTimeMillis();
            String returnStr = null;
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
            Date date=new Date(time);
            returnStr = f.format(date);
            String[] args=returnStr.split("-");
            int temp=Integer.valueOf(args[1].substring(args[1].length() - 2, args[1].length()));
            if(temp==12){
                temp=1;
            }else{
                temp++;
            }
            String arg="";
            if(temp<10){
                arg=args[0]+"-0"+temp;
            }else{
                arg=args[0]+"-"+temp;
            }
            return arg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }

    public static java.lang.String getCurrentyyyyMMdd(long month) {
        java.lang.String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = new GregorianCalendar();
            if (month != 0) {
                c.setTimeInMillis(c.getTimeInMillis() + month);
            }
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }

    /**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static int getCurrentMMM(long month) {
        int curDateTime = 0;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MM");
            Calendar c = new GregorianCalendar();
            if (month != 0) {
                c = getNextMonth();
            }
            curDateTime = Integer.parseInt(mSimpleDateFormat.format(c.getTime())) - 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }

    public static String getNextMounth() {
        SimpleDateFormat mSimpleDateFormatY = new SimpleDateFormat("yyy");
        SimpleDateFormat mSimpleDateFormatM = new SimpleDateFormat("MM");

        Calendar c = new GregorianCalendar();
        int year = Integer.parseInt(mSimpleDateFormatY.format(c.getTime()));
        int mounth = Integer.parseInt(mSimpleDateFormatM.format(c.getTime()));
        if (mounth == 12) {
            return (year + 1) + "-0" + 1;
        } else {
            return year + "-" + (mounth + 1);
        }
    }

    /**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static int getCurrentDatedd() {
        int curDateTime = 0;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd");
            Calendar c = new GregorianCalendar();
            curDateTime = Integer.parseInt(mSimpleDateFormat.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }

    /**
     * 取指定日期为星期几.
     *
     * @param strDate  指定日期
     * @param inFormat 指定日期格式
     * @return int   0为星期日，依次类推
     */
    public static int getWeekNumber(long month) {
        Calendar calendar = new GregorianCalendar();
        if (month != 0) {
            calendar = getNextMonth();
        }
        //当前月的第一天
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return intTemp;
    }
//    public static int zhengchu(){
//        return num%5==0;
//    }

    /**
     * 取指定日期为星期几.
     *
     * @param strDate  指定日期
     * @param inFormat 指定日期格式
     * @return String   星期几
     */
    public static int getWeekNumber(String strDate, String inFormat) {
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
            calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        } catch (Exception e) {
            return -1;
        }
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;

    }

    public static Calendar getNextMonth() {
        SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
        Date curDateYear = new Date(System.currentTimeMillis());//获取当前时间
        int year = Integer.parseInt(formatterYear.format(curDateYear));
        Calendar c = new GregorianCalendar();
        int monch = c.get(Calendar.MONTH) + 1;// 获取当前月份
        if (monch == 12) {
            year = year + 1;
            monch = 1;
        } else {
            monch = monch + 1;
        }
        SimpleDateFormat formatterNext = new SimpleDateFormat("yyyy:MM");
        java.util.Date date = null;
        try {
            date = formatterNext.parse(year + ":" + monch);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }
}

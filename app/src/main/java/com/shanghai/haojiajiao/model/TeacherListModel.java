package com.shanghai.haojiajiao.model;

import com.shanghai.haojiajiao.util.StringUtil;

/**
 * Created by Administrator on 2016/1/23.
 */
public class TeacherListModel {
    private int id;
    private String teacherName;
    private String teacherEmail;
    private String teacherGender;
    private String picUrl;
    private String teacherMoney;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTeacherMoney(){return teacherMoney;}

    public void setTeacherMoney(String teacherMoney){this.teacherMoney=teacherMoney;}

    public String getTeacherLessen() {
        return teacherLessen;
    }

    public void setTeacherLessen(String teacherLessen) {
        this.teacherLessen = teacherLessen;
    }

    private String teacherLessen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherGender() {
        return teacherGender;
    }

    public void setTeacherGender(String teacherGender) {
        this.teacherGender = teacherGender;
    }

    public String getTeacherUserName() {
        return teacherUserName;
    }

    public void setTeacherUserName(String teacherUserName) {
        this.teacherUserName = teacherUserName;
    }

    public double getTeacherRate() {
        return teacherRate;
    }

    public void setTeacherRate(double teacherRate) {
        this.teacherRate = teacherRate;
    }

    private String teacherUserName;
    private double teacherRate;
}

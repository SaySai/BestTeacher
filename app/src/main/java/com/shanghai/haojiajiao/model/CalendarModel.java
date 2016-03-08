package com.shanghai.haojiajiao.model;

/**
 * Created by Administrator on 2015/12/23.
 */
public class CalendarModel extends Model {
    private boolean clickState = false;
    private int id = 0;
    private int teacherId = 0;
    private int parentId = 0;
    private String appointment = null;
    private boolean hasdata=false;

    public boolean isHasdata() {
        return hasdata;
    }

    public void setHasdata(boolean hasdata) {
        this.hasdata = hasdata;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getAppointmentAvailableSegment() {
        return appointmentAvailableSegment == null ? "" : appointmentAvailableSegment;
    }

    public void setAppointmentAvailableSegment(String appointmentAvailableSegment) {
        this.appointmentAvailableSegment = appointmentAvailableSegment;
    }

    private String appointmentAvailableSegment = null;

    public boolean isClickState() {
        return clickState;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    private String dayTime = null;

    public void setClickState(boolean clickState) {
        this.clickState = clickState;
    }

    public int getChoiceState() {
        return choiceState;
    }

    public void setChoiceState(int choiceState) {
        this.choiceState = choiceState;
    }

    private int choiceState = 0;//0为无属性，1为已经有的，2为已经选中

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    private int day = 0;
}
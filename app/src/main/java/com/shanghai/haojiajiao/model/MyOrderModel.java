package com.shanghai.haojiajiao.model;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MyOrderModel {
    private int id;
    private int teacherId;
    private int parentId;
    private String orderCharge;
    private String orderTime;
    private String orderTimeSegment;
    private String orderNumber;
    private String orderState;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getOrderCharge() {
        return orderCharge;
    }

    public void setOrderCharge(String orderCharge) {
        this.orderCharge = orderCharge;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderTimeSegment() {
        return orderTimeSegment;
    }

    public void setOrderTimeSegment(String orderTimeSegment) {
        this.orderTimeSegment = orderTimeSegment;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderDay() {
        return orderDay;
    }

    public void setOrderDay(String orderDay) {
        this.orderDay = orderDay;
    }

    private String orderDay;
}

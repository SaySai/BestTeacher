package com.shanghai.haojiajiao.model;

/**
 * Created by Administrator on 2016/1/26.
 */
public class TeacherEvaluateModel {
    private int id;
    private String evaluationTime;
    private String evaluationContent;
    private int teacherId;
    private int parentId;
    private String picUrl;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public String getEvaluationContent() {
        return evaluationContent;
    }

    public void setEvaluationContent(String evaluationContent) {
        this.evaluationContent = evaluationContent;
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

    public int getEvaluationRate() {
        return evaluationRate;
    }

    public void setEvaluationRate(int evaluationRate) {
        this.evaluationRate = evaluationRate;
    }

    private int evaluationRate;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}

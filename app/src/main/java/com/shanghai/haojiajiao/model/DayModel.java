package com.shanghai.haojiajiao.model;

/**
 * Created by Administrator on 2015/12/23.
 */
public class DayModel {
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public boolean click = false;//教师true可选，false不可选
    private int state = 0;//0=none，1=可逾越
}

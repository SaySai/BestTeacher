package com.shanghai.haojiajiao.model;

import com.shanghai.haojiajiao.util.ModelUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户信息实体类
 *
 * @author lfz
 */
public class UserModel implements Serializable {
    public int userid; //用户id
    public String username;//用户名
    public String picUrl;//用户头像    added by husai. 2016/2/25
    public String identificationnumber; //身份证号
    public String phonenumber;//手机号
    public String sex;//性别(1 男  2 女)
    public String email;//邮箱地址
    public String address;//地址(住址)
    public String registerdate;//注册日期
    public String note;//备注
    public String birthday;//生日

    public static UserModel initWithMap(Map<String, Object> map) {
        UserModel model = new UserModel();
        model.userid = ModelUtil.getIntValue(map, "userid");
        model.username = ModelUtil.getStringValue(map, "username");
        model.identificationnumber = ModelUtil.getStringValue(map, "identificationnumber");
        model.phonenumber = ModelUtil.getStringValue(map, "phonenumber");
        model.sex = ModelUtil.getStringValue(map, "sex");
        model.email = ModelUtil.getStringValue(map, "email");
        model.address = ModelUtil.getStringValue(map, "address");
        model.registerdate = ModelUtil.getStringValue(map, "registerdate");
        model.note = ModelUtil.getStringValue(map, "note");
        model.birthday =ModelUtil.getStringValue(map, "birthday");
        return model;
    }
}

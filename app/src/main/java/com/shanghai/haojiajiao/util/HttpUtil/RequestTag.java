package com.shanghai.haojiajiao.util.HttpUtil;

/**
 * 存放网络访问请求的tag的类
 *
 * @author ahnw_01
 */
public enum RequestTag {
    userLogin, //登录
    checkRegister,//邮件是否可用
    parentRegister,//家长注册
    teacherRegister,//教师注册
    changePassword,//修改密码
    getTeacherByRate,//根据评分获取教师信息
    getParentInfoByUserName,//根据名字获取用户信息
    getParentInfoByUserNameForRY,//for Rongyun
    resetPassword,//重置密码
    getTeacherInfoByUserName,//获取教师信息
    getTeacherInfoByUserNameForRY,//for Rongyun
    getTeacherEvaluation,//获取用户评论
    updateTeacher,//修改老师
    updateParent,//修改家长
    getTeacherAvailableDate,//获取时间
    getPorPath,
    editTeacherAvailableTimeByDate,
    getParentOrderWithTeacher,
    addOrder,
    getToken,
    getTeacherToken,
    getParentToken,
    getParentOrder,
    getTeacherOrder,
    getTeacherById,
    getParentById,
    addEvaluation,
    getNotice
}

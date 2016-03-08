package com.shanghai.haojiajiao.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关于字符串的工具类
 *
 * @author homeworkcat
 */
public class StringUtil {
    private static SimpleDateFormat format;

    /**
     * 判断一个字符串是不是正确的手机号
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern
                .compile("^(13[0-9]|15[0-9]|170|177|179|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }

    /**
     * 从url中截取图片的文件名称,截取最后一个/后面的值
     *
     * @param imageUrl
     * @return
     */
    public static String getImgNameFormUrl(String imageUrl) {
        if (imageUrl == null || "".equals(imageUrl)) {
            return null;
        }

        // return imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
        // imageUrl.length())
        // + ".cache";
        return imageUrl.replaceAll("/", "#") + ".cache";
    }

    /**
     * 检查密码格式是否正则,允许输入6-12位的数字或字母
     *
     * @param pwd
     * @return
     */
    public static boolean checkPassword(String pwd) {
        if (pwd == null || "".equals(pwd)) {
            return false;
        }
        if (pwd.length() < 6 || pwd.length() > 12) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher matcher = pattern.matcher(pwd);

        return matcher.matches();

    }

    // 判断手机格式是否正确

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[6-8]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":").replaceAll("，", ",")
                .replaceAll("、", ",").replaceAll("。", ".").replaceAll("《", "<")
                .replaceAll("》", ">").replaceAll("“", "\"")
                .replaceAll("’", "'").replaceAll("（", "(").replaceAll("）", ")")
                .replaceAll("—", "--");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return ToDBC(m.replaceAll(""));
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 将 "2014-13-11 11:11:11.111" 这种格式的时间转成2014-13-11
     *
     * @param beforeTime
     * @return
     */
    public static String timeFormatChange(String beforeTime) {
        String afterTime = null;
        try {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = format.parse(beforeTime);
            format = new SimpleDateFormat("yyyy-MM-dd");
            afterTime = format.format(date);
        } catch (ParseException e) {
            return beforeTime;
        }

        return afterTime;
    }

    /**
     * 将 "2014-13-11 11:11:11.111" 这种格式的时间转成2014-13-11 11:11:11
     *
     * @param beforeTime
     * @return
     */
    public static String timeFormatChange1(String beforeTime) {
        String afterTime = null;
        try {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            Date date = format.parse(beforeTime);
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            afterTime = format.format(date);
        } catch (ParseException e) {
            return beforeTime;
        }

        return afterTime;
    }

    /*
     * 从后台返回的原始数据中获取json有用的一部分数据
     */
    public static String cutJsonStr(String str) {
        if (!str.equals("") || str != null) {
            return str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
        }
        return str;
    }

    /*
     * 用于获得一个数且带小数点后二位的小数
     */
    public static String getBigDecimal(String someDate) {
        String res;
        if (someDate.equals("")) {
            res = "数据有误";
        } else if (Double.parseDouble(someDate) == 0) {
            res = "0.00";
        } else {
            BigDecimal num = new BigDecimal(someDate);
            DecimalFormat df = new DecimalFormat("0.00");
            res = df.format(num);
        }
        return res;
    }

    /**
     * @param String ,String
     * @return List<String>
     * @Description 将字符串转换成数组
     */
    public static List<String> convertStrToArray(String str, String splitStr) {
        List<String> list = new ArrayList<String>();
        String[] strArray = str.split(splitStr);
        list = java.util.Arrays.asList(strArray);
        return list;
    }

    /**
     * @param List <String>,String
     * @return String
     * @Description 将数组转换成字符串
     */
    public static String convertListToStr(List<String> list, String splitStr) {
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            sb.append(list.get(i));
            sb.append(splitStr);
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 获取url中的图片名称
     *
     * @param imageUrl
     * @return
     */
    public static String getUrlExchange(String imageUrl) {
        if (imageUrl == null || "".equals(imageUrl)) {
            return null;
        }
//
//		return imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
//				imageUrl.length())+".cache";
        return imageUrl.replaceAll("/", "#") + ".cache";
    }
    /**
     * 从url中截取图片的文件名称,截取最后一个/后面的值
     *
     * @param imageUrl
     * @return
     */
    public static String getVideoNameFormUrl(String imageUrl) {
        if (imageUrl == null || "".equals(imageUrl)) {
            return null;
        }

        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
                imageUrl.length());
    }


    /*
     * 判断邮箱格式是否正确
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 验证身份证号是否符合规则
     *
     * @param text 身份证号
     * @return
     */
    public static boolean personIdValidation(String text) {
        String regx = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        String reg1 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$";
        return text.matches(regx) || text.matches(reg1);
    }


    /**
     * 对时间进行分割  针对  yyyy-MM-dd HH:mm:ss
     *
     * @param 时间
     */
    public static List<String> splitTimeString(String str) {
        // TODO Auto-generated method stub
        List<String> arr = new ArrayList<String>();
        String[] str1 = str.split(" ");
        String day[] = str1[0].split("-");
        String hour[] = str1[1].split(":");
        for (String temp : day) {
            arr.add(temp);
        }
        for (String temp : hour) {
            arr.add(temp);
        }
        return arr;

    }


}

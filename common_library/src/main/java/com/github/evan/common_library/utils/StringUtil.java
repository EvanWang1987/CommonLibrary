package com.github.evan.common_library.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Evan on 2017/11/3.
 */
public class StringUtil {

    /**
     * 是否是空
     * @param value
     * @return
     */
    public static boolean isEmpty(CharSequence value){
        return null == value || value.length() == 0;
    }

    /**
     * 是否是null字符串
     * @param value
     * @return
     */
    public static boolean isNullString(CharSequence value){
        return value.toString().equalsIgnoreCase("null");
    }

    /**
     *
     * @param value
     * @param isFilteringNullString
     * @return
     */
    public static boolean isEmptyString(CharSequence value, boolean isFilteringNullString){
        return isFilteringNullString ? isEmpty(value) || isNullString(value) : isEmpty(value);
    }

    public static boolean equals(String arg1, String arg2, boolean isIgnoreCase){
        if(isEmpty(arg1) && isEmpty(arg2)){
            return true;
        }
        return isIgnoreCase ? arg1.equalsIgnoreCase(arg2) : arg1.equals(arg2);
    }

    public static String toStringWithoutHashCode(Object obj){
        String s = obj.toString();
        return s.substring(0, s.lastIndexOf("@"));
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+任意数
     * 17+除9的任意数
     * 199+任意数
     * 198+任意数
     * 166+任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(199[0-9])|(198[0-9])|(166[0-9])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str)throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 格式化手机号
     * @param phoneNum
     * @return
     */
    public static String formatPhoneNum(String phoneNum){
        String regex = "(\\+86)|[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.replaceAll("");
    }

    /**
     * 验证是否是邮箱
     * @param email
     * @return
     */
    public static String isEmail(String email){
        String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.replaceAll("");
    }

}

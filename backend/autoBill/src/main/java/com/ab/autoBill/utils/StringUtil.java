package com.ab.autoBill.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
    /**
     * 对比新旧值返回有效的值
     * @param newStr
     * @param oldStr
     * @return
     */
    public static String getValidValue(String newStr,String oldStr){
        if(newStr != null && !newStr.isEmpty()){
            return newStr;
        }else{
            return oldStr;
        }
    }

    /**
     * 将输入的字符串转换为yyyy/MM/dd HH:mm:ss格式的date
     * @param str
     * @return
     */
    public static Date toDateTime(String str){
        Date date = null;
        //字符串转Date类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

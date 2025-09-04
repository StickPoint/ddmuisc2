package com.stickpoint.ddmusic.common.utils;

import java.util.Collection;

/**
 * @author fntp
 * @date 2025/9/4
 */
public class BaseUtils {

    /**
     * 判断字符串是否为空
     * @param str 目标字符串
     * @return 字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否非空
     * @param str 待判断字符串
     * @return 字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白
     * @param str 待判断字符串
     * @return 字符串是否为空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否非空白
     * @param str 待判断字符串
     * @return 字符串是否非空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 集合是否为空
     * @param dataList 待判断集合
     * @return 集合是否为空
     */
    public static <T> boolean isEmpty(Collection<T> dataList) {
        return dataList == null || dataList.isEmpty();
    }

    /**
     * 集合是否非空
     * @param dataList 待判断集合
     * @return 集合是否非空
     */
    public static <T> boolean isNotEmpty(Collection<T> dataList) {
        return !isEmpty(dataList);
    }

    /**
     * 比较两个字符串是否相等
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 字符串是否相等
     */
    public static boolean equals(String str1, String str2) {
        if (!(isEmpty(str1) == isEmpty(str2))) {
            return false;
        }
        return str1.equals(str2);
    }

}

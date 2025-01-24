package com.universe.touchpoint.utils;

public class ClassUtils {

    public static Object convertToFieldType(Class<?> fieldType, String value) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value);
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(value);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == float.class || fieldType == Float.class) {
            return Float.parseFloat(value);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else {
            return value; // 默认返回字符串
        }
    }

}

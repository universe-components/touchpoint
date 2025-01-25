package com.universe.touchpoint.utils;

import java.lang.reflect.Field;

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

    // 方法：获取类所有成员变量的值，并用逗号分隔
    public static String getFieldValues(Object obj) {
        StringBuilder result = new StringBuilder();

        // 获取对象的类
        Class<?> clazz = obj.getClass();
        // 获取类的所有字段（包括私有字段）
        Field[] fields = clazz.getDeclaredFields();

        // 遍历所有字段
        for (int i = 0; i < fields.length; i++) {
            try {
                // 使私有字段可访问
                fields[i].setAccessible(true);
                // 获取字段的值
                Object value = fields[i].get(obj);
                // 将字段值添加到结果中
                result.append(value);
                // 如果不是最后一个字段，添加逗号
                if (i < fields.length - 1) {
                    result.append(", ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

}

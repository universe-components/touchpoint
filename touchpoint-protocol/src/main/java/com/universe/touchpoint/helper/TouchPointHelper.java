package com.universe.touchpoint.helper;

import com.universe.touchpoint.TouchPointConstants;

public class TouchPointHelper {

    public static String touchPointFilterName(String filter) {
        return TouchPointConstants.TOUCH_POINT_FILTER_PREFIX + "." + filter;
    }

    public static String touchPointFilterName(String filter, String suffix) {
        return TouchPointConstants.TOUCH_POINT_FILTER_PREFIX + "." + filter + "." + suffix;
    }

    public static String touchPointFilterName(String filter, String middle, String suffix) {
        return String.join(".", TouchPointConstants.TOUCH_POINT_FILTER_PREFIX, filter, middle, suffix);
    }

    public static String extractFilter(String input) {
        // 假设输入格式是 "prefix.filter.suffix"
        String[] parts = input.split("\\.");

        // 确保分割后的数组有足够的部分
        if (parts.length >= 3) {
            return parts[1]; // filter 是第二部分
        }

        // 如果格式不对，返回 null 或抛出异常
        return null; // 或者抛出 IllegalArgumentException("Invalid input format");
    }

    public static String extractSuffixFromFilter(String filter) {
        int index = filter.lastIndexOf(".");
        return filter.substring(index + 1);
    }

}

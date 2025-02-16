package com.universe.touchpoint.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtils {

    public static Map<String, List<String>> convert(String[] toActionsConfig) {
        Map<String, List<String>> resultMap = new HashMap<>();

        for (String entry : toActionsConfig) {
            // 找到 `[` 和 `]` 的位置
            int startIndex = entry.indexOf('[');
            int endIndex = entry.indexOf(']');

            // 提取任务名称（在 `[` 前面的部分）
            String taskName = entry.substring(0, startIndex);

            // 提取动作名称部分（在 `[` 和 `]` 之间）
            String actionsStr = entry.substring(startIndex + 1, endIndex);

            // 按逗号分割并去除每个动作名称的空格
            List<String> actionList = Arrays.asList(actionsStr.split("\\s*,\\s*"));

            // 将结果放入 map 中
            resultMap.put(taskName, actionList);
        }

        return resultMap;
    }

}

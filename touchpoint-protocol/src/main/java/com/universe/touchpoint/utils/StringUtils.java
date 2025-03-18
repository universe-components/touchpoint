package com.universe.touchpoint.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtils {

  public static String camelToUnderline(String str, boolean capitalizeFirst) {
    if (str == null || str.isEmpty()) return "";

    // 如果首字母不大写，首字母手动大写，方便统一处理
    String normalized =
        capitalizeFirst ? str : Character.toUpperCase(str.charAt(0)) + str.substring(1);

    // 把大写字母前加下划线，然后转小写
    String result = normalized.replaceAll("([A-Z])", "_$1").toLowerCase();

    // 去掉开头的下划线
    return result.startsWith("_") ? result.substring(1) : result;
  }

  public static String convertToCamelCase(String input, boolean capitalizeFirst) {
    if (input == null || input.isEmpty()) {
      return input;
    }

    StringBuilder result = new StringBuilder();
    boolean capitalizeNext = capitalizeFirst; // 控制首字母是否大写

    for (char c : input.toCharArray()) {
      if (c == '_') {
        capitalizeNext = true; // 遇到下划线，下个字符需要大写
      } else {
        if (capitalizeNext) {
          result.append(Character.toUpperCase(c));
          capitalizeNext = false;
        } else {
          result.append(c);
        }
      }
    }

    return result.toString();
  }

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

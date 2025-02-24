package com.universe.touchpoint.agent.decoder;

import com.universe.touchpoint.agent.ModelOutputDecoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActionInstructionDecoder implements ModelOutputDecoder<Map<String, Object>, Double[]> {

    @Override
    public Map<String, Object> run(String params, Class<Double[]> clazz) {
        Double[] actionData = Arrays.stream(params.split(", ")).map(Double::parseDouble).toArray(Double[]::new);
        // 确保传入的数组有7个元素
        if (actionData.length != 7) {
            throw new IllegalArgumentException("actionData 必须是一个包含7个元素的数组");
        }

        Map<String, Object> decodedActions = new HashMap<>();

        // 设置位置变化
        Map<String, Double> deltaPosition = new HashMap<>();
        deltaPosition.put("x", actionData[0]);
        deltaPosition.put("y", actionData[1]);
        deltaPosition.put("z", actionData[2]);

        // 设置角度变化
        Map<String, Double> deltaOrientation = new HashMap<>();
        deltaOrientation.put("theta_x", actionData[3]);
        deltaOrientation.put("theta_y", actionData[4]);
        deltaOrientation.put("theta_z", actionData[5]);

        // 设置抓取力度
        double deltaGrip = actionData[6];

        // 将各部分信息添加到返回的 Map 中
        decodedActions.put("delta_position", deltaPosition);
        decodedActions.put("delta_orientation", deltaOrientation);
        decodedActions.put("delta_grip", deltaGrip);

        return decodedActions;
    }

}

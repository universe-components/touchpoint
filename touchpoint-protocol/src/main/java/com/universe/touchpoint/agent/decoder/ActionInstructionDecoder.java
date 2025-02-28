package com.universe.touchpoint.agent.decoder;

import com.universe.touchpoint.ActionSequence;
import com.universe.touchpoint.agent.ModelOutputDecoder;
import java.util.Arrays;

public class ActionInstructionDecoder implements ModelOutputDecoder<ActionSequence, Double[]> {

    @Override
    public ActionSequence run(String params, Class<Double[]> clazz) {
        Double[] actionData = Arrays.stream(params.split(", ")).map(Double::parseDouble).toArray(Double[]::new);

        // 确保传入的数组有7个元素
        if (actionData.length != 7) {
            throw new IllegalArgumentException("actionData 必须是一个包含7个元素的数组");
        }

        // 创建位置变化对象
        ActionSequence.Position deltaPosition = new ActionSequence.Position(actionData[0], actionData[1], actionData[2]);

        // 创建角度变化对象
        ActionSequence.Orientation deltaOrientation = new ActionSequence.Orientation(actionData[3], actionData[4], actionData[5]);

        // 获取抓取力度
        double deltaGrip = actionData[6];

        // 创建并返回 ActionSequence 对象
        return new ActionSequence(deltaPosition, deltaOrientation, deltaGrip);
    }

}

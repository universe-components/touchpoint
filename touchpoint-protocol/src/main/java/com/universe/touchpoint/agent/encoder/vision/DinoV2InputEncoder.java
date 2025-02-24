package com.universe.touchpoint.agent.encoder.vision;

import jep.Jep;
import jep.JepException;
import jep.SharedInterpreter;

import com.universe.touchpoint.agent.encoder.VisionInputEncoder;
import com.universe.touchpoint.config.ai.VisionModelConfig;

public class DinoV2InputEncoder implements VisionInputEncoder<VisionModelConfig> {

    @Override
    public Double[][] run(Double[][] imageData, VisionModelConfig modelConfig) {
        try (Jep jep = new SharedInterpreter()) {
            // 加载 Python 脚本
            jep.runScript("dinov2_encoder.py");

            // 调用 extract_dinov2_features 函数
            jep.set("image_data", imageData);
            jep.set("model", modelConfig.getModel().getName());
            return (Double[][]) jep.getValue("extract_dinov2_features(image_data)");
        } catch (JepException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package com.universe.touchpoint.agent.encoder.vision;

import com.universe.touchpoint.agent.encoder.VisionInputEncoder;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;

import jep.Jep;
import jep.JepException;
import jep.SharedInterpreter;

public class SigLIPInputEncoder implements VisionInputEncoder<VisionLangModelConfig> {

    @Override
    public Double[][] run(Double[][] imageData, VisionLangModelConfig modelConfig) {
        try (Jep jep = new SharedInterpreter()) {
            // 加载 Python 脚本
            jep.runScript("siglip_encoder.py");

            // 调用 extract_dinov2_features 函数
            jep.set("image_data", imageData);
            jep.set("model", modelConfig.getModel().getName());
            return (Double[][]) jep.getValue("extract_siglip_features(image_data, model)");
        } catch (JepException e) {
            e.printStackTrace();
        }
        return null;
    }

}

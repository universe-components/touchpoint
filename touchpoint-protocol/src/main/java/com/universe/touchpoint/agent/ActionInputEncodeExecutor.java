package com.universe.touchpoint.agent;

import com.universe.touchpoint.agent.encoder.TextInputEncoder;
import com.universe.touchpoint.agent.encoder.VisionInputEncodeExecutor;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;

import java.util.List;

import jep.Jep;
import jep.JepException;
import jep.SharedInterpreter;

public class ActionInputEncodeExecutor {

    public static Double[] encode(Double[][] imageData, String text, VisionModelConfig visionModelConfig, VisionLangModelConfig visionLangModelConfig, LangModelConfig langModelConfig) {
        Double[][] fused_visual_features = VisionInputEncodeExecutor.encode(imageData, visionModelConfig, visionLangModelConfig);
        List<Double> text_embedding = new TextInputEncoder().run(text, langModelConfig);

        try (Jep jep = new SharedInterpreter()) {
            jep.eval("import numpy as np");
            jep.set("fused_visual_features", fused_visual_features);
            jep.set("text_embedding", text_embedding.toArray());
            return (Double[]) jep.getValue("np.concatenate((fused_visual_features, text_embedding.reshape(1, -1)), axis=1)");
        } catch (JepException e) {
            e.printStackTrace();
        }

        return null;
    }

}

package com.universe.touchpoint.agent.encoder;

import com.universe.touchpoint.agent.encoder.vision.DinoV2InputEncoder;
import com.universe.touchpoint.agent.encoder.vision.SigLIPInputEncoder;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.utils.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jep.Jep;
import jep.JepException;
import jep.SharedInterpreter;

public class VisionInputEncodeExecutor {

    private static final Map<String, VisionInputEncoder<?>> encoders = new HashMap<>();
    static {
        encoders.put("dinov2_features", new DinoV2InputEncoder());
        encoders.put("siglip_features", new SigLIPInputEncoder());
    }

    public static Double[][] encode(Double[][] imageData, VisionModelConfig visionModelConfig, VisionLangModelConfig visionLangModelConfig) {
        new DinoV2InputEncoder().run(imageData, visionModelConfig);
        try (Jep jep = new SharedInterpreter()) {
            for (Map.Entry<String, VisionInputEncoder<?>> encoder : encoders.entrySet()) {
                List<Class<?>> types = ClassUtils.getInterfaceGenericTypes(Objects.requireNonNull(encoders.get(encoder.getKey())).getClass());
                if (types.contains(VisionModelConfig.class)) {
                    jep.set(encoder.getKey(), ((VisionInputEncoder<VisionModelConfig>) encoder.getValue()).run(imageData, visionModelConfig));
                } else {
                    jep.set(encoder.getKey(), ((VisionInputEncoder<VisionLangModelConfig>) encoder.getValue()).run(imageData, visionLangModelConfig));
                }
            }
            // 加载 Python 脚本
            jep.runScript("fuse_features.py");
            String features = encoders.keySet().stream().reduce((a, b) -> a + "," + b).get();
            return (Double[][]) jep.getValue("fuse_features(" + features + ")");
        } catch (JepException e) {
            e.printStackTrace();
        }
        return null;
    }

}

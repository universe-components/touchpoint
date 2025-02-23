package com.universe.touchpoint.ai.encoder;

import com.universe.touchpoint.ai.encoder.vision.DinoV2Encoder;
import com.universe.touchpoint.ai.encoder.vision.SigLIPEncoder;
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

public class VisionEncodeExecutor {

    private static final Map<String, VisionEncoder<?>> encoders = new HashMap<>();
    static {
        encoders.put("dinov2_features", new DinoV2Encoder());
        encoders.put("siglip_features", new SigLIPEncoder());
    }

    public static Double[][] encode(Double[][] imageData, VisionModelConfig visionModelConfig, VisionLangModelConfig visionLangModelConfig) {
        new DinoV2Encoder().run(imageData, visionModelConfig);
        try (Jep jep = new SharedInterpreter()) {
            for (Map.Entry<String, VisionEncoder<?>> encoder : encoders.entrySet()) {
                List<Class<?>> types = ClassUtils.getInterfaceGenericTypes(Objects.requireNonNull(encoders.get(encoder.getKey())).getClass());
                if (types.contains(VisionModelConfig.class)) {
                    jep.set(encoder.getKey(), ((VisionEncoder<VisionModelConfig>) encoder.getValue()).run(imageData, visionModelConfig));
                } else {
                    jep.set(encoder.getKey(), ((VisionEncoder<VisionLangModelConfig>) encoder.getValue()).run(imageData, visionLangModelConfig));
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

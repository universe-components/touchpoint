package com.universe.touchpoint.textmodel;

import java.util.HashMap;
import java.util.Map;

public class VectorUtils {

    // 归一化向量，使其 L2 范数 = 1
    public static Map<String, Double> normalizeVector(Map<String, Double> vector) {
        double norm = 0.0;
        for (double value : vector.values()) {
            norm += value * value;
        }
        norm = Math.sqrt(norm);
        if (norm == 0) return vector; // 避免除零错误

        Map<String, Double> normalizedVec = new HashMap<>();
        for (Map.Entry<String, Double> entry : vector.entrySet()) {
            normalizedVec.put(entry.getKey(), entry.getValue() / norm);
        }
        return normalizedVec;
    }

}

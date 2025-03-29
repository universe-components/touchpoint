package com.universe.touchpoint.textmodel.algo;

import com.universe.touchpoint.textmodel.VectorUtils;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Similarity {

  // 余弦相似度
  public static double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
    vec1 = VectorUtils.normalizeVector(vec1);
    vec2 = VectorUtils.normalizeVector(vec2);

    Set<String> allTokens = new HashSet<>(vec1.keySet());
    allTokens.addAll(vec2.keySet());
    double dotProduct = 0.0, norm1 = 0.0, norm2 = 0.0;
    for (String token : allTokens) {
      double v1 = vec1.getOrDefault(token, 0.0);
      double v2 = vec2.getOrDefault(token, 0.0);
      dotProduct += v1 * v2;
      norm1 += v1 * v1;
      norm2 += v2 * v2;
    }
    return (norm1 == 0 || norm2 == 0) ? 0.0 : dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
  }
}

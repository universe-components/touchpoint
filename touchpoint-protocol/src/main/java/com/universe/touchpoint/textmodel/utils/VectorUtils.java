package com.universe.touchpoint.textmodel.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hipparchus.linear.ArrayRealVector;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;

public class VectorUtils {

  // 计算每个 token（单词）的均值
  public static Map<String, Double> computeFeatureMean(
      List<Map<String, Double>> sparseMatrix, Set<String> vocabulary) {
    Map<String, Double> mean = new HashMap<>();
    int numSamples = sparseMatrix.size();

    for (Map<String, Double> tfidfVector : sparseMatrix) {
      for (Map.Entry<String, Double> entry : tfidfVector.entrySet()) {
        mean.put(entry.getKey(), mean.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
      }
    }

    // 计算均值
    for (String token : vocabulary) {
      mean.put(token, mean.getOrDefault(token, 0.0) / numSamples);
    }
    return mean;
  }

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

  // 计算马氏距离
  public static double mahalanobisDistance(
      double[] point1, double[] point2, RealMatrix covarianceMatrix) {
    ArrayRealVector vector1 = new ArrayRealVector(point1);
    ArrayRealVector vector2 = new ArrayRealVector(point2);

    // 计算差异向量
    ArrayRealVector diff = vector1.subtract(vector2);

    // 获取协方差矩阵的逆矩阵
    RealMatrix inverseCovarianceMatrix = MatrixUtils.inverse(covarianceMatrix);

    // 计算 (diff^T * inverseCovarianceMatrix * diff) 的平方根，即马氏距离
    ArrayRealVector temp =
        (ArrayRealVector) inverseCovarianceMatrix.operate(diff); // 计算逆协方差矩阵与差异向量的乘积
    return Math.sqrt(temp.dotProduct(diff)); // 计算平方根
  }

  // 计算欧几里得距离
  private static double euclideanDistance(double[] point1, double[] point2) {
    double sum = 0;
    for (int i = 0; i < point1.length; i++) {
      double diff = point1[i] - point2[i];
      sum += diff * diff;
    }
    return Math.sqrt(sum);
  }
}

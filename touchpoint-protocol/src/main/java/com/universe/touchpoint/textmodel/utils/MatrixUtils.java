package com.universe.touchpoint.textmodel.utils;

import com.universe.touchpoint.textmodel.TextVector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatrixUtils {

  // 构建特征矩阵
  public double[][] buildFeatureMatrix(List<TextVector> textVectors) {
    Set<String> allWords = new HashSet<>();
    for (TextVector textVector : textVectors) {
      allWords.addAll(textVector.allWords()); // 从每个工具获取所有特征
    }

    int numTextVectors = textVectors.size();
    int numFeatures = allWords.size();
    double[][] featureMatrix = new double[numTextVectors][numFeatures];
    List<String> featureList = new ArrayList<>(allWords);

    // 填充特征矩阵
    for (int i = 0; i < numTextVectors; i++) {
      TextVector textVector = textVectors.get(i);
      Map<String, Double> tfidf = textVector.TFIDF(); // 获取已预计算的 TF-IDF

      for (int j = 0; j < numFeatures; j++) {
        String feature = featureList.get(j);
        featureMatrix[i][j] = tfidf.getOrDefault(feature, 0.0);
      }
    }

    return featureMatrix;
  }

  // 计算协方差矩阵
  public double[][] calculateCovarianceMatrix(double[][] featureMatrix) {
    int numTools = featureMatrix.length;
    int numFeatures = featureMatrix[0].length;
    double[][] covarianceMatrix = new double[numFeatures][numFeatures];

    // 计算每两个特征之间的协方差
    for (int i1 = 0; i1 < numFeatures; i1++) {
      for (int i2 = 0; i2 < numFeatures; i2++) {
        double sum = 0;
        for (double[] matrix : featureMatrix) {
          double x = matrix[i1] - mean(featureMatrix, i1);
          double y = matrix[i2] - mean(featureMatrix, i2);
          sum += x * y;
        }
        covarianceMatrix[i1][i2] = sum / (numTools - 1); // 协方差公式
      }
    }

    return covarianceMatrix;
  }

  // 计算特征列的均值
  private static double mean(double[][] featureMatrix, int featureIndex) {
    double sum = 0;
    for (double[] matrix : featureMatrix) {
      sum += matrix[featureIndex];
    }
    return sum / featureMatrix.length;
  }
}

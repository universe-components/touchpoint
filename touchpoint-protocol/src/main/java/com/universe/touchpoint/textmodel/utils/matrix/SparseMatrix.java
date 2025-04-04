package com.universe.touchpoint.textmodel.utils.matrix;

import com.universe.touchpoint.textmodel.TextVector;
import com.universe.touchpoint.textmodel.utils.VectorUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hipparchus.linear.OpenMapRealMatrix;
import org.hipparchus.linear.RealMatrix;

public class SparseMatrix {

  // 计算稀疏协方差矩阵
  public static RealMatrix computeSparseCovarianceMatrix(List<TextVector> textVectors) {
    Set<String> vocabulary = new HashSet<>();
    List<Map<String, Double>> sparseMatrix = new ArrayList<>();
    for (TextVector textVector : textVectors) {
      vocabulary.addAll(textVector.allWords()); // 从每个文本获取所有特征
      sparseMatrix.add(textVector.TFIDF());
    }

    int numFeatures = vocabulary.size();
    int numSamples = sparseMatrix.size();

    // 计算均值
    Map<String, Double> mean = VectorUtils.computeFeatureMean(sparseMatrix, vocabulary);

    // 初始化稀疏协方差矩阵
    OpenMapRealMatrix covarianceMatrix = new OpenMapRealMatrix(numFeatures, numFeatures);

    // 遍历所有样本数据，计算协方差
    for (Map<String, Double> tfidfVector : sparseMatrix) {
      for (Map.Entry<String, Double> entry1 : tfidfVector.entrySet()) {
        String token1 = entry1.getKey();
        double xi = entry1.getValue() - mean.get(token1);

        for (Map.Entry<String, Double> entry2 : tfidfVector.entrySet()) {
          String token2 = entry2.getKey();
          double xj = entry2.getValue() - mean.get(token2);

          covarianceMatrix.addToEntry(
              vocabularyIndex(token1, vocabulary), vocabularyIndex(token2, vocabulary), xi * xj);
        }
      }
    }

    // 归一化（除以 m - 1）
    int denominator = numSamples - 1;
    for (int i = 0; i < numFeatures; i++) {
      for (int j = 0; j < numFeatures; j++) {
        covarianceMatrix.setEntry(i, j, covarianceMatrix.getEntry(i, j) / denominator);
      }
    }

    return covarianceMatrix;
  }

  // 获取 token 在 vocabulary 中的索引
  private static int vocabularyIndex(String token, Set<String> vocabulary) {
    int index = 0;
    for (String word : vocabulary) {
      if (word.equals(token)) {
        return index;
      }
      index++;
    }
    return -1; // 处理错误情况
  }
}

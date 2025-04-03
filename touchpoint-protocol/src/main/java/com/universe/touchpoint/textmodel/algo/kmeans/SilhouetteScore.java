package com.universe.touchpoint.textmodel.algo.kmeans;

import java.util.List;
import org.hipparchus.clustering.CentroidCluster;
import org.hipparchus.clustering.DoublePoint;
import org.hipparchus.clustering.KMeansPlusPlusClusterer;

public class SilhouetteScore {

  // 计算轮廓系数
  public static double computeSilhouetteScore(List<DoublePoint> actions, int k) {
    KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(k, 100);
    List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(actions);

    double totalScore = 0;
    int count = 0;

    for (CentroidCluster<DoublePoint> cluster : clusters) {
      List<DoublePoint> clusterPoints = cluster.getPoints();
      for (DoublePoint tool : clusterPoints) {
        double a = 0, b = Double.MAX_VALUE;

        // 计算簇内平均距离
        for (DoublePoint other : clusterPoints) {
          if (!tool.equals(other)) {
            a += euclideanDistance(tool.getPoint(), other.getPoint());
          }
        }
        if (clusterPoints.size() > 1) {
          a = a / (clusterPoints.size() - 1);
        }

        // 计算最近其他簇的平均距离
        for (CentroidCluster<DoublePoint> otherCluster : clusters) {
          if (!otherCluster.equals(cluster)) {
            for (DoublePoint other : otherCluster.getPoints()) {
              double dist = euclideanDistance(tool.getPoint(), other.getPoint());
              b = Math.min(b, dist);
            }
          }
        }

        double s = (b - a) / Math.max(a, b);
        totalScore += s;
        count++;
      }
    }

    return (count == 0) ? 0 : totalScore / count;
  }

  // 使用轮廓系数优化 K 值
  public static int refineKUsingSilhouette(List<DoublePoint> actions, int estimatedK) {
    double bestScore = -1;
    int bestK = estimatedK;

    for (int k = Math.max(2, estimatedK - 1); k <= estimatedK + 1; k++) {
      double score = computeSilhouetteScore(actions, k);
      if (score > bestScore) {
        bestScore = score;
        bestK = k;
      }
    }

    return bestK;
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

package com.universe.touchpoint.textmodel.algo.kmeans;

import com.universe.touchpoint.textmodel.utils.VectorUtils;
import java.util.List;
import org.hipparchus.clustering.CentroidCluster;
import org.hipparchus.clustering.DoublePoint;
import org.hipparchus.clustering.KMeansPlusPlusClusterer;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;

public class SilhouetteScore {

  // 计算轮廓系数
  public static double computeSilhouetteScore(
      List<DoublePoint> actions, int k, double[][] covarianceMatrix) {
    KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(k, 100);
    List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(actions);
    RealMatrix covarianceMatrixReal = MatrixUtils.createRealMatrix(covarianceMatrix);

    double totalScore = 0;
    int count = 0;

    for (CentroidCluster<DoublePoint> cluster : clusters) {
      List<DoublePoint> clusterPoints = cluster.getPoints();
      for (DoublePoint tool : clusterPoints) {
        double a = 0, b = Double.MAX_VALUE;

        // 计算簇内平均距离
        for (DoublePoint other : clusterPoints) {
          if (!tool.equals(other)) {
            a +=
                VectorUtils.mahalanobisDistance(
                    tool.getPoint(), other.getPoint(), covarianceMatrixReal);
          }
        }
        if (clusterPoints.size() > 1) {
          a = a / (clusterPoints.size() - 1);
        }

        // 计算最近其他簇的平均距离
        for (CentroidCluster<DoublePoint> otherCluster : clusters) {
          if (!otherCluster.equals(cluster)) {
            for (DoublePoint other : otherCluster.getPoints()) {
              double dist =
                  VectorUtils.mahalanobisDistance(
                      tool.getPoint(), other.getPoint(), covarianceMatrixReal);
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
  public static int refineKUsingSilhouette(
      List<DoublePoint> actions, double[][] covarianceMatrix, int estimatedK) {
    double bestScore = -1;
    int bestK = estimatedK;

    for (int k = Math.max(2, estimatedK - 1); k <= estimatedK + 1; k++) {
      double score = computeSilhouetteScore(actions, k, covarianceMatrix);
      if (score > bestScore) {
        bestScore = score;
        bestK = k;
      }
    }

    return bestK;
  }
}

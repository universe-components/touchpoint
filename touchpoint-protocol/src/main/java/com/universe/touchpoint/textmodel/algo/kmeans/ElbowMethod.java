package com.universe.touchpoint.textmodel.algo.kmeans;

import com.universe.touchpoint.textmodel.utils.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import org.hipparchus.clustering.CentroidCluster;
import org.hipparchus.clustering.DoublePoint;
import org.hipparchus.clustering.KMeansPlusPlusClusterer;
import org.hipparchus.linear.MatrixUtils;
import org.hipparchus.linear.RealMatrix;

public class ElbowMethod {

  public static int estimateKUsingElbow(
      List<DoublePoint> actions, double[][] covarianceMatrix, int maxK) {
    List<Double> wcss = new ArrayList<>();
    RealMatrix covarianceMatrixReal = MatrixUtils.createRealMatrix(covarianceMatrix);

    // 计算 WCSS（簇内误差平方和）
    for (int k = 1; k <= maxK; k++) {
      KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(k, 100);
      List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(actions);

      double sumSquaredError = 0;
      for (CentroidCluster<DoublePoint> cluster : clusters) {
        double[] centroid = cluster.getCenter().getPoint();
        for (DoublePoint point : cluster.getPoints()) {
          sumSquaredError +=
              VectorUtils.mahalanobisDistance(point.getPoint(), centroid, covarianceMatrixReal);
        }
      }
      wcss.add(sumSquaredError);
    }

    // 计算一阶差分（WCSS 下降速度）
    List<Double> firstDerivatives = new ArrayList<>();
    for (int i = 1; i < wcss.size(); i++) {
      firstDerivatives.add(wcss.get(i - 1) - wcss.get(i));
    }

    // 计算二阶差分（下降趋势变化）
    List<Double> secondDerivatives = new ArrayList<>();
    for (int i = 1; i < firstDerivatives.size(); i++) {
      secondDerivatives.add(firstDerivatives.get(i - 1) - firstDerivatives.get(i));
    }

    // 选取二阶差分绝对值最大的点作为最佳 k
    int bestK = 2;
    double maxCurvature = Double.MIN_VALUE;
    for (int i = 0; i < secondDerivatives.size(); i++) {
      if (Math.abs(secondDerivatives.get(i)) > maxCurvature) {
        maxCurvature = Math.abs(secondDerivatives.get(i));
        bestK = i + 2; // 因为二阶差分是从 k=2 开始的
      }
    }

    return bestK;
  }
}

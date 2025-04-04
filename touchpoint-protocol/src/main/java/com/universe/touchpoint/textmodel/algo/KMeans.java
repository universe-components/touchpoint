package com.universe.touchpoint.textmodel.algo;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.textmodel.TFIDF;
import com.universe.touchpoint.textmodel.algo.kmeans.ElbowMethod;
import com.universe.touchpoint.textmodel.algo.kmeans.SilhouetteScore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hipparchus.clustering.CentroidCluster;
import org.hipparchus.clustering.DoublePoint;
import org.hipparchus.clustering.KMeansPlusPlusClusterer;

public class KMeans {

  public static int findOptimalK(List<DoublePoint> actions, double[][] covarianceMatrix, int maxK) {
    int estimatedK = ElbowMethod.estimateKUsingElbow(actions, covarianceMatrix, maxK);
    return SilhouetteScore.refineKUsingSilhouette(actions, covarianceMatrix, estimatedK);
  }

  public static List<CentroidCluster<DoublePoint>> clusterActions(
      List<DoublePoint> actions, int k) {
    KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(k, 100);
    return clusterer.cluster(actions);
  }

  // 计算簇的质心，使用Action名作为键
  public static Map<String, Double> computeCentroid(
      CentroidCluster<DoublePoint> cluster, List<AgentActionMeta> actions) {
    Map<String, Double> centroid = new HashMap<>();

    // 假设每个Action的点都已经是一个向量表示
    for (int i = 0; i < cluster.getPoints().size(); i++) {
      DoublePoint point = cluster.getPoints().get(i);
      double[] coordinates = point.getPoint();
      String actionName = actions.get(i).getName();

      for (int j = 0; j < coordinates.length; j++) {
        String featureKey = actionName + "_feature" + j;
        centroid.put(featureKey, centroid.getOrDefault(featureKey, 0.0) + coordinates[j]);
      }
    }

    // 平均化
    int numPoints = cluster.getPoints().size();
    centroid.replaceAll((k, v) -> centroid.get(k) / numPoints);

    return centroid;
  }

  public static List<DoublePoint> action2DoublePoint(List<AgentActionMeta> actions) {
    List<DoublePoint> actionPoints = new ArrayList<>();
    for (AgentActionMeta action : actions) {
      Map<String, Double> actionVector = new TFIDF().computeTFIDF(action.getDesc());

      // 将特征映射到一个数组
      double[] coordinates = new double[actionVector.size()];
      int i = 0;

      // 将特征值填充到坐标数组
      for (double value : actionVector.values()) {
        coordinates[i++] = value;
      }

      actionPoints.add(new DoublePoint(coordinates));
    }
    return actionPoints;
  }
}

package com.universe.touchpoint.memory.mapping;

import com.universe.touchpoint.memory.TaskActionMapping;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.meta.data.TaskMeta;
import com.universe.touchpoint.textmodel.TFIDF;
import com.universe.touchpoint.textmodel.algo.Similarity;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SimilarityMapping implements TaskActionMapping {

  @Override
  public Set<AgentActionMeta> mapping(TaskMeta task, Set<AgentActionMeta> availableActions) {
    Map<AgentActionMeta, Double> similarityScores = new HashMap<>();
    TFIDF tfidf = new TFIDF();
    Map<String, Double> taskVector = tfidf.computeTFIDF(task.getDesc());
    for (AgentActionMeta action : availableActions) {
      Map<String, Double> actionVector = tfidf.computeTFIDF(action.getDesc());
      similarityScores.put(action, Similarity.cosineSimilarity(taskVector, actionVector));
    }

    // 按照相似度排序，选择前3个最相关的工具
    return similarityScores.entrySet().stream()
        .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
        .limit(3)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }
}

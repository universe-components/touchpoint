package com.universe.touchpoint.memory;

import com.universe.touchpoint.textmodel.TFIDF;
import com.universe.touchpoint.textmodel.Tokenizer;
import com.universe.touchpoint.textmodel.algo.Similarity;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActivityGraph {

  private final Map<String, Map<String, Double>> similarityEdges = new ConcurrentHashMap<>();
  private final Map<String, Map<String, Double>> tfidfVectors = new ConcurrentHashMap<>();
  private final TFIDF tfidf = new TFIDF();

  // 增量添加边，并更新IDF
  public synchronized void addNode(String input) {
    tfidf.incrementTotalDocuments();
    List<String> tokens = new Tokenizer().tokenize(input);
    Set<String> uniqueTokens = new HashSet<>(tokens);
    for (String token : uniqueTokens) {
      tfidf.incrementDocumentFrequency(token);
    }
    tfidfVectors.put(input, new TFIDF().computeTFIDF(input));
    tfidf.saveIDF(); // 自动保存
  }

  // 连接两个节点并计算相似度
  public double addEdge(String text1, String text2) {
    double sim = Similarity.cosineSimilarity(tfidfVectors.get(text1), tfidfVectors.get(text2));
    similarityEdges.computeIfAbsent(text1, k -> new ConcurrentHashMap<>()).put(text2, sim);
    similarityEdges.computeIfAbsent(text2, k -> new ConcurrentHashMap<>()).put(text1, sim);
    return sim;
  }

  public static class Recommendation {
    String output;
    double similarity;

    public Recommendation(String output, double similarity) {
      this.output = output;
      this.similarity = similarity;
    }

    @Override
    public String toString() {
      return "推荐: " + output + ", 相似度: " + String.format("%.4f", similarity);
    }
  }
}

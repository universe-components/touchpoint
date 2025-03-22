package com.universe.touchpoint.memory;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ActivityGraph {

  private final Map<String, List<String>> adjList = new ConcurrentHashMap<>();
  private final Map<String, Map<String, Double>> tfidfVectors = new ConcurrentHashMap<>();
  private final Map<String, Integer> documentFrequencies = new ConcurrentHashMap<>();
  private final Set<String> stopWords = Collections.synchronizedSet(new HashSet<>());
  private final JiebaSegmenter segmenter = new JiebaSegmenter();

  private volatile int totalDocuments = 0;
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  private String idfPath = null; // 可选保存当前热加载的 idf 路径

  // 加载停词表
  public void loadStopWords(String stopWordsFile) {
    try (BufferedReader br = new BufferedReader(new FileReader(stopWordsFile))) {
      String line;
      while ((line = br.readLine()) != null) {
        stopWords.add(line.trim());
      }
      System.out.println("停词表加载完成");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 加载IDF字典
  public synchronized void loadIDF(String idfPath) {
    this.idfPath = idfPath;
    File file = new File(idfPath);
    if (!file.exists()) return;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      totalDocuments = Integer.parseInt(br.readLine().trim());
      Map<String, Integer> tempDF = new ConcurrentHashMap<>();
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split("\t");
        if (parts.length == 2) {
          tempDF.put(parts[0], Integer.parseInt(parts[1]));
        }
      }
      documentFrequencies.clear();
      documentFrequencies.putAll(tempDF);
      System.out.println("热加载IDF字典完成，总文档数: " + totalDocuments);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 定时热加载
  public void startHotIDFLoader(long intervalSeconds) {
    if (idfPath == null) {
      throw new IllegalStateException("请先调用 loadIDF(idfPath) 加载IDF字典");
    }
    scheduler.scheduleWithFixedDelay(
        () -> loadIDF(idfPath), intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
  }

  // 停止热加载
  public void stopHotIDFLoader() {
    scheduler.shutdown();
  }

  // 保存IDF字典
  public synchronized void saveIDF() {
    try (PrintWriter pw = new PrintWriter(new FileWriter(idfPath))) {
      pw.println(totalDocuments);
      for (Map.Entry<String, Integer> entry : documentFrequencies.entrySet()) {
        pw.println(entry.getKey() + "\t" + entry.getValue());
      }
      System.out.println("IDF字典已保存");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 中文分词 + 停词过滤
  private List<String> tokenize(String text) {
    List<String> tokens = new ArrayList<>();
    for (SegToken token : segmenter.process(text, JiebaSegmenter.SegMode.SEARCH)) {
      String word = token.word.trim();
      if (!stopWords.contains(word) && word.length() > 1) {
        tokens.add(word);
      }
    }
    return tokens;
  }

  // 计算 TF-IDF 向量
  private Map<String, Double> computeTFIDF(String text) {
    List<String> tokens = tokenize(text);
    Map<String, Double> tf = new HashMap<>();
    for (String token : tokens) {
      tf.put(token, tf.getOrDefault(token, 0.0) + 1.0);
    }
    tf.replaceAll((t, v) -> tf.get(t) / tokens.size());

    Map<String, Double> tfidf = new HashMap<>();
    for (String token : tf.keySet()) {
      double idf =
          Math.log((totalDocuments + 1.0) / (documentFrequencies.getOrDefault(token, 0) + 1.0)) + 1;
      tfidf.put(token, tf.get(token) * idf);
    }
    return tfidf;
  }

  // 增量添加边，并更新IDF
  public synchronized void addEdge(String input, String output) {
    adjList.computeIfAbsent(input, k -> new CopyOnWriteArrayList<>()).add(output);
    totalDocuments++;
    List<String> tokens = tokenize(input);
    Set<String> uniqueTokens = new HashSet<>(tokens);
    for (String token : uniqueTokens) {
      documentFrequencies.put(token, documentFrequencies.getOrDefault(token, 0) + 1);
    }
    tfidfVectors.put(input, computeTFIDF(input));
    saveIDF(); // 自动保存
  }

  // 余弦相似度
  private double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
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

  // 推荐
  public List<Recommendation> recommend(String input, int topN) {
    Map<String, Double> inputVector = computeTFIDF(input);
    PriorityQueue<Recommendation> queue =
        new PriorityQueue<>((a, b) -> Double.compare(b.similarity, a.similarity));
    for (String node : adjList.keySet()) {
      double sim = cosineSimilarity(inputVector, tfidfVectors.get(node));
      if (sim > 0) {
        for (String output : adjList.get(node)) {
          queue.add(new Recommendation(output, sim));
        }
      }
    }

    List<Recommendation> results = new ArrayList<>();
    while (!queue.isEmpty() && results.size() < topN) {
      results.add(queue.poll());
    }
    return results;
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

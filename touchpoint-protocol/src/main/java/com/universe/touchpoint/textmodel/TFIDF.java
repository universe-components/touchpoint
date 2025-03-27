package com.universe.touchpoint.textmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TFIDF {

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final Map<String, Integer> documentFrequencies = new ConcurrentHashMap<>();
  private volatile int totalDocuments = 0;
  private String idfPath = null; // 可选保存当前热加载的 idf 路径

  private final Tokenizer tokenizer = new Tokenizer();

  // 计算 TF-IDF 向量
  public Map<String, Double> computeTFIDF(String text) {
    List<String> tokens = tokenizer.tokenize(text);
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

  public void incrementTotalDocuments() {
    totalDocuments++;
  }

  public void incrementDocumentFrequency(String token) {
    documentFrequencies.put(token, documentFrequencies.getOrDefault(token, 0) + 1);
  }
}

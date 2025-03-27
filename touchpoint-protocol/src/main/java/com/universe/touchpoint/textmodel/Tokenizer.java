package com.universe.touchpoint.textmodel;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tokenizer {

  private final Set<String> stopWords = Collections.synchronizedSet(new HashSet<>());

  // 中文分词 + 停词过滤
  public List<String> tokenize(String text) {
    List<String> tokens = new ArrayList<>();
    JiebaSegmenter segmenter = new JiebaSegmenter();
    for (SegToken token : segmenter.process(text, JiebaSegmenter.SegMode.SEARCH)) {
      String word = token.word.trim();
      if (!stopWords.contains(word) && word.length() > 1) {
        tokens.add(word);
      }
    }
    return tokens;
  }

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
}

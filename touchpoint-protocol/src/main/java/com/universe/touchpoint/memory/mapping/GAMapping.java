package com.universe.touchpoint.memory.mapping;

import com.universe.touchpoint.memory.TaskActionMapping;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.meta.data.TaskMeta;
import com.universe.touchpoint.textmodel.TFIDF;
import com.universe.touchpoint.textmodel.algo.Similarity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GAMapping implements TaskActionMapping {

  private static final int POP_SIZE = 10;
  private static final int GENERATIONS = 20;
  private static final double MUTATION_RATE = 0.2;
  private static final Random rand = new Random();

  @Override
  public Set<AgentActionMeta> mapping(TaskMeta task, Set<AgentActionMeta> availableActions) {
    List<List<AgentActionMeta>> population = initPopulation(availableActions);

    for (int gen = 0; gen < GENERATIONS; gen++) {
      population = evolvePopulation(population, task);
    }

    // 返回最优工具组合
    return new HashSet<>(population.get(0));
  }

  private List<List<AgentActionMeta>> initPopulation(Set<AgentActionMeta> actions) {
    List<AgentActionMeta> actionList = new ArrayList<>(actions);
    List<List<AgentActionMeta>> population = new ArrayList<>();
    for (int i = 0; i < POP_SIZE; i++) {
      Collections.shuffle(actionList);
      population.add(new ArrayList<>(actionList.subList(0, 3)));
    }
    return population;
  }

  private List<List<AgentActionMeta>> evolvePopulation(
      List<List<AgentActionMeta>> population, TaskMeta task) {
    population.sort(Comparator.comparingDouble(individual -> -fitness(task, individual)));
    List<List<AgentActionMeta>> newPopulation = new ArrayList<>();

    for (int i = 0; i < POP_SIZE / 2; i++) {
      List<AgentActionMeta> parent1 = population.get(rand.nextInt(POP_SIZE / 2));
      List<AgentActionMeta> parent2 = population.get(rand.nextInt(POP_SIZE / 2));
      newPopulation.add(crossover(parent1, parent2));
      newPopulation.add(mutate(new ArrayList<>(parent1)));
    }

    return newPopulation;
  }

  private List<AgentActionMeta> crossover(
      List<AgentActionMeta> parent1, List<AgentActionMeta> parent2) {
    Set<AgentActionMeta> child = new HashSet<>(parent1.subList(0, parent1.size() / 2));
    child.addAll(parent2.subList(parent2.size() / 2, parent2.size()));
    return new ArrayList<>(child);
  }

  private List<AgentActionMeta> mutate(List<AgentActionMeta> individual) {
    if (rand.nextDouble() < MUTATION_RATE) {
      Collections.shuffle(individual);
    }
    return individual;
  }

  private double fitness(TaskMeta task, List<AgentActionMeta> actions) {
    TFIDF tfidf = new TFIDF();
    Map<String, Double> taskVector = tfidf.computeTFIDF(task.getDesc());
    return actions.stream()
        .mapToDouble(
            action -> {
              Map<String, Double> actionVector = tfidf.computeTFIDF(action.getDesc());
              return Similarity.cosineSimilarity(taskVector, actionVector);
            })
        .sum();
  }
}

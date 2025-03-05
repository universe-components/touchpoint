package com.universe.touchpoint.context;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.config.ai.Model;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.monitor.metric.ActionMetric;

import java.util.HashMap;
import java.util.Map;

public class ActionContext {

    private String currentAction;
    private final Map<String, Model> langModels = new HashMap<>();
    private final Map<String, Model> visionModels = new HashMap<>();
    private final Map<String, Model> visionLangModels = new HashMap<>();
    private final Map<String, Transport> transports = new HashMap<>();
    private Map<String, ActionMetric> actionMetrics;

    public String getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(String currentAction) {
        this.currentAction = currentAction;
    }

    public Model getLangModel(String action) {
        return langModels.get(action);
    }

    public void addLangModel(String action, Model langModel) {
        langModels.put(action, langModel);
    }

    public Model getVisionModel(String action) {
        return visionModels.get(action);
    }

    public void addVisionModel(String action, Model visionModel) {
        visionModels.put(action, visionModel);
    }

    public Model getVisionLangModel(String action) {
        return visionLangModels.get(action);
    }

    public void addVisionLangModel(String action, Model visionLangModel) {
        visionLangModels.put(action, visionLangModel);
    }

    public Transport getTransport(String action) {
        return transports.get(action);
    }

    public void addTransport(String action, Transport transport) {
        transports.put(action, transport);
    }

    public Map<String, ActionMetric> getActionMetrics() {
        return actionMetrics;
    }

    public void setActionMetrics(Map<String, ActionMetric> actionMetrics) {
        this.actionMetrics = actionMetrics;
    }

    public ActionMetric getActionMetric(String action) {
        return actionMetrics.computeIfAbsent(action, k -> new ActionMetric());
    }

    public AgentActionMeta getActionMetaInfo(String action) {
        MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
        return metaRegion.getTouchPointAction(action);
    }

}

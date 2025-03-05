package com.universe.touchpoint.agent;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;

public class AgentActionManager {

    private static AgentActionManager actionManager;
    private static final Object lock = new Object();

    public static AgentActionManager getInstance() {
        synchronized (lock) {
            if (actionManager == null) {
                actionManager = new AgentActionManager();
            }
            return actionManager;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T paddingActionInput(String actionName, String actionInput) {
        MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
        AgentActionMeta agentActionMeta = metaRegion.getTouchPointAction(actionName);
        Class<T> inputClass;
        try {
            inputClass = (Class<T>) Class.forName(agentActionMeta.getInputClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 分割输入
        AIModelOutputDecoder<T, T> actionParamsDecoder = (AIModelOutputDecoder<T, T>) AIModelOutputDecoderSelector.selectParamsDecoder(agentActionMeta.getType());
        return actionParamsDecoder.run(actionInput, inputClass);
    }

}

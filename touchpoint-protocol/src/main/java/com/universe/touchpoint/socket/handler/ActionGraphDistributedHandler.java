package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ActionGraphDistributedHandler implements AgentSocketStateHandler<Boolean> {

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public <C extends AgentContext> Boolean onStateChange(Object input, C actionContext, Context context, String filterSuffix) {
        Map<String, List<String>> actionGraph = (Map<String, List<String>>) input;
        TaskActionContext taskActionContext = (TaskActionContext) actionContext;
        if (actionGraph != null) {
            DriverRegion driverRegion = DriverRegion.getInstance(DriverRegion.class);
            List<String> predecessors  = getPredecessors(taskActionContext.getActionName(), actionGraph);
            List<String> successors = getSuccessors(taskActionContext.getActionName(), actionGraph);

            TouchPointTransportRegistry registry = TouchPointTransportRegistryFactory
                    .createRegistry(Objects.requireNonNull(Agent.agentConfig()).keySet().iterator().next());
            AgentActionManager manager = AgentActionManager.getInstance();

            predecessors.forEach(action -> registry.register(context, driverRegion.getTouchPointAction(action)));
            successors.forEach(action -> manager.registerAgentFinishReceiver(context, action, driverRegion.getTouchPointAction(action).inputClass()));

            driverRegion.putPredecessors(taskActionContext.getActionName(), predecessors);
            driverRegion.putSuccessors(taskActionContext.getActionName(), successors);
        }
        return true;
    }

    public List<String> getSuccessors(String nodeName, Map<String, List<String>> actionGraph) {
        // 直接从 actionGraph 获取 nodeName 的后置节点列表，如果不存在则返回空列表
        return actionGraph.getOrDefault(nodeName, new ArrayList<>());
    }

    // 获取指定节点的所有前置节点
    public List<String> getPredecessors(String nodeName, Map<String, List<String>> actionGraph) {
        List<String> predecessors = new ArrayList<>();

        // 遍历 actionGraph，查找所有指向 nodeName 的节点
        for (Map.Entry<String, List<String>> entry : actionGraph.entrySet()) {
            String node = entry.getKey();
            List<String> successors = entry.getValue();

            // 如果当前节点的后置节点中包含指定的节点，则它是前置节点
            if (successors != null && successors.contains(nodeName)) {
                predecessors.add(node);
            }
        }

        return predecessors;
    }

}

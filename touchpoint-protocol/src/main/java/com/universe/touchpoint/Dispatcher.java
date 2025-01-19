package com.universe.touchpoint;

import com.openai.models.ChatCompletion;
import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.dispatcher.prompt.PromptBuilder;
import com.universe.touchpoint.dispatcher.routers.AIModelRouter;
import com.universe.touchpoint.dispatcher.AgentRouteItem;
import com.universe.touchpoint.dispatcher.routers.AgentRouter;

public class Dispatcher {

    public static <T extends TouchPoint> void dispatch(String content) {
        AIModelType modelType = new AIModelRouter().routeTo(content);
        if (modelType == null) {
            throw new RuntimeException("unknown model type");
        }

        AgentRouter agentRouter = new AgentRouter();
        content = PromptBuilder.createPromptGenerator(modelType).generatePrompt(
                agentRouter.agentRouteItems(Agent.getProperty("name")));

        // 推理并获取choice，随机选择一个choice
        ChatCompletion.Choice choice = AIModelFactory.callModel(content, modelType);
        if (choice != null) {
            AgentRouteItem routeItem = agentRouter.routeTo(choice);
            if (routeItem != null) {
                // 找到匹配的AgentRouteTable并处理
                T touchPoint = TouchPointContextManager.generateTouchPoint(
                        (Class<T>) routeItem.getSharedClass(), routeItem.getToAgent());
                boolean rs = touchPoint.finish();
                if (!rs) {
                    throw new RuntimeException("send target agent failed");
                }
            }
        }
    }

}

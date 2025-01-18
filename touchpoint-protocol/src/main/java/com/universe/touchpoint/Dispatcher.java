package com.universe.touchpoint;

import com.openai.models.ChatCompletion;
import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.router.routers.AIModelRouter;
import com.universe.touchpoint.router.AgentRouteItem;
import com.universe.touchpoint.router.routers.AgentRouter;

public class Dispatcher {

    public static <T extends TouchPoint> void dispatch(String content) {
        // 推理并获取choice，随机选择一个choice
        ChatCompletion.Choice choice = AIModelFactory.callModel(content, new AIModelRouter().routeTo(content));
        if (choice != null) {
            AgentRouteItem routeItem = new AgentRouter().routeTo(choice);
            if (routeItem != null) {
                // 找到匹配的AgentRouteTable并处理
                T touchPoint = TouchPointContextManager.generateTouchPoint((Class<T>) routeItem.getSharedClass(), routeItem.getToAgent());
                boolean rs = touchPoint.finish();
                if (!rs) {
                    throw new RuntimeException("send target agent failed");
                }
            }
        }
    }

}

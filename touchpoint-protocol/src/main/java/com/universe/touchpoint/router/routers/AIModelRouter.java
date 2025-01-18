package com.universe.touchpoint.router.routers;

import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.router.Router;

public class AIModelRouter implements Router<String, AIModelType> {

    @Override
    public AIModelType routeTo(String input) {
        return AIModelType.OPEN_AI;
    }

}

package com.universe.touchpoint.router;

import com.universe.touchpoint.Router;
import com.universe.touchpoint.ai.AIModelType;

public class AIModelRouter implements Router<String, AIModelType> {

    @Override
    public AIModelType routeTo(String input) {
        return AIModelType.OPEN_AI;
    }

}

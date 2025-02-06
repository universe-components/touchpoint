package com.universe.touchpoint.driver;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.driver.processor.AgentActionProcessor;
import com.universe.touchpoint.driver.processor.AgentFinishProcessor;
import com.universe.touchpoint.driver.processor.DefaultResultProcessor;

public class ResultProcessorAdapter {
    
    public static <R, T extends TouchPoint> ResultProcessor<R, T> getProcessor(
            R result, String goal, String task, TouchPointListener<T, ?> tpReceiver, Context context, Transport transportType) {
        if (result instanceof AgentAction || task != null) {
            return (ResultProcessor<R, T>) new AgentActionProcessor<>((AgentAction) result, goal, task, tpReceiver, context, transportType);
        }
        if (result instanceof AgentFinish) {
            return (ResultProcessor<R, T>) new AgentFinishProcessor<>((AgentFinish) result, goal, task, tpReceiver, context, transportType);
        }
        return new DefaultResultProcessor((TouchPoint) result, goal, task, tpReceiver, context, transportType);
    }

}

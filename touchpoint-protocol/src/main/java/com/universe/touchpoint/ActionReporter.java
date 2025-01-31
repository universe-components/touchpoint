package com.universe.touchpoint;

import android.content.Context;

import com.universe.touchpoint.driver.TaskActionReporter;
import com.universe.touchpoint.router.AgentRouterReporter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class ActionReporter<D> implements TouchPointRegistry {

    public static ActionReporter<?> INSTANCE;
    private static final Object lock = new Object();

    private static final Map<String, Class<?>> reporterMap = new HashMap<>();
    static {
        reporterMap.put("router", AgentRouterReporter.class);
        reporterMap.put("taskAction", TaskActionReporter.class);
    }

    public static <D> ActionReporter<D> getInstance(String type) {
        synchronized (lock) {
            if (INSTANCE == null) {
                try {
                    INSTANCE = (ActionReporter<D>) Objects.requireNonNull(
                            reporterMap.get(type)).getConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error creating reporter for type: " + type, e);
                }
            }
            return (ActionReporter<D>) INSTANCE;
        }
    }

    public abstract void report(D data, Context context);

}

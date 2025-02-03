package com.universe.touchpoint;

import android.content.Context;

import com.universe.touchpoint.driver.TaskActionReporter;
import com.universe.touchpoint.router.AgentRouterReporter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AgentReporter<D> implements TouchPointRegistry {

    public static AgentReporter<?> INSTANCE;
    private static final Object lock = new Object();

    private static final Map<String, Class<?>> reporterMap = new HashMap<>();
    static {
        reporterMap.put("router", AgentRouterReporter.class);
        reporterMap.put("taskAction", TaskActionReporter.class);
        reporterMap.put("clean", AgentCleaner.class);
    }

    public static <D> AgentReporter<D> getInstance(String type) {
        synchronized (lock) {
            if (INSTANCE == null) {
                try {
                    INSTANCE = (AgentReporter<D>) Objects.requireNonNull(
                            reporterMap.get(type)).getConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error creating reporter for type: " + type, e);
                }
            }
            return (AgentReporter<D>) INSTANCE;
        }
    }

    public abstract void report(D data, Context context);

}

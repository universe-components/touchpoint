package com.universe.touchpoint;

public class TouchPointRegistryFactory {

    private static volatile TouchPointRegistry<?> instance;
    private static final Object lock = new Object();

    public static <T, R extends TouchPointRegistry<T>> R getInstance(Class<?> registryClass) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    try {
                        instance = (TouchPointRegistry<?>) registryClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return (R) instance;
    }

}

package com.universe.touchpoint.memory;

public abstract class TouchPointRegion {

    private static TouchPointRegion region;
    private static final Object lock = new Object();

    protected TouchPointRegion() {
    }

    // 获取区域名称
    // 利用反射创建实例
    public static <R extends TouchPointRegion> R getInstance(Class<R> regionClass) {
        synchronized(lock) {
            if (region == null) {
                try {
                    region = regionClass.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return (R) region;
    }

}

package com.universe.touchpoint.binder;

import com.universe.touchpoint.binder.mode.AndroidBinder;

import java.util.HashMap;
import java.util.Map;

public class BinderFactory {

    private static final Map<BinderType, Binder> binders = new HashMap<>();
    static {
        binders.put(BinderType.ANDROID_BINDER, new AndroidBinder());
    }

    public static Binder getBinder(BinderType binderType) {
        return binders.get(binderType);
    }

}

package com.universe.touchpoint;

import com.universe.touchpoint.binder.BinderFactory;
import com.universe.touchpoint.binder.BinderType;

public class AgentSocket {

    public static void bind(String path, BinderType binderType) {
        BinderFactory.getBinder(binderType).bind(path);
    }

    public static void unbind(String path, BinderType binderType) {
        BinderFactory.getBinder(binderType).unbind(path);
    }

}

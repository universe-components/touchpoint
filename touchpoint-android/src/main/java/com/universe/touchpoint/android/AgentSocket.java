package com.universe.touchpoint.android;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.android.binder.BinderFactory;
import com.universe.touchpoint.android.binder.BinderType;

public class AgentSocket extends Socket {

    public AgentSocket(String task) {
        super(task);
    }

    public static void bind(String path, BinderType binderType) {
        BinderFactory.getBinder(binderType).bind(path);
    }

    public static void unbind(String path, BinderType binderType) {
        BinderFactory.getBinder(binderType).unbind(path);
    }

}

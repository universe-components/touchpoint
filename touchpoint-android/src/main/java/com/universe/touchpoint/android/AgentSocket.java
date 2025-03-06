package com.universe.touchpoint.android;

import com.universe.touchpoint.TaskSocket;
import com.universe.touchpoint.android.binder.BinderFactory;
import com.universe.touchpoint.android.binder.BinderType;

public class AgentSocket extends TaskSocket {

    public AgentSocket(String task) {
        super(task);
    }

    public void bind(String path, BinderType binderType) {
        BinderFactory.getBinder(binderType).bind(path);
    }

    public void unbind(String path, BinderType binderType) {
        BinderFactory.getBinder(binderType).unbind(path);
    }

}

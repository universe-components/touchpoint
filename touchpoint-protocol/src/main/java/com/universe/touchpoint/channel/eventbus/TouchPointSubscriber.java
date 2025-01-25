package com.universe.touchpoint.channel.eventbus;

import com.qihoo360.replugin.RePlugin;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.helper.TouchPointHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class TouchPointSubscriber<T extends TouchPoint> {

    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(T event) {
        String name = TouchPointHelper.touchPointPluginName(event.getHeader().getToAgent());
        TouchPointListener<T, ?> tpReceiver = (TouchPointListener<T, ?>) TouchPointContextManager.getContext(name).getTouchPointReceiver(name);
        tpReceiver.onReceive(event, RePlugin.getHostContext());
    }

}

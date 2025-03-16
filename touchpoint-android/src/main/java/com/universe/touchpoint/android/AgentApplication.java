package com.universe.touchpoint.android;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;
import com.qihoo360.replugin.RePluginFramework;
import com.qihoo360.replugin.RePluginHost;
import com.universe.touchpoint.android.utils.ApkUtils;
import com.universe.touchpoint.annotations.task.TouchPointAction;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class AgentApplication extends Application {

    protected RePluginConfig createConfig() {
        return new RePluginConfig();
    }

    protected RePluginCallbacks createCallbacks() {
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        RePluginConfig c = createConfig();
        if (c == null) {
            c = new RePluginConfig();
        }

        RePluginCallbacks cb = createCallbacks();
        if (cb != null) {
            c.setCallbacks(cb);
        }

        RePluginHost.App.attachBaseContext(this, c);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Context ctx;
        boolean isPlugin = false;
        if (AppVar.sAppContext != null) {
            RePluginHost.App.onCreate();
            // 初始化发送方
            RePluginFramework.init(AppVar.sAppContext.getClassLoader());
            ctx = AppVar.sAppContext;
        } else {
            ctx = RePlugin.getPluginContext();
            isPlugin = true;
        }

        List<Pair<String, List<Object>>> receiverFilterPair = ApkUtils.getClassNames(
                ctx,
                TouchPointAction.class,
                Arrays.asList("name", "desc", "role", "toActions"),
                !isPlugin
        );

        ActionRegistry.registerActions(receiverFilterPair);
        ActionRegistry.listenTasks(receiverFilterPair);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // 如果App的minSdkVersion >= 14，该方法可以不调用
        RePluginHost.App.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        // 如果App的minSdkVersion >= 14，该方法可以不调用
        RePluginHost.App.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 如果App的minSdkVersion >= 14，该方法可以不调用
        RePluginHost.App.onConfigurationChanged(newConfig);
    }

}

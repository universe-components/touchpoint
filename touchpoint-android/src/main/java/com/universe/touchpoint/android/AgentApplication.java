package com.universe.touchpoint.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Pair;
import androidx.annotation.NonNull;

import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;
import com.qihoo360.replugin.RePluginFramework;
import com.qihoo360.replugin.RePluginHost;
import com.universe.touchpoint.android.utils.ApkUtils;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.provider.TouchPointContentFactory;

import java.util.Arrays;
import java.util.List;

public class AgentApplication extends Application {

    private Activity currentActivity;

    protected RePluginConfig createConfig() {
        return new RePluginConfig();
    }

    protected RePluginCallbacks createCallbacks() {
        return null;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
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

        TaskProposer.init(ctx);

        TaskParticipant.registerActions(receiverFilterPair);
        TaskParticipant.listenTasks(receiverFilterPair);

        //Todo Maybe remove this in the future
        TouchPointContentFactory.registerContentProvider(ctx);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                // Activity 被创建时的回调
                currentActivity = activity;
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                // Activity 被启动时的回调
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                // 每次 Activity 恢复时，记录当前的 Activity 实例
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                // 当 Activity 被暂停时，可以做一些清理工作
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                // 当 Activity 停止时，可以做一些清理工作
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                // 保存 Activity 状态的回调
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                // 当 Activity 被销毁时，可以做一些清理工作
            }
        });
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

package com.universe.touchpoint;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;
import com.qihoo360.replugin.RePluginFramework;
import com.qihoo360.replugin.RePluginHost;

public class TouchPointHostApplication extends Application {

    private Activity currentActivity;

    /**
     * 子类可以复写此方法来自定义RePluginConfig。请参见 RePluginConfig 的说明
     *
     * @see RePluginConfig
     * @return 新的RePluginConfig对象
     */
    protected RePluginConfig createConfig() {
        return new RePluginConfig();
    }

    /**
     * 子类可以复写此方法来自定义RePluginCallbacks。请参见 RePluginCallbacks 的说明 <p>
     * 注意：若在createConfig的RePluginConfig内同时也注册了Callbacks，则以这里创建出来的为准
     *
     * @see RePluginCallbacks
     * @return 新的RePluginCallbacks对象，可以为空
     */
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

        RePluginHost.App.onCreate();
        // 初始化发送方
        RePluginFramework.init(AppVar.sAppContext.getClassLoader());

        // 初始化接收方
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            TouchPointContextManager.registerTouchPointReceivers(AppVar.sAppContext, false);
        }

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
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
            public void onActivitySaveInstanceState(@NonNull Activity activity, Bundle outState) {
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

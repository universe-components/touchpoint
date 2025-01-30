package com.universe.touchpoint;

import android.content.Context;

public interface TouchPointRegistry<C> {

    void register(C config, Context context);
    void registerReceiver(Context context);

}

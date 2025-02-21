package com.universe.touchpoint.api;

import android.content.Context;

public interface RoleExecutor<I, O> {

    O run(I input, Context context);

}

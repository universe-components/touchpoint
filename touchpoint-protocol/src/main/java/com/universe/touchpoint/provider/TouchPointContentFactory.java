package com.universe.touchpoint.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

public class TouchPointContentFactory {

    @SuppressLint("StaticFieldLeak")
    private static TouchPointContent content;
    private static final Object lock = new Object();

    public static TouchPointContent createContent(Uri uri, Context context) {
        synchronized(lock) {
            if (content == null) {
                content = new TouchPointContent(uri, context);
            }
        }
        return content;
    }

}

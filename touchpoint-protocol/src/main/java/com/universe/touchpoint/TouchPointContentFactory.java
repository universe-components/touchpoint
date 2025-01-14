package com.universe.touchpoint;

import android.content.Context;
import android.net.Uri;

import com.universe.touchpoint.provider.TouchPointContent;

public class TouchPointContentFactory {

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

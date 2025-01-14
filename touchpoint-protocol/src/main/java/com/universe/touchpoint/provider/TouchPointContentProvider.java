package com.universe.touchpoint.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.LruCache;

import androidx.annotation.NonNull;

import com.universe.touchpoint.utils.SerializeUtils;

public class TouchPointContentProvider extends ContentProvider {

    private LruCache<String, byte[]> cache;

    @Override
    public boolean onCreate() {
        // Set up memory cache
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        cache = new LruCache<>(cacheSize);
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String key = uri.getLastPathSegment();  // URI path segment is the key
        assert values != null;
        byte[] objectBytes = values.getAsByteArray("touch_point");

        if (objectBytes != null) {
            // Serialize the object to byte array
            cache.put(key, objectBytes);
        }

        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String key = uri.getLastPathSegment();
        byte[] objectBytes = cache.get(key);

        if (objectBytes != null) {
            // 创建一个 MatrixCursor
            MatrixCursor cursor = new MatrixCursor(new String[]{"touch_point"});
            // 直接添加字节数组到 Cursor
            cursor.addRow(new Object[]{objectBytes});
            return cursor;
        }

        return null; // 如果未找到匹配的字节数组，返回 null
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String key = uri.getLastPathSegment();
        assert values != null;
        byte[] objectBytes = values.getAsByteArray("touch_point");

        if (objectBytes != null) {
            cache.put(key, objectBytes);
        }

        return 1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String key = uri.getLastPathSegment();
        cache.remove(key);
        return 1;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.dir/vnd.com.universe.touchpoint.provider";
    }

}

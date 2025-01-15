package com.universe.touchpoint.provider;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.utils.SerializeUtils;

public class TouchPointContent {

    private final Uri uri;
    private final Context context;
    
    public TouchPointContent(Uri uri, Context context) {
        this.uri = uri;
        this.context = context;
    }

    public <T> boolean insertOrUpdate(T object) {
        ContentValues values = new ContentValues();
        values.put(TouchPointConstants.TOUCH_POINT_EVENT_NAME, SerializeUtils.serializeToByteArray(object));

        int rowsUpdated = context.getContentResolver().update(uri, values, null, null);

        if (rowsUpdated == 0) {
            // 如果没有更新任何记录，执行插入操作
            Uri insertedUri = context.getContentResolver().insert(uri, values);
            return insertedUri != null;
        }

        return rowsUpdated > 0;
    }

    public <T> T query(Class<T> clazz) {
        T object = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") byte[] objectBytes = cursor.getBlob(cursor.getColumnIndex(TouchPointConstants.TOUCH_POINT_EVENT_NAME));
            // 这里的 object 需要被反序列化
            object = SerializeUtils.deserializeFromByteArray(objectBytes, clazz);
            cursor.close();
        }

        return object;
    }

}

package com.universe.touchpoint.api.executor;

import com.universe.touchpoint.context.TouchPoint;

public class ImageData extends TouchPoint {

    private byte[][][] data;

    public byte[][][] getData() {
        return data;
    }

    public void setData(byte[][][] data) {
        this.data = data;
    }

}

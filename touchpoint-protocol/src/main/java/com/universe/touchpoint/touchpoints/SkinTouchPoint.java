package com.universe.touchpoint.touchpoints;

import com.universe.touchpoint.context.TouchPoint;

public class SkinTouchPoint extends TouchPoint {

    private Integer optionBackgroundId;
    private Integer blurWindowBackGroundId;

    public Integer getOptionBackgroundId() {
        return optionBackgroundId;
    }

    public SkinTouchPoint setOptionBackgroundId(Integer optionBackgroundId) {
        this.optionBackgroundId = optionBackgroundId;
        return this;
    }

    public Integer getBlurWindowBackGroundId() {
        return blurWindowBackGroundId;
    }

    public SkinTouchPoint setBlurWindowBackGroundId(Integer blurWindowBackGroundId) {
        this.blurWindowBackGroundId = blurWindowBackGroundId;
        return this;
    }

}

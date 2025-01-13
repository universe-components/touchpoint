package com.universe.touchpoint.touchpoints;

import com.universe.touchpoint.TouchPoint;

public class TextTouchPoint extends TouchPoint {

    private Float fontSize;
    private Boolean isBold = false;
    private Boolean isUnderlined = false;
    private String text;

    public TextTouchPoint() {
        super();
    }

    public Float getFontSize() {
        return fontSize;
    }

    public TextTouchPoint setFontSize(Float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public Boolean getBold() {
        return isBold;
    }

    public TextTouchPoint setBold(Boolean bold) {
        this.isBold = bold;
        return this;
    }

    public Boolean getUnderlined() {
        return isUnderlined;
    }

    public TextTouchPoint setUnderlined(Boolean underlined) {
        this.isUnderlined = underlined;
        return this;
    }

    public String getText() {
        return text;
    }

    public TextTouchPoint setText(String text) {
        this.text = text;
        return this;
    }

}

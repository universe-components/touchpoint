package com.universe.touchpoint.touchpoints;

import com.universe.touchpoint.context.TouchPoint;

public class TableTouchPoint extends TouchPoint {

    private Integer rowCount;
    private Integer columnCount;

    public Integer getRowCount() {
        return rowCount;
    }

    public TableTouchPoint setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
        return this;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public TableTouchPoint setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
        return this;
    }

}

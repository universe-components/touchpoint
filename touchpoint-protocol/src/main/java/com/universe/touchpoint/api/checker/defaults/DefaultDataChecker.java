package com.universe.touchpoint.api.checker.defaults;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.checker.DataChecker;

public interface DefaultDataChecker<I extends TouchPoint> extends DataChecker<I, Boolean> {

    Boolean run(I data);

}

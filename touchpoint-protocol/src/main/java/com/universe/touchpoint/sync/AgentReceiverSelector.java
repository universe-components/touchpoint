package com.universe.touchpoint.sync;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.monitor.MetricReceiver;
import com.universe.touchpoint.negotiation.AgentStateReceiver;
import com.universe.touchpoint.opsmodel.AgentContextReceiver;
import com.universe.touchpoint.plan.selector.RelevanceActionSelector;

public class AgentReceiverSelector {

  public static AgentReceiver<?> selectReceiver(String filter) {
    return switch (filter) {
      case TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER -> new AgentStateReceiver();
      case TouchPointConstants.TOUCH_POINT_TASK_OPERATE_CONTEXT_FILTER ->
          new AgentContextReceiver();
      case TouchPointConstants.METRIC_FILTER -> new MetricReceiver();
      case TouchPointConstants.TOUCH_POINT_ACTIVITY_FILTER ->
          new RelevanceActionSelector.ActivityRecordReceiver<>();
      case TouchPointConstants.TOUCH_POINT_ACTIVITY_RESPONSE_FILTER ->
          new RelevanceActionSelector.ActivityResponseReceiver<>();
      default -> null;
    };
  }
}

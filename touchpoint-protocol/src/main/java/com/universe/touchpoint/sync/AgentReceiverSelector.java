package com.universe.touchpoint.sync;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.layer.AgentContextReceiver;
import com.universe.touchpoint.memory.RelevantActionNeuralNetwork;
import com.universe.touchpoint.monitor.MetricReceiver;
import com.universe.touchpoint.negotiation.AgentStateReceiver;

public class AgentReceiverSelector {

  public static AgentReceiver<?> selectReceiver(String filter) {
    return switch (filter) {
      case TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER -> new AgentStateReceiver();
      case TouchPointConstants.TOUCH_POINT_TASK_OPERATE_CONTEXT_FILTER ->
          new AgentContextReceiver();
      case TouchPointConstants.METRIC_FILTER -> new MetricReceiver();
      case TouchPointConstants.TOUCH_POINT_ACTIVITY_FILTER ->
          new RelevantActionNeuralNetwork.ActivityRecordReceiver<>();
      case TouchPointConstants.TOUCH_POINT_ACTIVITY_RESPONSE_FILTER ->
          new RelevantActionNeuralNetwork.ActivityResponseReceiver<>();
      default -> null;
    };
  }
}

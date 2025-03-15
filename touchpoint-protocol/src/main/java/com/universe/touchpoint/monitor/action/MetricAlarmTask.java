package com.universe.touchpoint.monitor.action;

import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.task.TouchPointAction;

@TouchPointAction(
    name = "metrics_alarm",
    desc = "I want to check action metrics.",
    role = ActionRole.PROPOSER)
public class MetricAlarmTask {}

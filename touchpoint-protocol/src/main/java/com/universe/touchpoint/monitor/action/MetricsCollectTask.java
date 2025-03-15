package com.universe.touchpoint.monitor.action;

import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.task.TouchPointAction;

@TouchPointAction(
    name = "collect_metrics",
    desc =
        "I want to collect action and task metrics, where task metrics include the number of execution errors and prediction counts for multiple actions within the task, and action metrics include the prediction count for a single action.",
    role = ActionRole.PROPOSER)
public class MetricsCollectTask {}

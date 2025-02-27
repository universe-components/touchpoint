package com.universe.touchpoint.monitor.syncer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Pair;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.monitor.MetricSyncer;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.Map;
import java.util.Objects;

public class AndroidBroadcastSyncer extends MetricSyncer {

    public AndroidBroadcastSyncer(String task) {
        super(task);
    }

    @Override
    public void initialize(MetricSocketConfig config) {
    }

    @Override
    public void registerListener(Context context) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(task, TouchPointConstants.METRIC_FILTER)
        );
        context.registerReceiver(new MetricListener(), filter, Context.RECEIVER_EXPORTED);
    }

    @Override
    public void sendMetrics(Pair<TaskMetric, Map<String, ActionMetric>> metricPair, Context context) {
        Intent intent = new Intent(TouchPointHelper.touchPointFilterName(task, TouchPointConstants.METRIC_FILTER));
        intent.putExtra(TouchPointConstants.METRIC_EVENT_NAME, SerializeUtils.serializeToByteArray(metricPair));
        context.sendBroadcast(intent);
    }

    public static class MetricListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] metricBytes = intent.getByteArrayExtra(TouchPointConstants.METRIC_EVENT_NAME);
            if (metricBytes == null) {
                return;
            }
            Pair<TaskMetric, Map<String, ActionMetric>> metricPair = SerializeUtils.deserializeFromByteArray(metricBytes, Pair.class);
            // Process the metricPair as needed.
            String task = TouchPointHelper.extractFilter(Objects.requireNonNull(intent.getAction()));
            TouchPointContextManager.getTouchPointContext(task).getTaskContext().setMetric(metricPair.first);
            TouchPointContextManager.getTouchPointContext(task).getActionContext().setActionMetrics(metricPair.second);
        }

    }

}

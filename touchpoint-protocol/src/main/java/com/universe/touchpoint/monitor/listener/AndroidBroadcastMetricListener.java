package com.universe.touchpoint.monitor.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.monitor.MetricListener;
import com.universe.touchpoint.monitor.TaskMetricManager;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.Map;

public class AndroidBroadcastMetricListener implements MetricListener {

    @Override
    public void initialize(MetricSocketConfig socketConfig) {
    }

    @Override
    public void send(Map<String, ?> metrics, Context context, String task) {
        Intent intent = new Intent(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_TASK_METRIC_FILTER,
                        task
                ));
        intent.putExtra(TouchPointConstants.TOUCH_POINT_TASK_METRIC_EVENT, SerializeUtils.serializeToByteArray(metrics));
        context.sendBroadcast(intent);
    }

    @Override
    public void registerReceiver(Context appContext, String task) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_TASK_METRIC_FILTER,
                        task)
        );
        appContext.registerReceiver(new MetricsListener(), filter, Context.RECEIVER_EXPORTED);
    }

    public static class MetricsListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] metricsBytes = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_TASK_METRIC_EVENT);
            if (metricsBytes == null) {
                return;
            }
            Map<String, Integer> metrics = SerializeUtils.deserializeFromByteArray(metricsBytes, Map.class);
            for (Map.Entry<String, Integer> entry : metrics.entrySet()) {
                if (entry.getKey().contains("||")) {
                    String[] taskActionPair = entry.getKey().split("\\|\\|");
                    TaskMetricManager.getActionMetric(taskActionPair[0], taskActionPair[1]).setPredictionCount(entry.getValue());
                } else {
                    TaskMetricManager.getTaskMetric(entry.getKey()).setRetryActionCount(entry.getValue());
                }
            }
        }

    }

}

package com.universe.touchpoint.monitor;

import android.content.Context;

import com.universe.touchpoint.config.metric.MetricSocketConfig;

import java.util.Map;

public interface MetricListener {

    void initialize(MetricSocketConfig socketConfig);

    void send(Map<String, ?> metrics, Context context, String filterSuffix);

    void registerReceiver(Context appContext, String filterSuffix);

}

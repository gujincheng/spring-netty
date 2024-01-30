package com.digiwin.ltgx.metric;

public abstract class GaugeMetric<T> extends Metric<T> {

    public GaugeMetric(String name, MetricUnit unit, String description) {
        super(name, MetricType.GAUGE, unit, description);
    }
}

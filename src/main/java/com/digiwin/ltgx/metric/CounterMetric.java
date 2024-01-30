package com.digiwin.ltgx.metric;

public abstract class CounterMetric<T> extends Metric<T> {

    public CounterMetric(String name, MetricUnit unit, String description) {
        super(name, MetricType.COUNTER, unit, description);
    }

    public abstract void increase(T delta);
}

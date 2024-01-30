package com.digiwin.ltgx.metric;

public class GaugeMetricImpl<T> extends GaugeMetric<T> {
    public GaugeMetricImpl(String name, MetricUnit unit, String description) {
        super(name, unit, description);
    }

    private T value;

    public void setValue(T v) {
        this.value = v;
    }

    @Override
    public T getValue() {
        return value;
    }
}

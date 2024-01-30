package com.digiwin.ltgx.metric;

import com.codahale.metrics.Histogram;

public abstract class MetricVisitor {
    protected String prefix;

    public MetricVisitor(String prefix) {
        this.prefix = prefix;
    }

    public abstract void visit(Metric metric);

    public abstract void visitHistogram(String name, Histogram histogram);

    public abstract void getNodeInfo();

    public abstract String build();
}

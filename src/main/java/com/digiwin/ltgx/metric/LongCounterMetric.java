package com.digiwin.ltgx.metric;

import java.util.concurrent.atomic.LongAdder;

public class LongCounterMetric  extends CounterMetric<Long>{
    public LongCounterMetric(String name, MetricUnit unit, String description) {
        super(name, unit, description);
    }

    // LongAdder is used for purposes such as collecting statistics, not for fine-grained synchronization control.
    // Under high contention, expected throughput of LongAdder is significantly higher than AtomicLong
    private LongAdder value = new LongAdder();

    @Override
    public void increase(Long delta) {
        value.add(delta);
    }

    @Override
    public Long getValue() {
        return value.longValue();
    }
}

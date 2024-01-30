package com.digiwin.ltgx.server;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.digiwin.ltgx.metric.CounterMetric;
import com.digiwin.ltgx.metric.GaugeMetric;
import com.digiwin.ltgx.metric.MetricVisitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyMetricRegistry {
    private static final NettyMetricRegistry INSTANCE = new NettyMetricRegistry();
    /** A gauge metric. The number of netty workers which determines the concurrency of netty requests. */
    public static final String SOCKET_WORKERS_NUM = "socket_workers_num";

    /**
     * A gauge metric. The number of tasks that are pending for processing in the queues of
     * socket workers. Use Netty's NioEventLoop#pendingTasks to get the number, but as the API
     * said, it can be expensive as it depends on the internal implementation. So this metric
     * only be available when <code>Config#enable_socket_detail_metrics</code> is enabled.
     */
    public static final String SOCKET_WORKER_PENDING_TASKS_NUM = "socket_worker_pending_tasks_num";

    /** A counter metric. The number of socket connections. */
    public static final String SOCKET_CONNECTIONS_NUM = "socket_connections_num";


    /** A counter metric. The number of socket requests that is being handled by */
    public static final String SOCKET_HANDLING_REQUESTS_NUM = "socket_handling_requests_num";

    /** A histogram metric. The latency in milliseconds to handle socket requests by . */
    public static final String SOCKET_REQUEST_HANDLE_LATENCY_MS = "socket_request_handle_latency_ms";

    private final Map<String, CounterMetric<?>> counterMetrics = new ConcurrentHashMap<>();

    public static NettyMetricRegistry getInstance() {
        return INSTANCE;
    }
    private final Map<String, GaugeMetric<?>> gaugeMetrics = new ConcurrentHashMap<>();
    private final MetricRegistry histoMetricRegistry = new MetricRegistry();
    private NettyMetricRegistry() {
    }

    public synchronized void registerCounter(CounterMetric<?> counterMetric) {
        counterMetrics.put(counterMetric.getName(), counterMetric);
    }
    public synchronized void registerGauge(GaugeMetric<?> gaugeMetric) {
        gaugeMetrics.put(gaugeMetric.getName(), gaugeMetric);
    }

    public synchronized Histogram registerHistogram(String name) {
        return histoMetricRegistry.histogram(name);
    }

    public void visit(MetricVisitor visitor) {
        for (CounterMetric<?> metric : counterMetrics.values()) {
            visitor.visit(metric);
        }

        for (GaugeMetric<?> metric : gaugeMetrics.values()) {
            visitor.visit(metric);
        }

        for (Map.Entry<String, Histogram> entry : histoMetricRegistry.getHistograms().entrySet()) {
            visitor.visitHistogram(entry.getKey(), entry.getValue());
        }
    }
}

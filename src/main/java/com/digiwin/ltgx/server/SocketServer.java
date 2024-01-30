package com.digiwin.ltgx.server;

import com.digiwin.ltgx.config.Config;
import com.digiwin.ltgx.metric.GaugeMetric;
import com.digiwin.ltgx.metric.GaugeMetricImpl;
import com.digiwin.ltgx.metric.Metric;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

import static com.digiwin.ltgx.server.NettyMetricRegistry.SOCKET_WORKERS_NUM;
import static com.digiwin.ltgx.server.NettyMetricRegistry.SOCKET_WORKER_PENDING_TASKS_NUM;

@Component
@Slf4j
public class SocketServer {
    /**
     * boss 线程组用于处理连接工作
     */
    private NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    private int numWorkerThreads = Math.max(0, Config.http_worker_threads_num);
    /**
     * work 线程组用于数据处理
     */
    private NioEventLoopGroup workerGroup = new NioEventLoopGroup(numWorkerThreads);

    @Value("${netty.port}")
    private Integer port;


    /**
     * 启动Netty Server
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                // 指定Channel
                .channel(NioServerSocketChannel.class)
                //使用指定的端口设置套接字地址
                .localAddress(new InetSocketAddress(port))
                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, Config.http_backlog_num)
                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //将小的数据包包装成更大的帧进行传送，提高网络的负载
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new SocketServerInitializer());
        registerMetrics(workerGroup);
        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            log.info("启动 Netty Server");
        }
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
        log.info("关闭Netty");
    }

    private void registerMetrics(NioEventLoopGroup workerGroup) {
        NettyMetricRegistry nettyMetricRegistry = NettyMetricRegistry.getInstance();

        GaugeMetricImpl<Long> socketWorkersNum = new GaugeMetricImpl<>(
                SOCKET_WORKERS_NUM, Metric.MetricUnit.NOUNIT, "the number of http workers");
        socketWorkersNum.setValue(0L);
        nettyMetricRegistry.registerGauge(socketWorkersNum);

        GaugeMetric<Long> pendingTasks = new GaugeMetric<Long>(SOCKET_WORKER_PENDING_TASKS_NUM, Metric.MetricUnit.NOUNIT,
                "the number of tasks that are pending for processing in the queues of http workers") {
            @Override
            public Long getValue() {
                if (!Config.enable_http_detail_metrics) {
                    return 0L;
                }
                long pendingTasks = 0;
                for (EventExecutor executor : workerGroup) {
                    if (executor instanceof NioEventLoop) {
                        pendingTasks += ((NioEventLoop) executor).pendingTasks();
                    }
                }
                return pendingTasks;
            }
        };
        nettyMetricRegistry.registerGauge(pendingTasks);
    }
}
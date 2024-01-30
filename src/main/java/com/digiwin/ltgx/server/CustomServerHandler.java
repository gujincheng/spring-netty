package com.digiwin.ltgx.server;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.codahale.metrics.Histogram;
import com.digiwin.ltgx.config.BeanFactoryContext;
import com.digiwin.ltgx.constant.Constants;
import com.digiwin.ltgx.core.VehicleBindingControlPoints;
import com.digiwin.ltgx.core.VehicleCheckInControlPoints;
import com.digiwin.ltgx.core.VehicleCheckOutControlPoints;
import com.digiwin.ltgx.core.VehicleQueryControlPoints;
import com.digiwin.ltgx.dto.ResponseDTO;
import com.digiwin.ltgx.enums.ResponseCode;
import com.digiwin.ltgx.metric.LongCounterMetric;
import com.digiwin.ltgx.metric.Metric;
import com.digiwin.ltgx.utils.DateUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.digiwin.ltgx.server.NettyMetricRegistry.*;

@Slf4j
public class CustomServerHandler extends SimpleChannelInboundHandler {
    private final LongCounterMetric socketConnectionsNum;
    private final LongCounterMetric handlingRequestsNum;
    private final Histogram requestHandleLatencyMs;

    public CustomServerHandler() {
        super();
        NettyMetricRegistry nettyMetricRegistry = NettyMetricRegistry.getInstance();
        this.socketConnectionsNum = new LongCounterMetric(SOCKET_CONNECTIONS_NUM,
                Metric.MetricUnit.NOUNIT, "the number of established socket connections currently");
        nettyMetricRegistry.registerCounter(socketConnectionsNum);
        this.handlingRequestsNum = new LongCounterMetric(SOCKET_HANDLING_REQUESTS_NUM, Metric.MetricUnit.NOUNIT,
                "the number of socket requests that is being handled");
        nettyMetricRegistry.registerCounter(handlingRequestsNum);
        this.requestHandleLatencyMs = nettyMetricRegistry.registerHistogram(SOCKET_REQUEST_HANDLE_LATENCY_MS);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        socketConnectionsNum.increase(1L);
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        JSONObject inputParamJson = null;
        log.info("收到消息：" + msg.toString());
        JSONObject responseJson = null;
        long startTime = System.currentTimeMillis();
        try{
            handlingRequestsNum.increase(1L);
            inputParamJson = JSONObject.parseObject(msg.toString());
            // 首先进入公共逻辑，把多个管控点都需要执行的逻辑先走完
            int messagetype = inputParamJson.getIntValue("messagetype");
            String machineNo = null;
            String carrierNo = null;
            String opNo = null;
            String transactionId = null;
            String plotNo = null;
            List<String> snList = null;

            machineNo = inputParamJson.getString("machineno");
            carrierNo = inputParamJson.getString("carrierno");
            transactionId = inputParamJson.getString("transactionid");
            if(ObjectUtils.isNotEmpty(inputParamJson.getString("opno"))) {
                opNo = inputParamJson.getString("opno");
            }

            if(messagetype == Constants.bindRequestCode){
                plotNo = inputParamJson.getString("plotno");
                snList = inputParamJson.getJSONArray("detail").stream()
                        .map(x -> JSONObject.parseObject(x.toString()).getString("sn")).collect(Collectors.toList());
            }
            switch (messagetype){
                case Constants.inRequestCode:
                    System.out.println("进站逻辑");
                    VehicleCheckInControlPoints vehicleCheckInControlPoint = (VehicleCheckInControlPoints)BeanFactoryContext.findBeanByName("vehicleCheckInControlPoints");
                    responseJson = vehicleCheckInControlPoint.checkInControlPointLogic(transactionId,machineNo,carrierNo,opNo);
                    responseJson.put("responseTime", DateUtils.getCurrentTimeStamp());
                    break;
                case Constants.outRequestCode:
                    System.out.println("出站逻辑");
                    VehicleCheckOutControlPoints vehicleCheckOutControlPoint = (VehicleCheckOutControlPoints)BeanFactoryContext.findBeanByName("vehicleCheckOutControlPoints");
                    responseJson = vehicleCheckOutControlPoint.checkOutControlPointLogic(machineNo,carrierNo,opNo);
                    break;
                case Constants.bindRequestCode:
                    System.out.println("绑定逻辑");
                    VehicleBindingControlPoints vehicleBindingControlPoints = (VehicleBindingControlPoints) BeanFactoryContext.findBeanByName("vehicleBindingControlPoints");
                    responseJson = vehicleBindingControlPoints.bindingCardControlLogic(machineNo,carrierNo,opNo,plotNo,snList);
                    break;
                case Constants.queryRequestCode:
                    System.out.println("查询逻辑");
                    VehicleQueryControlPoints vehicleQueryControlPoints = (VehicleQueryControlPoints) BeanFactoryContext.findBeanByName("vehicleQueryControlPoints");
                    responseJson = vehicleQueryControlPoints.queryCardControlLogic(machineNo,carrierNo);
                    break;
                case Constants.queryMetric:
                    log.info(socketConnectionsNum.getValue() + "===========" + handlingRequestsNum.getValue() + "==========" + requestHandleLatencyMs.getCount() + "#####" + requestHandleLatencyMs.getSnapshot().toString());
            }
            // 如果返回的消息体没有\n，客户端会收不到消息，客户端会一直等待
        } catch (JSONException jsonException){
            responseJson = ResponseDTO.build(ResponseCode.PARSE_ERROR);
            log.error(responseJson.toJSONString());
        } catch (Exception exception){
            responseJson = ResponseDTO.build(ResponseCode.UNKNOWN_ERROR,"未知错误，请检查");
            log.error(responseJson.toJSONString());
            exception.printStackTrace();
        } finally {
            long latency = System.currentTimeMillis() - startTime;
            handlingRequestsNum.increase(-1L);
            requestHandleLatencyMs.update(latency);
            ctx.write(responseJson + Constants.delimiter).addListener(ChannelFutureListener.CLOSE);
            log.info("receive socket request. thread id: {}, startTime: {}, latency: {} ms", Thread.currentThread().getId(), startTime, latency);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel关闭");
        socketConnectionsNum.increase(-1L);
        super.channelInactive(ctx);
    }
}

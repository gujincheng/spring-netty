package com.digiwin.ltgx.client;

import com.digiwin.ltgx.constant.Constants;
import com.digiwin.ltgx.utils.DateUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


public class NettyClientTest {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .group(group)
                .handler(new LoggingHandler(LogLevel.class))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        ByteBuf delimiterBuf = Unpooled.copiedBuffer(Constants.delimiter.getBytes());
                        pipeline.addLast(new DelimiterBasedFrameDecoder(1024, delimiterBuf));
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringSocketClientHandler());
                    }
                });


        //websocke连接的地址
        ChannelFuture future = bootstrap.connect("localhost", 9999).sync();

        System.out.println(DateUtils.getCurrentTimeStamp() + "开始发送消息");
        //进站
        //future.channel().writeAndFlush("{\"header\":{\"method\":\"ServicesCUS.Module_CUS.CUS_VehicleCheckIn\",\"lang\":\"zh_CN\",\"is_debug\":\"true\",\"platform\":\"web\",\"account\":\"EQP\"},\"content\":{\"machine_no\":\"0450-01\",\"carrier_no\":\"1-02-0-0512\",\"op_no\":\"A0210\",\"messagetype\":0,\"stamp\":\"2024-01-0418:29:33\",\"transactionid\":\"202401041829330\"}}");

        // 出站
        //future.channel().writeAndFlush("{\"messagetype\":32,\"stamp\":\"2024_01_23_09:57:52:861\",\"transactionid\":\"8ed6b4099fd949a1ac7b222bd61b8929\",\"machineno\":\"PD03112230-01\",\"plotno\":\"20220708\",\"carrierno\":\"V323100102\",\"opno\":\"\",\"parameter\":\"\",\"detail\":[{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"},{\"SN\":\"ABCSDF\",\"location\":\"1_1\",\"remark1\":\"0,1,2,3,5\",\"remark2\":\"Porro测试NG\"}]}\n" + Constants.delimiter);
        future.channel().writeAndFlush("{\"machineno\":\"0450-01\",\"carrierno\":\"1-02-0-0528\",\"messagetype\":0,\"stamp\":\"2024-01-04 18:29:33\",\"parameter\":\"\",\"transactionid\":\"202401041829330\",\"opno\":\"A0207\"}\n" + Constants.delimiter);

        //TimeUnit.SECONDS.sleep(2);
        //sengMessage(future.channel());
        //group.shutdownGracefully();



    }
    public static void sengMessage(Channel channel) throws InterruptedException {
        //发送的内容，是一个文本格式的内容
        //admin
        String putMessage="{\"header\":{\"method\":\"ServicesCUS.Module_CUS.CUS_VehicleCheckIn\",\"lang\":\"zh_CN\",\"is_debug\":\"true\",\"platform\":\"web\",\"account\":\"EQP\"},\"content\":{\"machine_no\":\"0450-01\",\"carrier_no\":\"1-02-0-0512\",\"op_no\":\"A0210\",\"messagetype\":0,\"stamp\":\"2024-01-0418:29:33\",\"transactionid\":\"202401041829330\"}}"; //admin

        channel.writeAndFlush(putMessage).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("=====好消息===="+putMessage);
                } else {
                    System.out.println("=====坏消息=====" + channelFuture.cause().getMessage());
                }
                //channelFuture.channel().closeFuture();
            }
        });
    }

}
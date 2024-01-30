package com.digiwin.ltgx.client;

import com.digiwin.ltgx.utils.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class StringSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println(DateUtils.getCurrentTimeStamp() + "=====客户端接收的消息是====="+msg);
    }
}

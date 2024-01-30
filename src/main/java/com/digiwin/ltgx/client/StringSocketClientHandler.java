package com.digiwin.ltgx.client;

import com.digiwin.ltgx.utils.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class StringSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(DateUtils.getCurrentTimeStamp() + "=====客户端接收的消息是====="+msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().closeFuture();
        super.channelReadComplete(ctx);
    }
}

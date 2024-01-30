package com.digiwin.ltgx.server;

import com.digiwin.ltgx.constant.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;


public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {
    private EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(100);//业务线程池
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ByteBuf delimiterBuf = Unpooled.copiedBuffer(Constants.delimiter.getBytes());
        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(102400, delimiterBuf));
        socketChannel.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast(businessGroup,new CustomServerHandler());
    }
}

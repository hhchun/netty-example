package com.qqs.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 向管道中添加处理器
        ChannelPipeline pipeline = ch.pipeline();
        // HttpServerCodec: netty提供处理http的编/解码器
        pipeline.addLast("simpleHttpServerCodec", new HttpServerCodec());
        // 自定义业务handler
        pipeline.addLast("simpleNettyHttpServerHandler", new NettyHttpServerHandler());
    }
}

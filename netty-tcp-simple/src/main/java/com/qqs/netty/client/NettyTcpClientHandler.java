package com.qqs.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class NettyTcpClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当通道就绪就会触发此方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("cxt:  " + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,server", StandardCharsets.UTF_8));
    }

    /**
     * 当通道有读事件时触发此方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            System.out.println("server reply: " + ((ByteBuf) msg).toString(StandardCharsets.UTF_8));
            System.out.println("server address: " + ctx.channel().remoteAddress());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

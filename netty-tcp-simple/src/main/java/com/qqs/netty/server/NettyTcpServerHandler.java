package com.qqs.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyTcpServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取客户端发送的消息
     *
     * @param ctx 上下文对象,包含: 管道(pipeline)、通道(channel)、地址等
     * @param msg 客户端发送的数据,默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx: " + ctx);
        System.out.println("client msg: " + ((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        System.out.println("pipeline hashCode: " + ctx.pipeline().hashCode());
        System.out.println("client address: " + ctx.channel().remoteAddress());
    }

    /**
     * 数据读写完毕
     * @param ctx 上下文对象
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓存,并刷新缓存
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client", CharsetUtil.UTF_8));
    }

    /**
     * 异常处理
     * @param ctx 上下文对象
     * @param cause 错误/异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}


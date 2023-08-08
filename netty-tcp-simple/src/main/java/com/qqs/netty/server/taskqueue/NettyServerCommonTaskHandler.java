package com.qqs.netty.server.taskqueue;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NettyServerCommonTaskHandler extends ChannelInboundHandlerAdapter {
    // 格式化时间
    private final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 读取客户端发送的消息
     *
     * @param ctx 上下文对象,包含: 管道(pipeline)、通道(channel)、地址等
     * @param msg 客户端发送的数据,默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 比如这里有一个非常耗时的业务，提交到channel对应的NioEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client delay 5 seconds,"
                        + SDF.format(new Date()), CharsetUtil.UTF_8));
                System.out.println("channel hashCode: " + ctx.channel().hashCode());
                System.out.println("task current thread id: " + Thread.currentThread().getId());
                System.out.println("task current thread name: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client delay 3 seconds,"
                        + SDF.format(new Date()), CharsetUtil.UTF_8));
                System.out.println("channel hashCode: " + ctx.channel().hashCode());
                System.out.println("task current thread id: " + Thread.currentThread().getId());
                System.out.println("task current thread name: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("current thread id: " + Thread.currentThread().getId());
        System.out.println("current thread name: " + Thread.currentThread().getName());
    }

    /**
     * 数据读写完毕
     *
     * @param ctx 上下文对象
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入到缓存,并刷新缓存
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client", CharsetUtil.UTF_8));
    }

    /**
     * 异常处理
     *
     * @param ctx   上下文对象
     * @param cause 错误/异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

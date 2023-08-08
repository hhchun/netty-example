package com.qqs.netty.client;

import com.qqs.netty.entity.StudentEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class NettyProtobufSimpleClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StudentEntity.Student student = StudentEntity.Student.newBuilder()
                .setId(1001)
                .setName("jack")
                .build();
        ctx.writeAndFlush(student);
        System.out.println("student send successful");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buff = (ByteBuf) msg;
        System.out.println("client address: " + ctx.channel().remoteAddress());
        System.out.println("client reply message: " + ((ByteBuf) msg).toString(StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

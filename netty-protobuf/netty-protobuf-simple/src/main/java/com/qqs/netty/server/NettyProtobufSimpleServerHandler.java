package com.qqs.netty.server;

import com.qqs.netty.entity.StudentEntity;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class NettyProtobufSimpleServerHandler extends SimpleChannelInboundHandler<StudentEntity.Student> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("I am the server", StandardCharsets.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentEntity.Student student) throws Exception {
        System.out.println("student id = " + student.getId());
        System.out.println("student name = " + student.getName());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

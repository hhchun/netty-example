package com.qqs.netty.client;

import com.qqs.netty.entity.PersonEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class NettyProtobufMultiClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Random random = new Random();
        int next = random.nextInt(3);
        if (next == 1) {
            PersonEntity.Student student = PersonEntity.Student.newBuilder()
                    .setId(1001)
                    .setName("jack")
                    .build();
            PersonEntity.Person person = PersonEntity.Person.newBuilder()
                    .setType(PersonEntity.Person.Type.STUDENT)
                    .setStudent(student)
                    .build();
            ctx.writeAndFlush(person);
        } else if (next == 2) {
            PersonEntity.Teacher teacher = PersonEntity.Teacher.newBuilder()
                    .setId(2001)
                    .setName("tom")
                    .build();
            PersonEntity.Person person = PersonEntity.Person.newBuilder()
                    .setType(PersonEntity.Person.Type.TEACHER)
                    .setTeacher(teacher)
                    .build();
            ctx.writeAndFlush(person);
        }
        System.out.println("student send successful");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client address: " + ctx.channel().remoteAddress());
        System.out.println("client reply message: " + ((ByteBuf) msg).toString(StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

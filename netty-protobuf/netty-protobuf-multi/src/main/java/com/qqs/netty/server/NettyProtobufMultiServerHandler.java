package com.qqs.netty.server;

import com.qqs.netty.entity.PersonEntity;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

public class NettyProtobufMultiServerHandler extends SimpleChannelInboundHandler<PersonEntity.Person> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("I am the server", StandardCharsets.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PersonEntity.Person person) throws Exception {
        PersonEntity.Person.Type type = person.getType();
        if (type == PersonEntity.Person.Type.STUDENT) {
            PersonEntity.Student student = person.getStudent();
            System.out.println("received client student");
            System.out.println("student id = " + student.getId());
            System.out.println("student name = " + student.getName());
        } else if (type == PersonEntity.Person.Type.TEACHER) {
            PersonEntity.Teacher teacher = person.getTeacher();
            System.out.println("received client teacher");
            System.out.println("teacher id = " + teacher.getId());
            System.out.println("teacher name = " + teacher.getName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}

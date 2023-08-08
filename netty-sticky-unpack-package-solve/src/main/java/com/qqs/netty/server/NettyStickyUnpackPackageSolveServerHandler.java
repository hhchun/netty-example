package com.qqs.netty.server;

import com.qqs.netty.common.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class NettyStickyUnpackPackageSolveServerHandler extends ChannelInboundHandlerAdapter {

    private int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtocol messageProtocol = (MessageProtocol) msg;
        System.out.println("server receive message len: " + messageProtocol.getLen());
        System.out.println("server receive message content: " + new String(messageProtocol.getContent()));
        System.out.println("server receive count: " + (++count));

        // 回复给客户端一个随机ID
        byte[] responseContent = UUID.randomUUID().toString().getBytes();
        int responseLen = responseContent.length;
        MessageProtocol responseMessageProtocol = new MessageProtocol();
        responseMessageProtocol.setLen(responseLen);
        responseMessageProtocol.setContent(responseContent);
        ctx.writeAndFlush(responseMessageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

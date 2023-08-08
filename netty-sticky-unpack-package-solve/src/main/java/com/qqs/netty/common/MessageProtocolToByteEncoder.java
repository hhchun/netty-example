package com.qqs.netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class MessageProtocolToByteEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MessageProtocolToByteEncoder encode call");
        if (msg == null || msg.getLen() < 1 ||
                msg.getContent() == null || msg.getContent().length < 1) {
            System.out.println("msg == null or msg is empty");
            return;
        }
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}

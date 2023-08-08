package com.qqs.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBufExampleOne {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        System.out.println("buffer capacity = " + buffer.capacity());

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
            System.out.println("buffer writable bytes = " + buffer.writableBytes());
        }

        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println("buffer index " + i + " value = " + buffer.readByte());
            System.out.println("buffer readable bytes = " + buffer.readableBytes());
        }
    }
}
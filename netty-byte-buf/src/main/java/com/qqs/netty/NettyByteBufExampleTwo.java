package com.qqs.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class NettyByteBufExampleTwo {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.copiedBuffer("hello,netty", StandardCharsets.UTF_8);
        System.out.println("buffer capacity = " + buffer.capacity());
        if (buffer.hasArray()) {
            byte[] content = buffer.array();
            System.out.println("buffer content = " + new String(content, 0, buffer.readableBytes(), StandardCharsets.UTF_8));
            System.out.println("buffer = " + buffer);
            System.out.println("buffer array offset = " + buffer.arrayOffset());
            System.out.println("buffer reader index = " + buffer.readerIndex());
            System.out.println("buffer writer index = " + buffer.writerIndex());
            System.out.println("buffer capacity = " + buffer.capacity());

            System.out.println("buffer index 0 value = " + buffer.getByte(0));
            System.out.println("buffer readable bytes = " + buffer.readableBytes());

            for (int i = 0; i < buffer.readableBytes(); i++) {
                System.out.println("buffer index " + i + " value = " + (char) buffer.getByte(i));
            }

            System.out.println("buffer 0-4 sequence = " + buffer.getCharSequence(0, 4, StandardCharsets.UTF_8));
            System.out.println("buffer 4-6 sequence = " + buffer.getCharSequence(4, 6, StandardCharsets.UTF_8));

        }
    }
}

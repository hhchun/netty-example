package com.qqs.nio;

import java.nio.ByteBuffer;

public class ByteBufferPutGet {
    public static void main(String[] args) {
        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);
        // 类型化方式放入数据
        buffer.putInt(100);
        buffer.putLong(9);
        buffer.putChar('帅');
        buffer.putShort((short) 4);

        // 取出，顺序与放入的顺序一致，类型一致
        buffer.flip();
        System.out.println();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }
}

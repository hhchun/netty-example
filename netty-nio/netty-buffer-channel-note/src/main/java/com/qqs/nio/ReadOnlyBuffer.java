package com.qqs.nio;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {
    public static void main(String[] args) {
        // 创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            // 添加数据
            buffer.put((byte) i);
        }
        // 读取
        buffer.flip();

        // 获取只读Buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        // 读取
        while (readOnlyBuffer.hasRemaining()) { // 判断是否还有数据
            // 取出，并给position+1
            System.out.println(readOnlyBuffer.get());
        }

        // 测试只能读取，不能在put写入
        readOnlyBuffer.put((byte) 100); // throws ReadOnlyBufferException
    }
}

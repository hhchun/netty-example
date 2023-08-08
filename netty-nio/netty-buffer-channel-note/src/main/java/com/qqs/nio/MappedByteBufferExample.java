package com.qqs.nio;


import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferExample {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("/var/tmp/1.txt", "rw");
        // 获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();
        // 参数1: FileChannel.MapMode.READ_WRITE 使用的读写模式
        // 参数2：0表示直接修改的起始位置（字节位置）
        // 参数3: 5表示映射到内存的大小（不是索引位置）,即将 1.txt 的多少个字节映射到内存
        // 那么可以直接修改的范围为: 0-5
        // mappedByteBuffer 的实际类型是 DirectByteBuffer
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        mappedByteBuffer.put(5, (byte) 'Y'); // throws IndexOutOfBoundsException
        // 关闭资源
        randomAccessFile.close();
        channel.close();
    }
}

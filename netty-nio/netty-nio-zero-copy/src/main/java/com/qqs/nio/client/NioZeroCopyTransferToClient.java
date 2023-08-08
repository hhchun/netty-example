package com.qqs.nio.client;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NioZeroCopyTransferToClient {
    public static void main(String[] args) {
        File file = new File("/var/tmp/1.zip");
        try (
                SocketChannel socketChannel = SocketChannel.open();
                FileInputStream is = new FileInputStream(file);
                FileChannel fileChannel = is.getChannel()
        ) {
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 3210));
            long startTime = System.currentTimeMillis();
            long len = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
            long emdTime = System.currentTimeMillis();
            System.out.println("总字节数: " + len);
            System.out.println("总耗时: " + (emdTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

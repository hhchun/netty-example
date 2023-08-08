package com.qqs.nio.server;

import sun.nio.ch.IOStatus;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioZeroCopyServer {
    public static void main(String[] args) {
        InetSocketAddress address = new InetSocketAddress(3210);
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(address);

            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (true) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                int len = 0;
                int total = 0;
                long startTime = System.currentTimeMillis();
                while (len != IOStatus.EOF) {
                    len = socketChannel.read(buffer);
                    total += len;
                    buffer.rewind();
                }
                long emdTime = System.currentTimeMillis();
                System.out.println("总字节数: " + total);
                System.out.println("总耗时: " + (emdTime - startTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

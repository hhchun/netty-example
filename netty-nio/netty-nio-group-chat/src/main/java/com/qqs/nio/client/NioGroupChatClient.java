package com.qqs.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NioGroupChatClient {
    public static void main(String[] args) {
        final String host = "127.0.0.1";
        final int port = 3209;
        try (
                // 创建选择器
                Selector selector = Selector.open();
                // 创建连接服务端对象
                SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, port))
        ) {
            // 设置非阻塞模式
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            String username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " 准备完成");

            // 启动一个子线程,每3秒读取从服务端发送的数据
            new Thread(() -> {
                while (true) {
                    try {
                        int count = selector.select();
                        if (count > 0) {
                            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                            while (selectionKeyIterator.hasNext()) {
                                SelectionKey selectionKey = selectionKeyIterator.next();
                                if (selectionKey.isReadable()) {
                                    // 处理读

                                    // 反向获取通道
                                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                                    int len = channel.read(buffer);
                                    if (len > 0) {
                                        String message = new String(Arrays.copyOf(buffer.array(), len));
                                        System.out.println(message);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            // 从控制台获取消息并发送给服务端
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                message = username + ": " + message;
                System.out.println(message);
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                socketChannel.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

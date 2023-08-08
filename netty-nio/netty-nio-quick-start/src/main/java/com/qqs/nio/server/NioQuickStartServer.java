package com.qqs.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NioQuickStartServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 监听3208端口
        serverSocketChannel.socket().bind(new InetSocketAddress(3208));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 创建Selector
        Selector selector = Selector.open();
        // 将serverSocketChannel注册到selector中，只关心OP_ACCEPT事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端的连接或数据
        while (true) {
            if (selector.select(TimeUnit.SECONDS.toMillis(1)) == 0) {
                System.out.println("服务器已等待1秒,无连接");
                continue;
            }
            // 如果selector.select返回的结果大于0,表示已获取到关心的事件，
            // 可通过selector.selectedKeys获取关心事件的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 使用迭代器遍历事件
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();
                // 根据发生的事件不同进行相应的处理
                if (selectionKey.isAcceptable()) {
                    // OP_ACCEPT事件，有新的客户端连接
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    System.out.println("客户端连接成功，生成一个新的SocketChannel,hashcode = " + socketChannel.hashCode());
                    // 将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);

                    // 将socketChannel注册到selector中，只关心OP_READ事件，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (selectionKey.isReadable()) {
                    // OP_READ事件，客户端发送数据
                    // 通过selectionKey反向获取对应的channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    // 获取到channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                    // 从channel中读取客户端发送的数据
                    channel.read(buffer);
                    System.out.println("从客户端中读取到数据: " + new String(buffer.array()));
                    buffer.clear();
                }
                // 移除selectionKey，防止重复操作
                selectionKeyIterator.remove();
            }
        }
    }
}

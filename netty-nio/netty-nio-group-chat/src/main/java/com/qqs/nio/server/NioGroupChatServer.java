package com.qqs.nio.server;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;

public class NioGroupChatServer {
    public static void main(String[] args) {
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;
        try {
            // 创建选择器
            selector = Selector.open();
            // 创建通道
            serverSocketChannel = ServerSocketChannel.open();
            // 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(3209));
            // 设置非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 将通道注册到选择器中,并指定关心accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    // 有事件需要处理
                    Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                    // 使用迭代器遍历处理事件
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey selectionKey = selectionKeyIterator.next();

                        if (selectionKey.isAcceptable()) {
                            // 处理accept事件
                            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel socketChannel = channel.accept();
                            socketChannel.configureBlocking(false);
                            // 将socketChannel注册到selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            // 提示
                            System.out.println(socketChannel.getRemoteAddress() + " 上线");
                        }
                        if (selectionKey.isReadable()) {
                            // 处理read事件,即通道是可读状态
                            String message = handlerRead(selectionKey);
                            if (message != null) {
                                // 将读取到的消息发送给其他客户端
                                sendMessageToOtherClients(message, (SocketChannel) selectionKey.channel(), selector);
                            }
                        }
                        selectionKeyIterator.remove();
                    }
                } else {
                    System.out.println("等待...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverSocketChannel != null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将读取到的消息发送给其他客户端
     *
     * @param message  消息内容
     * @param self     排除无需发送的客户端
     * @param selector 选择器
     */
    private static void sendMessageToOtherClients(String message, SocketChannel self, Selector selector) {
        for (SelectionKey selectionKey : selector.keys()) {
            Channel channel = selectionKey.channel();
            // 排除无需发送的客户端
            if (channel instanceof SocketChannel && channel != self) {
                SocketChannel targetChannel = (SocketChannel) channel;
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                try {
                    targetChannel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        System.out.println(targetChannel.getRemoteAddress() + " 可能已离线");
                        // 取消注册
                        selectionKey.cancel();
                        // 关闭通道
                        targetChannel.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        }
    }


    /**
     * 处理读
     *
     * @param selectionKey SelectionKey
     */
    private static String handlerRead(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int len = 0;
        try {
            len = channel.read(buffer);
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 已离线");
                // 取消注册
                selectionKey.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (len > 0) {
            // 将缓冲区中的数据转成字符串
            String message = new String(Arrays.copyOf(buffer.array(), len));
            // 打印输出消息内容
            System.out.println("服务端接收到消息: " + message);
            return message;
        }
        return null;
    }


}

package com.qqs.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NioQuickStartClient {
    public static void main(String[] args) throws IOException {
        // 创建网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 设置服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 3208);
        // 连接服务端
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("连接服务端需要时间，连接期间客户端不会阻塞，可以去做其他工作");
            }
        }

        // 连接成功,发送数据
        String data = "hello，NioQuickStartServer";
        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
        socketChannel.write(buffer);

        // 阻塞客户端,防止客户端与客户端的连接挂掉
        System.in.read();
    }
}

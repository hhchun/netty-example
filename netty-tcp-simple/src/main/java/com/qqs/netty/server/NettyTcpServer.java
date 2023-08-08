package com.qqs.netty.server;

import com.qqs.netty.server.taskqueue.NettyServerCommonTaskHandler;
import com.qqs.netty.server.taskqueue.NettyServerScheduleTaskHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

public class NettyTcpServer {
    public static void main(String[] args) {

        // 创建两个线程组 bossGroup 和 workerGroup
        // bossGroup只处理连接请求,由 workerGroup 完成对客户端的业务处理
        // 两个都是无限循环
        // bossGroup 和 workerGroup 的子线程（NioEventLoop）个数,默认线程数为: 实际cpu核心数 * 2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置两个线程组
            bootstrap.group(bossGroup, workerGroup)
                    // 使用NioServerSocketChannel作为服务端的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 设置通道初始化对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 给pipeline添加处理器
                             socketChannel.pipeline().addLast(new NettyTcpServerHandler());
                            // socketChannel.pipeline().addLast(new NettyServerCommonTaskHandler());
                            // socketChannel.pipeline().addLast(new NettyServerScheduleTaskHandler());
                        }
                    });
            // 绑定端口,启动服务器
            ChannelFuture cf = bootstrap.bind(3208).sync();
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

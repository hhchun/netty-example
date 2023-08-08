package com.qqs.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyWebSocketServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 基于http协议,使用http的编解码器
                            pipeline.addLast(new HttpServerCodec());

                            // 由块的方式写,需要添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            // http数据传输过程是分段的,HttpObjectAggregator用于将多个段聚合
                            // 这就是为什么,当浏览器发送大量数据时,会发生多次http请求
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            // websocket的数据是以帧(frame)形式传递的
                            // WebSocketServerProtocolHandler 核心功能是将http协议升级为ws协议,保持长连接,是通过状态码101来完成的
                            pipeline.addLast(new WebSocketServerProtocolHandler("/netty-ws"));

                            // 业务处理器
                            pipeline.addLast(new NettyWebSocketServerHandler());
                        }
                    });
            // 启动服务,监听端口
            ChannelFuture channelFuture = bootstrap.bind(3208).sync();
            // 监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

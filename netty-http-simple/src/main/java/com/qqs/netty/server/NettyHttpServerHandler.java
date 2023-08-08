package com.qqs.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * SimpleChannelInboundHandler: ChannelInboundHandlerAdapter的子类
 * HttpObject: 客户端和服务端相互通讯的数据被封装成HttpObject类型
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            System.out.println("pipeline hashCode: " + ctx.pipeline().hashCode());
            System.out.println("msg class type: " + msg.getClass());
            System.out.println("client address: " + ctx.channel().remoteAddress());
            HttpRequest request = (HttpRequest) msg;
            if (Objects.equals("/favicon.ico", request.uri())) {
                System.out.println("针对/favicon.ico资源不进行响应处理");
                return;
            }
            ByteBuf content = Unpooled.copiedBuffer("hello,browser client", StandardCharsets.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将response进行响应返回
            ctx.writeAndFlush(response);
        }
    }
}

package com.qqs.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyHeartbeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            Channel channel = ctx.channel();
            if (IdleState.READER_IDLE.equals(state)) {
                System.out.println("client "+ channel.remoteAddress() + " read idle");
            }
            if (IdleState.WRITER_IDLE.equals(state)) {
                System.out.println("client "+ channel.remoteAddress() + " writer idle");
            }
            if (IdleState.ALL_IDLE.equals(state)) {
                System.out.println("client "+ channel.remoteAddress() + " all idle");
            }
        }
    }
}

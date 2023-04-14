package com.Richerlv.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author lvyanling
 * @date 2023-04-13
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf message = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务端读取数据 -> " + message.toString(Charset.forName("utf-8")));

        System.out.println(new Date() + ": 服务端回复数据");
        ByteBuf byteBuf = getByteBuf(ctx);
        ctx.channel().writeAndFlush(byteBuf);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        //准备数据
        byte[] bytes = "Hi, I am Richerlv!".getBytes(StandardCharsets.UTF_8);
        //填充数据
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }
}

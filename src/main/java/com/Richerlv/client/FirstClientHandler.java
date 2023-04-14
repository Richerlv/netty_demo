package com.Richerlv.client;

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
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端写入数据");
        //获取数据
        ByteBuf byteBuf = getByteBuf(ctx);
        //写数据
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf message = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读取数据 -> " + message.toString(Charset.forName("utf-8")));

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = ctx.alloc().buffer();
        //准备数据
        byte[] bytes = "Hello, Richerlv!".getBytes(StandardCharsets.UTF_8);
        //填充数据
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }
}

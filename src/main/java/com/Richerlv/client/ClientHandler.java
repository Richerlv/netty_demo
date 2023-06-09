package com.Richerlv.client;

import com.Richerlv.packet.LoginRequestPacket;
import com.Richerlv.packet.LoginResponsePacket;
import com.Richerlv.packet.MessageResponsePacket;
import com.Richerlv.packet.Packet;
import com.Richerlv.serializer.PacketCodeC;
import com.Richerlv.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

/**
 * @author lvyanling
 * @date 2023-04-14
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 客户端登陆
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登陆");

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUserName("辣条总统");
        loginRequestPacket.setPassword("123456");

        ByteBuf byteBuf = PacketCodeC.encode(loginRequestPacket);
        ctx.channel().writeAndFlush(byteBuf);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodeC.decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
                LoginUtil.markAsLogin(ctx.channel());
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }
        } else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            String fromUserId = messageResponsePacket.getFromUserId();
            String fromUserName = messageResponsePacket.getFromUserName();
            System.out.println(fromUserId + ":" + fromUserName + " -> " + messageResponsePacket .getMessage());
        }
    }
}

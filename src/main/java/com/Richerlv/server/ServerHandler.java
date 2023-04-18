package com.Richerlv.server;

import com.Richerlv.packet.*;
import com.Richerlv.serializer.PacketCodeC;
import com.Richerlv.util.SessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @author lvyanling
 * @date 2023-04-14
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        //解码
        Packet packet = PacketCodeC.decode(requestByteBuf);
        System.out.println(packet);
        if(packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            //构造登陆响应包
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            loginResponsePacket.setUserId(loginRequestPacket.getUserId());
            loginResponsePacket.setUserName(loginRequestPacket.getUserName());
            //校验
            if(valid(loginRequestPacket)) {
                System.out.println(new Date() + ": " + loginRequestPacket.getUserName() + "—登陆成功");
                loginResponsePacket.setSuccess(true);
            } else {
                System.out.println(new Date() + ": " + loginRequestPacket.getUserName() + "—登陆失败");
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setReason("账号密码校验失败");
            }

            //编码
            ByteBuf responseByteBuf = PacketCodeC.encode(loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        } else if (packet instanceof MessageRequestPacket) {
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket)packet;
            // 1.拿到消息发送方的会话信息
            Session session = SessionUtil.getSession(ctx.channel());

            // 2.通过消息发送方的会话信息构造要发送的消息
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setFromUserId(session.getUserId());
            messageResponsePacket.setFromUserName(session.getUserName());
            messageResponsePacket.setMessage(messageRequestPacket.getMessage());

            // 3.拿到消息接收方的 channel
            Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());

            // 4.将消息发送给消息接收方
            if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
                ByteBuf responseByteBuf = PacketCodeC.encode(messageResponsePacket);
                toUserChannel.writeAndFlush(responseByteBuf);
            } else {
                System.err.println("[" + messageRequestPacket.getToUserId() + "] 不在线，发送失败!");
            }
        }

    }

    /**
     * TODO: 登陆校验逻辑
     * @param loginRequestPacket
     * @return
     */
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}

package com.Richerlv.server;

import com.Richerlv.packet.*;
import com.Richerlv.serializer.PacketCodeC;
import io.netty.buffer.ByteBuf;
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
            loginResponsePacket.setUserName(loginRequestPacket.getUsername());
            //校验
            if(valid(loginRequestPacket)) {
                System.out.println(new Date() + ": " + loginRequestPacket.getUsername() + "—登陆成功");
                loginResponsePacket.setSuccess(true);
            } else {
                System.out.println(new Date() + ": " + loginRequestPacket.getUsername() + "—登陆失败");
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setReason("账号密码校验失败");
            }

            //编码
            ByteBuf responseByteBuf = PacketCodeC.encode(loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        } else if (packet instanceof MessageRequestPacket) {
            // 处理消息
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf = PacketCodeC.encode(messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
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

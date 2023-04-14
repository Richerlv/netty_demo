package com.Richerlv;

/**
 * @author lvyanling
 * @date 2023-04-14
 */

import com.Richerlv.packet.LoginRequestPacket;
import com.Richerlv.packet.Packet;
import com.Richerlv.serializer.JSONSerializer;
import com.Richerlv.serializer.PacketCodeC;
import com.Richerlv.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

public class PacketCodeCTest {
    @Test
    public void encode() {

        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserId(123);
        loginRequestPacket.setUsername("zhangsan");
        loginRequestPacket.setPassword("password");

        PacketCodeC packetCodeC = new PacketCodeC();
        ByteBuf byteBuf = packetCodeC.encode(loginRequestPacket);
        Packet decodedPacket = packetCodeC.decode(byteBuf);
        System.out.println(decodedPacket);

        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}

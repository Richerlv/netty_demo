package com.Richerlv.packet;

import lombok.Data;

import static com.Richerlv.packet.Command.LOGIN_REQUEST;

/**
 * @author lvyanling
 * @date 2023-04-14
 */

@Data
public class LoginRequestPacket extends Packet{

    private String userId;
    private String userName;
    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}

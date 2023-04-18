package com.Richerlv.packet;

import lombok.Data;

/**
 * @author lvyanling
 * @date 2023-04-17
 */

@Data
public class LoginResponsePacket extends Packet{

    private String userId;

    private String userName;

    private Boolean success;

    private String reason;

    public Boolean isSuccess() {
        return success;
    }

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}

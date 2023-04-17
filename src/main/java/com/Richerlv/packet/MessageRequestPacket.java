package com.Richerlv.packet;

import lombok.Data;

/**
 * @author lvyanling
 * @date 2023-04-17
 */

@Data
public class MessageRequestPacket extends Packet{

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}

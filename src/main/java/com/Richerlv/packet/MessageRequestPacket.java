package com.Richerlv.packet;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lvyanling
 * @date 2023-04-17
 */

@Data
@AllArgsConstructor
public class MessageRequestPacket extends Packet{

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}

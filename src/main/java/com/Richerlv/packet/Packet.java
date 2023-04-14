package com.Richerlv.packet;

import lombok.Data;

/**
 * @author lvyanling
 * @date 2023-04-14
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     * @return
     */
    public abstract Byte getCommand();
}

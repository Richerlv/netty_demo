package com.Richerlv.attribute;

import com.Richerlv.packet.Session;
import io.netty.util.AttributeKey;

/**
 * @author lvyanling
 * @date 2023-04-17
 */
public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}

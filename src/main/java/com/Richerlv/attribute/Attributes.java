package com.Richerlv.attribute;

import io.netty.util.AttributeKey;

/**
 * @author lvyanling
 * @date 2023-04-17
 */
public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}

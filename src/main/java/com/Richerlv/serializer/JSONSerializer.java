package com.Richerlv.serializer;

import com.alibaba.fastjson2.JSON;

/**
 * @author lvyanling
 * @date 2023-04-14
 */

interface SerializerAlgorithm {
    /**
     * json 序列化标识
     */
    byte JSON = 1;
}

public class JSONSerializer implements Serializer{
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}

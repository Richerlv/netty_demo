package com.Richerlv.serializer;

/**
 * @author lvyanling
 * @date 2023-04-14
 */
public interface Serializer {

    byte JSON_SERIALIZER = 1;
    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * Java转二进制对象
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 二进制转Java对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

}

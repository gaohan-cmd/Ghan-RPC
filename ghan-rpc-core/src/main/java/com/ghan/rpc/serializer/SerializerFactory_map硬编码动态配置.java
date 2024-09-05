package com.ghan.rpc.serializer;

import java.util.Map;

/**
 * 序列化工厂实现 *
 *
 * @author: Ghan
 * @time: 2024-08-29 15:15:29
 */
public class SerializerFactory_map硬编码动态配置 {

    /**
     * 序列化映射
     */
    private static final Map<String, Serializer> SERIALIZER_MAP = Map.of(
            SerializerKeys.JDK, new JdkSerializer(),
            SerializerKeys.JSON, new JsonSerializer(),
            SerializerKeys.KRYO, new KryoSerializer(),
            SerializerKeys.HESSIAN, new HessianSerializer()
    );

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = SERIALIZER_MAP.get(SerializerKeys.JDK);

    /**
     * 获取序列化器
     *
     * @param key 键名
     * @return 序列化器
     */
    public static Serializer getInstance(String key) {
        return SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
    }
}

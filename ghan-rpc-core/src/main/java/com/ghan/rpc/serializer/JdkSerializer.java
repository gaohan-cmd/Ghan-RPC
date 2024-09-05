package com.ghan.rpc.serializer;

import java.io.*;

/**
 * JdkSerializer *
 * JDK 序列化器
 * @author: Ghan
 * @time: 2024-08-29 18:43:41
 */
public class JdkSerializer implements Serializer{

    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        // 创建字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 创建对象输出流
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        // 写入对象
        objectOutputStream.writeObject(object);
        // 关闭流
        objectOutputStream.close();
        // 返回字节数组
        return outputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    // <T> 表示这个方法是泛型的，T 是一个占位符，表示方法返回类型的实际类型。方法的返回值类型为 T
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            objectInputStream.close();
        }
        return null;
    }
}

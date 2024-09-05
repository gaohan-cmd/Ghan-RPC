package com.ghan.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ghan.rpc.model.RpcRequest;
import com.ghan.rpc.model.RpcResponse;
import com.ghan.rpc.serializer.JdkSerializer;
import com.ghan.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ServiceLoader;

/**
 * 服务代理（JDK 动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *  invoke 方法通常是在动态代理对象中被调用，用于将方法调用转发到实际的服务端
     *  invoke 方法在方法调用时被触发
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        //Serializer serializer = new JdkSerializer();
        // 自定义序列化器-java内置spi机制实现
        // 指定序列化器
        Serializer serializer = null;
        ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);
        for (Serializer service : serviceLoader) {
            serializer = service;
        }

        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 发送请求
            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

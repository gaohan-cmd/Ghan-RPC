package com.ghan.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ghan.rpc.RpcApplication;
import com.ghan.rpc.config.RpcConfig;
import com.ghan.rpc.constant.RpcConstant;
import com.ghan.rpc.model.RpcRequest;
import com.ghan.rpc.model.RpcResponse;
import com.ghan.rpc.model.ServiceMetaInfo;
import com.ghan.rpc.registry.Registry;
import com.ghan.rpc.registry.RegistryFactory;
import com.ghan.rpc.serializer.Serializer;
import com.ghan.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

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
        // 方式一 指定序列化器
        //Serializer serializer = new JdkSerializer();

        // 方式二 自定义序列化器-java内置spi机制实现
        // 指定序列化器
//        Serializer serializer = null;
//        ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);
//        for (Serializer service : serviceLoader) {
//            serializer = service;
//        }

        // 方式三 自定义序列化器-动态配置实现
        // 指定序列化器
        String serviceName = method.getDeclaringClass().getName();
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());


        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        /**
         * 未采用注册中心，硬编码方式
         */
        /**
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
        }**/

        /**
         * 采用注册中心
         */
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            // 暂时先取第一个
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            // 发送请求
            try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

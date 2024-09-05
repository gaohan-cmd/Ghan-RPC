package com.ghan.demo.provider;

import cn.hutool.core.net.NetUtil;
import com.ghan.demo.common.service.UserService;
import com.ghan.rpc.RpcApplication;
import com.ghan.rpc.config.RegistryConfig;
import com.ghan.rpc.config.RpcConfig;
import com.ghan.rpc.model.ServiceMetaInfo;
import com.ghan.rpc.registry.LocalRegistry;
import com.ghan.rpc.registry.Registry;
import com.ghan.rpc.registry.RegistryFactory;
import com.ghan.rpc.server.HttpServer;
import com.ghan.rpc.server.VertxHttpServer;


/**
 * 服务提供者示例
 * 有注册中心-rpc动态配置
 *
 */
public class ProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}

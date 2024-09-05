package com.ghan.demo.provider;


import com.ghan.demo.common.service.UserService;
import com.ghan.rpc.RpcApplication;
import com.ghan.rpc.registry.LocalRegistry;
import com.ghan.rpc.server.HttpServer;
import com.ghan.rpc.server.VertxHttpServer;


/**
 * 服务提供者示例
 * 未有注册中心-rpc动态配置
 *
 */
public class ProviderExample_demo {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();
        
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}

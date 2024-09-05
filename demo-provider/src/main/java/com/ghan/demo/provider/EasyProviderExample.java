package com.ghan.demo.provider;

import com.ghan.demo.common.service.UserService;
import com.ghan.rpc.registry.LocalRegistry;
import com.ghan.rpc.server.HttpServer;
import com.ghan.rpc.server.VertxHttpServer;


/**
 * EasyProviderExample *
 * 简易服务提供者示例
 * @author: Ghan
 * @time: 2024-08-07 21:57:23
 */

public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}

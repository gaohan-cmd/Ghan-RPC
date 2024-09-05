package com.ghan.demo.consumer;

import com.ghan.demo.common.model.User;
import com.ghan.demo.common.service.UserService;
import com.ghan.rpc.config.RpcConfig;
import com.ghan.rpc.proxy.ServiceProxyFactory;
import com.ghan.rpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 *
 */
public class ConsumerExample {

//    public static void main(String[] args) {
//        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
//        System.out.println(rpc);
//    }

    public static void main(String[] args) {

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("gaohan");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
//        long number = userService.getNumber();
//        System.out.println(number);
    }
}

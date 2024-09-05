package com.ghan.demo.consumer;

import com.ghan.demo.common.model.User;
import com.ghan.demo.common.service.UserService;
import com.ghan.rpc.proxy.ServiceProxyFactory;

/**
 * EasyConsumerExample *
 * 简易服务消费者示例
 * @author: Ghan
 * @time: 2024-08-07 22:01:59
 */

public class EasyConsumerExample {

    public static void main(String[] args) {
        // todo 需要获取 UserService 的实现类对象
        //UserService userService = null;
        // 动态代理
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
    }
}

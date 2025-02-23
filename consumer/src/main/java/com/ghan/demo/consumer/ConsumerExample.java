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

    public static void main(String[] args){

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("zjr");
        // 调用
        try {
            User newUser = userService.getUser(user);
            System.out.println("Debug: newUser = " + newUser); // 添加此行
            if (newUser != null) {
                System.out.println(newUser.getName());
            } else {
                System.out.println("user == null");
            }
            // 第二次调用
            userService.getUser(user);
            userService.getUser(user);
            long number = userService.getNumber();
            System.out.println(number);
        } catch (Exception e) {
            e.printStackTrace(); // 强制打印异常堆栈
        }
    }
}

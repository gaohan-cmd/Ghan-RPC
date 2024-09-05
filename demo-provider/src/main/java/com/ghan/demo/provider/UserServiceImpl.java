package com.ghan.demo.provider;

import com.ghan.demo.common.model.User;
import com.ghan.demo.common.service.UserService;

/**
 * UserServiceImpl *
 * 用户服务实现类
 * @author: Ghan
 * @time: 2024-08-07 21:57:38
 */

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}

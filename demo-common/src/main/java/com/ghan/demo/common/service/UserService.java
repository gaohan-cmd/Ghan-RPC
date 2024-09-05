package com.ghan.demo.common.service;

import com.ghan.demo.common.model.User;

/**
 * User: Ghan
 * Date: 2024/8/7
 * Time: 21:49
 * Description:用户服务
 */

public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     * 新方法-测试mock
     * 实现类没有覆盖: 如果实现类没有重写接口的默认方法，那么调用时会使用接口中的默认实现。
     * 动态代理默认返回UserService对象 但是没有重写getUser 默认User对象为null，但默认getNumber为0
     */
    default short getNumber() {
        return 0;
    }
}

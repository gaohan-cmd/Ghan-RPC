package com.ghan.demo.common.model;

import java.io.Serializable;

/**
 * User *
 * 用户
 * @author: Ghan
 * @time: 2024-08-07 21:48:53
 */

public class User implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

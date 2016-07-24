package com.baidu.file.api.entity;

import com.baidu.file.api.Users;

/**
 * Created by zhangzf on 16/7/24.
 */
public interface UsersService {
    Users getUser(String username,String password);
}

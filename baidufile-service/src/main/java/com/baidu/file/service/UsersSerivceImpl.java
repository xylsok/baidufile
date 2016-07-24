package com.baidu.file.service;

import com.baidu.file.api.Users;
import com.baidu.file.api.entity.UsersService;
import com.baidu.file.dao.UsersDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhangzf on 16/7/24.
 */
@Service("UsersService")
public class UsersSerivceImpl implements UsersService {
    @Resource
    UsersDao usersDao;
    @Override
    public Users getUser(String username, String password) {
        return usersDao.getUser(username,password);
    }
}

package com.baidu.file.web.util;

import com.baidu.file.api.Users;
import com.baidu.file.api.entity.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by zhangzf on 16/7/24.
 */
@Path("user")
@Api(value = "用户管理", description = "用户管理")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class UsersWeb {
    @Resource
    UsersService usersService;

    @GET
    @Path("getuser")
    @ApiOperation(value = "获取一个试题的详细信息", notes = "获取一个试题的详细信息")
    public Users questDetail(@QueryParam("username") String username, @QueryParam("passowrd") String password) {
        return usersService.getUser(username, password);
    }
}

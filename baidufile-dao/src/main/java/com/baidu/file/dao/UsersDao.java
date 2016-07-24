package com.baidu.file.dao;
import com.baidu.file.api.Users;
import com.baidu.tables.records.UsersRecord;
import org.jooq.Result;
import org.springframework.stereotype.Component;

import static com.baidu.tables.Users.USERS;
/**
 * Created by zhangzf on 16/7/24.
 */
@Component
public class UsersDao extends JooqDao<UsersRecord,Users,String>{

    protected UsersDao() {
        super(USERS, Users.class);
    }

    @Override
    protected String getId(Users users) {
        return null;
    }

    public Users getUser(String username, String password) {

        Result<UsersRecord> fetch = create().selectFrom(USERS).where(USERS.NAME.eq(username)).and(USERS.PASSWORD.eq(password)).fetch();
        if(fetch.size()>0){
            return fetch.get(0).into(Users.class);
        }else{
            return null;
        }
    }
}

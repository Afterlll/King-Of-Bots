package com.kob.backend.service.user.account;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;

/**
 * 用户登录接口
 */
public interface LoginService {

    Result<User> getToken(String username, String password);

}

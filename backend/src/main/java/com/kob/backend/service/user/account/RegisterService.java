package com.kob.backend.service.user.account;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;

/**
 * 用户注册接口
 */
public interface RegisterService {
    Result<User> register(String username, String password, String confirmedPassword);
}

package com.kob.backend.service.user.account;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;

/**
 * 根据令牌返回用户信息
 */
public interface InfoService {

    Result<User> getInfo();

}

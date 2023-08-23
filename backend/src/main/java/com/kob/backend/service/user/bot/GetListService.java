package com.kob.backend.service.user.bot;

import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;

/**
 * 获取bot列表
 */
public interface GetListService {
    Result<Bot> getList();
}

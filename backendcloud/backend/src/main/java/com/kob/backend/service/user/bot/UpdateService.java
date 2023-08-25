package com.kob.backend.service.user.bot;

import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;

import java.util.Map;

/**
 * 更新一个bot
 */
public interface UpdateService {
    Result<Bot> update(Map<String, String> map);
}

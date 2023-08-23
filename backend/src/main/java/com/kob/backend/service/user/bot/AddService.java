package com.kob.backend.service.user.bot;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.Bot;

import java.util.Map;

/**
 * 添加一个bot
 */
public interface AddService {
    Result<Bot> insert(Map<String, String> data);
}

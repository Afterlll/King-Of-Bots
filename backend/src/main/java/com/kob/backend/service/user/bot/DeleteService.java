package com.kob.backend.service.user.bot;

import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;

import java.util.Map;

/**
 * 删除一个bot
 */
public interface DeleteService {
    Result<Bot> delete(Map<String, String> map);
}

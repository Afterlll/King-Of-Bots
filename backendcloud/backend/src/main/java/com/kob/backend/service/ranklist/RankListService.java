package com.kob.backend.service.ranklist;

import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;

public interface RankListService {
    /**
     * 返回page个记录
     * @param page
     * @return
     */
    Result<Bot> getList(Integer page);
}

package com.kob.backend.service.record;

import com.kob.backend.bean.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.backend.bean.Result;

/**
* @author 13547
* @description 针对表【record(对战记录表)】的数据库操作Service
* @createDate 2023-08-25 17:54:21
*/
public interface RecordService extends IService<Record> {
    /**
     * 查询第几页的数据
     * @param page
     * @return
     */
    Result<Record> getList(Integer page);
}

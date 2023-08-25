package com.kob.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.bean.Record;
import com.kob.backend.service.RecordService;
import com.kob.backend.mapper.RecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 13547
* @description 针对表【record(对战记录表)】的数据库操作Service实现
* @createDate 2023-08-25 17:54:21
*/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
    implements RecordService{

}





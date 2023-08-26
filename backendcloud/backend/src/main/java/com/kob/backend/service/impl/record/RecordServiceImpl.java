package com.kob.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.bean.Record;
import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.record.RecordService;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.utils.message.TotalMessage;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.*;

/**
* @author 13547
* @description 针对表【record(对战记录表)】的数据库操作Service实现
* @createDate 2023-08-25 17:54:21
*/
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record>
    implements RecordService{

    @Resource
    private RecordMapper recordMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result<Record> getList(Integer page) {
        IPage<Record> iPage = new Page<>(page, 10); // 一次返回十条记录
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id"); // 返回最新的记录
        recordMapper.selectPage(iPage, queryWrapper);
        List<Record> records = iPage.getRecords(); // 查询出的十条记录

        List<JSONObject> items = new LinkedList<>();
        // 还需要返回对战双方的用户名、头像、对战结果
        for (Record record : records) {
            Integer aId = record.getAId();
            Integer bId = record.getBId();
            User userA = userMapper.selectById(aId);
            User userB = userMapper.selectById(bId);
            String usernameA = userA.getUsername();
            String usernameB = userB.getUsername();
            String photoA = userA.getPhoto();
            String photoB = userB.getPhoto();
            String result = "平局";
            if ("A".equals(record.getLoser())) result = "B胜";
            else if ("B".equals(record.getLoser())) result = "A胜";
            JSONObject item = new JSONObject();
            item.put("a_username", usernameA);
            item.put("b_username", usernameB);
            item.put("a_photo", photoA);
            item.put("b_photo", photoB);
            item.put("result", result);
            item.put("record", record);
            items.add(item);
        }

        JSONObject resp = new JSONObject();
        resp.put("records", items);
        resp.put("records_count", iPage.getTotal());
        resp.put("size", iPage.getSize());
        resp.put("page", iPage.getPages());
        resp.put("current", iPage.getCurrent());

        return new Result<Record>("200", resp, TotalMessage.RETURN_RECORD_LIST);
    }
}





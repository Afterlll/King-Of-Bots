package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.ranklist.RankListService;
import com.kob.backend.utils.message.TotalMessage;
import lombok.experimental.Tolerate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RankListServiceImpl implements RankListService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Result<Bot> getList(Integer page) {
        IPage<User> iPage = new Page<>(page, 10); // 每页返回10条记录
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        userMapper.selectPage(iPage, queryWrapper);
        List<User> users = iPage.getRecords();
        for (User user : users) {
            user.setPassword("");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", users);
        jsonObject.put("page", iPage.getPages());
        jsonObject.put("total", iPage.getTotal());

        return new Result<>("200", jsonObject, TotalMessage.RETURN_RANK_LIST_LIST);
    }
}

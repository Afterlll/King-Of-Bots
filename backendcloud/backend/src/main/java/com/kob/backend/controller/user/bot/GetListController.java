package com.kob.backend.controller.user.bot;

import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;
import com.kob.backend.service.user.bot.GetListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class GetListController {

    @Resource
    private GetListService getListService;

    @GetMapping("/user/bot/getlist/")
    public Result<Bot> getList() {
        return getListService.getList();
    }

}

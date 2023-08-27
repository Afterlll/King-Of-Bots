package com.kob.backend.controller.user.bot;

import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class AddController {

    @Resource
    private AddService addService;

    @PostMapping("/api/user/bot/add/")
    public Result<Bot> add(@RequestParam Map<String, String> map) {
        return addService.insert(map);
    }

}

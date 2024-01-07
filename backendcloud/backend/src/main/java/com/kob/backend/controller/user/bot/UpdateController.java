package com.kob.backend.controller.user.bot;

import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class UpdateController {

    @Resource
    private UpdateService updateService;

    @PostMapping("/api/user/bot/update/")
    public Result<Bot> update(@RequestParam Map<String, String> map) {
        return updateService.update(map);
    }

}

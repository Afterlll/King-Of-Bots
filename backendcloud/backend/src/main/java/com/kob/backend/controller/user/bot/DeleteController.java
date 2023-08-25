package com.kob.backend.controller.user.bot;

import com.kob.backend.bean.Result;
import com.kob.backend.service.user.bot.DeleteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class DeleteController {

    @Resource
    private DeleteService deleteService;

    @PostMapping("/user/bot/remove/")
    public Result delete(@RequestParam Map<String, String> map) {
        return deleteService.delete(map);
    }

}

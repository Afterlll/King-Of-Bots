package com.kob.backend.controller.user.account;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class InfoController {

    @Resource
    private InfoService infoService;

    @GetMapping("/api/user/account/info/")
    public Result<User> getInfo() {
        return infoService.getInfo();
    }

}

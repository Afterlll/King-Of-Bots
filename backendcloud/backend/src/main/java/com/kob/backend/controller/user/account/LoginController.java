package com.kob.backend.controller.user.account;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.service.user.account.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/api/user/account/token/")
    public Result<User> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return loginService.getToken(username, password);
    }

}

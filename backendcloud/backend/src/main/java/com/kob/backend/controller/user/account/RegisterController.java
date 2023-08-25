package com.kob.backend.controller.user.account;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user/account/")
public class RegisterController {

    @Resource
    private RegisterService registerService;

    @PostMapping("register/")
    public Result<User> register(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmPassword = map.get("confirm_password");
        return registerService.register(username, password, confirmPassword);
    }
}

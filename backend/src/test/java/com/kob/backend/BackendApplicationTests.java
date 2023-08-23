package com.kob.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.bean.User;
import com.kob.backend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class BackendApplicationTests {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
//        System.out.println(passwordEncoder.encode("123"));
//        System.out.println(passwordEncoder.matches("1234", "$2a$10$Ov5iN5rN2tgl2Ss9hV5D7eeJLBL2d79fzeDaz6Tsvn.focTpEeG3u"));
//        $2a$10$2mgdDZCyB6Zbf4I2V81BW.uLaBcMw4BBxrwS8rdx8.1IMdXG00QIK
//        $2a$10$Ov5iN5rN2tgl2Ss9hV5D7eeJLBL2d79fzeDaz6Tsvn.focTpEeG3u
        //$2a$10$Ov5iN5rN2tgl2Ss9hV5D7eeJLBL2d79fzeDaz6Tsvn.focTpEeG3u


//        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", "Afterlll"));
//        System.out.println(user);
    }

}

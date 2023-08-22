package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/pk")
@RestController
public class IndexController {

    @GetMapping("/getBotInfo")
    public Map<String, Object> getBotInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("bot_name", "bot1");
        map.put("bot_age", 18);
        return map;
    }
}

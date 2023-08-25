package com.kob.mathcingsystem.controller;

import com.kob.mathcingsystem.service.MatchingServer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
public class MatchingController {

    @Resource
    private MatchingServer matchingServer;

    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return matchingServer.addPlayer(userId, rating);
    }
    @PostMapping("/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingServer.removePlayer(userId);
    }
}

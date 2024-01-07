package com.kob.backend.controller.ranklist;

import com.kob.backend.bean.Result;
import com.kob.backend.service.ranklist.RankListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class RankListController {
    @Resource
    private RankListService rankListService;

    @GetMapping("/api/rankList/getList/")
    public Result getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return rankListService.getList(page);
    }
}

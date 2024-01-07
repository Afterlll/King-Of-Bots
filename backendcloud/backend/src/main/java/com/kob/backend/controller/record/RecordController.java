package com.kob.backend.controller.record;

import com.kob.backend.bean.Record;
import com.kob.backend.bean.Result;
import com.kob.backend.service.record.RecordService;
import kotlin.properties.ObservableProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@RestController
public class RecordController {
    @Resource
    private RecordService recordService;

    @GetMapping("/api/record/getList/")
    public Result<Record> getList(@RequestParam Map<String, String> data) {
        int page = Integer.parseInt(Objects.requireNonNull(data.get("page")));
        return recordService.getList(page);
    }
}

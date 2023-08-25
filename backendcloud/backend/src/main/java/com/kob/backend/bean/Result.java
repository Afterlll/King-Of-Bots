package com.kob.backend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /**
     * 状态码
     */
    private String code;
    /**
     * 数据
     */
    private Object data;
    /**
     * 信息
     */
    private String message;

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

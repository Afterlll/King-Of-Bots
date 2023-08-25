package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 蛇的节点
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
    private Integer x, y;
}

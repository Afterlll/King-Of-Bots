package com.kob.backend.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * bot表
 * @TableName kob
 */
@TableName(value ="bot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot implements Serializable {
    /**
     * bot的唯一性id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * bot的所有者
     */
    private Integer userId;

    /**
     * bot的标题
     */
    private String title;

    /**
     * bot的描述
     */
    private String description;

    /**
     * bot的执行代码
     */
    private String content;

    /**
     * bot的天梯分（默认为1500）
     */
    private Integer rating;

    /**
     * bot的创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/shanghai")
    private Date createTime;

    /**
     * bot的修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/shanghai")
    private Date modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
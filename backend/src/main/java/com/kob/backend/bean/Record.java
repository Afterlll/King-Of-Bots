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
 * 对战记录表
 * @TableName record
 */
@TableName(value ="record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record implements Serializable {
    /**
     * 对战记录唯一性id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 玩家a的id
     */
    private Integer aId;

    /**
     * 玩家a起点的横坐标
     */
    private Integer aSx;

    /**
     * 玩家a起点的纵坐标
     */
    private Integer aSy;

    /**
     * 玩家b的id
     */
    private Integer bId;

    /**
     * 玩家b起点的横坐标
     */
    private Integer bSx;

    /**
     * 玩家b起点的纵坐标
     */
    private Integer bSy;

    /**
     * 玩家a蛇的移动方向
     */
    private String aSteps;

    /**
     * 玩家b蛇的移动方向
     */
    private String bSteps;

    /**
     * 玩家a和b当前对局的游戏地图
     */
    private String map;

    /**
     * 当前对局的败者，all: 平局，A: A输，B: B输
     */
    private String loser;

    /**
     * 对局的时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.better.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线文档:es实现的实体.
 *
 * @waring NULL
 * @return
 * @author SL
 * @date 2022/7/11 4:15 PM
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("article")
public class Article implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;


    /**
     * 摘要
     */
    private String summary;

    /**
     * 内容 字段类型将设置为text
     */
    private String originContent;

    /**
     * 无html标签内容,用于字段检索 字段类型将设置为text
     */
    private String htmlContent;


    /**
     * 文章作者
     */
    private String author;

    /**
     * 目录id
     */
    private Integer directoryId;


    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 创建者id
     */
    private Object userId;


    /**
     * 排序编号
     */
    private Long sortNum;

    /**
     * 删除标记
     */
    private int deleteFlag;

    /**
     * 关键字
     */
    private String keyword;


    /**
     * 描述
     */
    private String description;



    /**
     * url
     */
    private String url;


    private String seoTitle;

    /**
     * 绝对路径
     */
    private String absolutePath;

    /**
     * 绝对路径
     */
    private String  searchContent;
}

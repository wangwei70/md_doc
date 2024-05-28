package com.better.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleParam implements Serializable {

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
     * 目录id
     */
    private Integer directoryId;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 排序编号 (正序排列,越大越在最前)
     */
    private Long sortNum;

    /**
     * 作者
     */
    private String author;

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
}

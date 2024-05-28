package com.better.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo extends Article implements Serializable  {

    /**
     * 目录结构
     */
    private List<String> directoryList;

    /**
     * html摘要
     */
    private String htmlSummary;

    private String keyword;


}

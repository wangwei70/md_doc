package com.better.bean;


import lombok.Data;

import java.io.Serializable;

/**
 * 文章
 *
 * @waring NULL
 * @return 
 * @author SL
 * @date 2022/7/18 10:05 AM
 */
@Data
public class ArticleSearchParam extends PageVo implements Serializable {


    /**
     * 搜索内容(包含所有内容)
     */
    private String search;


    /**
     *  目录id
     */
    private Integer directoryId;


}

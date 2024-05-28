package com.better.bean;


import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 文章目录结构
 *
 * @example:
 *      AntDB-M
 *          1.3.1
 *              内核
 *              系统表
 *                  sys
 *                  sys_1
 *              公告文章
 *          1.4.0
 *          1.5.0
 *
 * @waring NULL
 * @return
 * @author SL
 * @date 2022/7/12 10:37 AM
 */
@Data
public class DirectoryVo implements Serializable {

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序字段
     */
    private Integer sortNum;

    /**
     * 最底部的文章目录
     */
    private List<ArticleVo> list;

}

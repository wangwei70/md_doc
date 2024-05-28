package com.better.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

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
 * @param
 * @waring NULL
 * @return
 * @author SL
 * @date 2022/7/12 10:37 AM
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("directory")
public class Directory implements Serializable {

    @TableId(value = "id")
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
    private Long sortNum;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 绝对路径
     */
    private String absolutePath;

}



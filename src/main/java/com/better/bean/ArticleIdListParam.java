package com.better.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文章id集合
 *
 * @waring NULL
 * @return
 * @author SL
 * @date 2022/7/13 9:47 AM
 */
@Data
public class ArticleIdListParam implements Serializable {

    private List<Integer> idList;

}

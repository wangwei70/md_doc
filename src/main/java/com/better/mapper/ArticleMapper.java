package com.better.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.better.bean.Article;
import com.better.bean.ArticleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * 在线文档mapper
 *
 * @author SL
 * @waring NULL
 * @return
 * @date 2022/7/11 4:28 PM
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {


    /**
     * 根据目录id获取文章
     *
     * @param directoryId
     * @return java.util.List<com.asiainfo.ahweb.common.entity.Article>
     * @waring NULL
     * @author SL
     * @date 2022/7/21 4:33 PM
     */
    List<Article> selectByDirectoryId(@Param("directoryId") Integer directoryId);


    List<Article> selectAll();


    List<Article> selectAllNoContent();


    Article selectById(@Param("id") Integer id);

    List<Article> selectAllByTitle(@Param("title") String title);

    List<Article> selectAllByTitleAndDirectoryId(@Param("title") String title, @Param("directoryId") Integer directoryId);

    int insertBatch(@Param("articleCollection") Collection<Article> articleCollection);

    List<ArticleVo> selectByKeyword(@Param("keyword")String keyword);
}

package com.better.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HtmlUtil;
import com.better.bean.*;
import com.better.mapper.ArticleMapper;
import com.better.service.ArticleService;
import com.better.service.DirectoryService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 在线文档service
 *
 * @author SL
 * @waring NULL
 * @return
 * @date 2022/7/11 4:31 PM
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private DirectoryService directoryService;


    @Override
    public List<Article> selectByDirectoryId(Integer directoryId) {
        List<Article> list = articleMapper.selectByDirectoryId(directoryId);
        return list;
    }

    /**
     * 全文检索
     *
     * @param param
     * @return void
     * @waring 后续优化:初始化版本
     * @version 1.0
     * @author SL
     * @date 2022/7/12 4:42 PM
     */
    @Override
    public Page<ArticleVo> listArticle(PageVo param) {
//        int num = param.getPageNum() == null ? 0 : param.getPageNum();
//        int size = param.getPageSize() == null ? 10 : param.getPageSize();
//
//        Pageable pageable = PageRequest.of(num, size, Sort.by("id").descending());
//
//        // 无搜索条件 查询所有
//        Page<Article> page = articleRepository.searchArticlesByDeleteFlag(Boolean.FALSE, pageable);
//        List<Article> content = page.getContent();
//        List<ArticleVo> articleVoList = content.stream().map(item -> {
//            ArticleVo articleVo = new ArticleVo();
//            BeanUtil.copyProperties(item, articleVo);
//            return articleVo;
//        }).collect(Collectors.toList());
//        Page<ArticleVo> pageVo = new PageImpl(articleVoList, pageable, page.getTotalPages());
//        return pageVo;

        return null;
    }


    /**
     * 添加文章
     *
     * @param articleParam
     * @param userID
     * @return com.asiainfo.ahweb.common.entity.Article
     * @waring NULL
     * @author SL
     * @date 2022/7/13 9:39 AM
     */
    @Override
    @Transactional
    public Article insertArticle(ArticleParam articleParam, Object userID) {

        if (articleParam.getDirectoryId() == null) {
            log.error("文章类型不可为空");
//            throw new DefinedException(400, "文章类型不可为空");
        }

        Article article = new Article();
        BeanUtil.copyProperties(articleParam, article);
        Long sortNum = articleParam.getSortNum() == null ? 0L : articleParam.getSortNum();

        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setUserId(userID);
        article.setSortNum(sortNum);
        article.setDeleteFlag(0);
        int insert = articleMapper.insert(article);
        if (insert > 0) {
//            article = saveToEs(article);
        }

        return article;

    }

    @Override
    public Article updateArticle(ArticleParam articleParam, Object userID) {
        Article article = articleMapper.selectById(articleParam.getId());
        if (article == null) {
            log.warn("数据不存在 id:{} title:{} ", articleParam.getId(), articleParam.getTitle());
//            throw new DefinedException(400, "数据不存在");
        }
        BeanUtil.copyProperties(articleParam, article, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        article.setUpdateTime(new Date());
        int i = articleMapper.updateById(article);
        if (i > 0) {
//            article = saveToEs(article);
        }
        return article;
    }


    /**
     * 删除文章: 假删除
     *
     * @param articleIdListParam
     * @param userID
     * @return java.lang.Boolean
     * @waring NULL
     * @author SL
     * @date 2022/7/12 4:38 PM
     */
    @Override
    @Transactional
    public Boolean deleteArticle(ArticleIdListParam articleIdListParam, Object userID) {

        List<Integer> idList = articleIdListParam.getIdList();
        if (CollectionUtil.isEmpty(idList)) {
            return Boolean.TRUE;
        }

        for (Integer id : idList) {
            Article article = articleMapper.selectById(id);
            if (article == null) {
                continue;
            }
            article.setDeleteFlag(1);
            articleMapper.updateById(article);
        }

//        articleRepository.deleteAllById(idList);

        return Boolean.TRUE;
    }


    /**
     * 根据id查询文章详情信息
     *
     * @param id
     * @return com.asiainfo.ahweb.common.vo.article.ArticleVo
     * @waring NULL
     * @author SL
     * @date 2022/7/15 10:29 AM
     */
    @Override
    public ArticleVo detailArticle(Integer id) {

//        Article article = articleRepository.findFirstByIdAndDeleteFlag(id, Boolean.FALSE);
//
//        if (article == null) {
//            throw new DefinedException(400, "数据不存在");
//        }
//
//        ArticleVo articleVo = new ArticleVo();
//        BeanUtil.copyProperties(article, articleVo);
//        // 直接展示文档原内容，不进行\n替换
//        String markdown2Html = article.getContent();
//        articleVo.setContent(markdown2Html);
//        // 目录赋值
//        List<String> directoryList = directoryService.selectArticleDirectory(article.getDirectoryId());
//        articleVo.setDirectoryList(directoryList);
//
//        return articleVo;
        return null;
    }


    /**
     * 搜索内容
     *
     * @param searchContent
     * @return java.util.List<com.asiainfo.ahweb.common.vo.article.ArticleVo>
     * @waring NULL
     * @author SL
     * @date 2022/7/25 9:33 AM
     */
    @Override
    public List<ArticleVo> searchArticleList(String searchContent) {
//        // 字段查询
//        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(searchContent, "title", "summary", "noHtmlContent");
//
//        String[] indexs = {"ah_web", "ah_web_news"};
//
//        try {
//            List<Article> articles = elasticsearchService.searchList(queryBuilder,
//                    Arrays.asList(new SortOrderVo("_score", SortOrder.DESC), new SortOrderVo("id", SortOrder.DESC))
//                    , Article.class
//                    , Arrays.asList("title", "summary", "noHtmlContent")
//                    , indexs
//            );
//
//            return articles.stream().map(item -> {
//                ArticleVo articleVo = new ArticleVo();
//                BeanUtil.copyProperties(item, articleVo);
//                return articleVo;
//            }).collect(Collectors.toList());
//
//        } catch (IllegalAccessException e) {
//            log.error("searchArticleList============={}", e.getMessage());
//            throw new DefinedException(400, "查询错误");
//        }
        return null;
    }

    @Override
    public List<Article> selectAllNoHtmlField() {
        List<Article> articles = articleMapper.selectAll();
        // 去除html标签
        articles.forEach(item -> item.setOriginContent(HtmlUtil.cleanHtmlTag(item.getOriginContent())));
        return articles;

    }

}









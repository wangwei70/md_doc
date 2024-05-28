package com.better.controller;

import com.better.bean.ApiResult;
import com.better.bean.ArticleSearchParam;
import com.better.bean.ArticleVo;
import com.better.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


/**
 * 在线文档Controller
 *
 * @waring NULL
 * @return
 * @author SL
 * @date 2022/7/11 4:58 PM
 */
@RestController
@RequestMapping("/article")
@Tag(name = "文档页面", description = "用户端的文档查询")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文档分页查询
     *
     * @param articleSearchParam
     * @waring NULL
     * @return com.asiainfo.ahweb.config.ApiResult<org.springframework.data.domain.Page < com.asiainfo.ahweb.common.vo.article.ArticleVo>>
     * @author SL
     * @date 2022/7/18 5:08 PM
     */
    @PostMapping("/page/list")
    @Operation(summary = "分页查询")
    public ApiResult<Page<ArticleVo>> listArticle(@RequestBody ArticleSearchParam articleSearchParam){

        return ApiResult.ok(articleService.listArticle(articleSearchParam));

    }

    /**
     * 文章详情
     *
     * @param id
     * @waring NULL
     * @return com.asiainfo.ahweb.config.ApiResult<com.asiainfo.ahweb.common.vo.article.ArticleVo>
     * @author SL
     * @date 2022/7/20 5:14 PM
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "根据ID获取详情")
    public ApiResult<ArticleVo> detailArticle(@PathVariable("id") Integer id){

        return ApiResult.ok(articleService.detailArticle(id));
    }



}













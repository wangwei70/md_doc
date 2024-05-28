package com.better.config;


import cn.hutool.core.collection.CollectionUtil;
import com.better.bean.Article;
import com.better.bean.ArticleParam;
import com.better.bean.Directory;
import com.better.mapper.ArticleMapper;
import com.better.mapper.DirectoryMapper;
import com.better.service.ArticleService;
import com.better.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: wanwgei70
 * @Description:
 * @Date Created at 10:36 2024/5/7
 * @Modified By:
 */
@Component
public class DocInitConfig {

    @Autowired
    private DocProperty property;

    private static final Logger logger = LoggerFactory.getLogger(DocInitConfig.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private DocService mdImportService;

    @Autowired
    private DirectoryMapper directoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @PostConstruct
    private void init() {

        logger.info("*********************开始文档解析***************************");
        if (!property.getEnableReload()) {
            return;
        }
        // 清除历史文档
        cleanHistory(1);
        // 导入文档目录
        importDir(1);
        // 填充文档内容
        ArrayList<Future> futures = paddingConetent();
        for (Future future : futures) {
            try {
                future.get(100, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);

            }
        }
        logger.info("*********************帮助文档解析完成***************************");

    }

    private ArrayList<Future> paddingConetent() {
        List<Article> articles = articleMapper.selectAll();
        AtomicInteger threadNumber = new AtomicInteger(1);
        ExecutorService executorService = new ThreadPoolExecutor(5,
                5,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue(100),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "docInitPool_" + threadNumber.getAndIncrement());
                    }
                }
        );
        ArrayList<Future> futures = new ArrayList<>();
        for (Article article : articles) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                ArticleParam articleParam = new ArticleParam();
                BeanUtils.copyProperties(article, articleParam);
                mdImportService.importMdFile(articleParam.getAbsolutePath(), -1);
            }, executorService)
                    .exceptionally(e -> {
                        CompletionException ex = new CompletionException(e);
                        throw ex;
                    });
            futures.add(future);
        }

        return futures;
    }


    private void importDir(int id) {
        Directory directory = new Directory();
        directory.setId(id);
        directory.setName("test");
        directory.setDescription("测试数据");
        directory.setAbsolutePath(property.getAccurateDir());
        directory.setCreateTime(new Date());
        directoryMapper.insert(directory);
        mdImportService.importDirectory(property.getAccurateDir(), 1);
    }

    private void cleanHistory(int id) {
        LinkedList<Integer> allDirs = new LinkedList<>();
        LinkedList<Integer> allArts = new LinkedList<>();
        collectDirAndArts(id, allDirs, allArts);
        if (!CollectionUtil.isEmpty(allDirs)) {
            directoryMapper.deleteBatchIds(allDirs);
        }
        if (!CollectionUtil.isEmpty(allArts)) {
            articleMapper.deleteBatchIds(allArts);
        }
    }


    // 递归删除
    private void collectDirAndArts(Integer id, LinkedList<Integer> allDirs, LinkedList<Integer> allArts) {
        Directory directory = directoryMapper.selectById(id);
        if (!Objects.isNull(directory)) {
            allDirs.add(directory.getId());
        }
        // 查询子目录
        List<Directory> dirs = directoryMapper.selectDirectoryByParentId(id);
        // 查询子文章
        List<Article> arts = articleMapper.selectByDirectoryId(id);
        if (!CollectionUtil.isEmpty(arts)) {
            List<Integer> artIds = arts.stream().map(art -> art.getId()).collect(Collectors.toList());
            allArts.addAll(artIds);
        }

        if (!CollectionUtil.isEmpty(dirs)) {
            List<Integer> dirIds = dirs.stream().map(dir -> dir.getId()).collect(Collectors.toList());
            for (Integer dirId : dirIds) {
                collectDirAndArts(dirId, allDirs, allArts);
            }
        }
    }

}

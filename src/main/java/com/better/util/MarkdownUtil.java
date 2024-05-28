package com.better.util;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.util.Arrays;

/**
 * *********************************************************************************************
 * *
 *
 * @File Name : MarkdownUtil
 * *
 * @Author Name :  Sl
 * *
 * @Start Date : 2022/7/20 11:17 PM
 * *
 * @Last Update : 2022/7/20 11:17 PM
 * *
 * @Description : markdown 转换html工具类
 * <p>
 * *
 * ----------------------------------------------------------------------------------------------
 */
public class MarkdownUtil {


    /**
     * 直接将markdown语义的文本转为html格式输出
     *
     * @param content markdown语义文本
     * @return
    */
    public static String markdown2Html(String content) {
        // 不替换\n ， 替换\n会造成code部分代码缩为一行
        String html = parse(content);
         return html;
    }


    /**
     * markdown to image
     *
     * @param content markdown contents
     * @return parse html contents
     */
    private static String parse(String content) {
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        //enable table parse!
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(content);
        return renderer.render(document);
    }


}

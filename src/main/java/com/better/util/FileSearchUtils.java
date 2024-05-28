package com.better.util;

import java.io.File;
import java.util.List;

/**
 * @Author: JeanRiver
 * @Description:
 * @Date Created at 10:28 2022/8/29
 * @Modified By:
 */
public class FileSearchUtils {

    /**
     * 递归遍历目标文件夹下的所有文件
     *
     * @param directoryName 文件夹的路径
     * @param suffix        要搜索的文件后缀名，例子 .md
     */
    public static void searchBySuffix(String directoryName, String suffix, List<File> files) {
        File directory = new File(directoryName);
        //This method gets all files (subFolders are not included) from a directory.
        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isFile() && file.getName().contains(suffix)) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    // 递归 recursion
                    searchBySuffix(file.getAbsolutePath(), suffix, files);
                }
            }
    }
}

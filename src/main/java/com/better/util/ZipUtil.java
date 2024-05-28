package com.better.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtil.class);

    /**
     * 压缩文件
     * @param zipFilePath 需要被压缩的文件绝对路径
     * @param zipFilePathName 压缩后的文件绝对路径(带后缀名)
     */
    public static void zip(String zipFilePath,String zipFilePathName){
        ZipOutputStream zos=null;
        try {
            //创建压缩后的文件路径
            FileOutputStream fos = new FileOutputStream(zipFilePathName);
            zos=new ZipOutputStream(fos);
            File file=new File(zipFilePath);
            compress(zos,file,"");
            LOGGER.info("zip finish");
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException:{}",e);
        } catch (IOException e) {
            LOGGER.error("IOException:{}",e);
        } finally {
            IOUtils.closeQuietly(zos);
        }
    }

    /**
     * 压缩文件：压缩一个目录下所有文件及文件夹
     * @param zos 压缩输出流
     * @param file 被压缩文件绝对路径
     * @param base 被压缩文件名
     */
    private static void compress(ZipOutputStream zos, File file, String base) throws IOException {
        LOGGER.info("zipping:{}",file.getName());
        //判断当前文件是目录还是文件
        if(file.isDirectory()){
            File[] files=file.listFiles();
            zos.putNextEntry(new ZipEntry(base+file.getName()+"/"));
            for(int i=0;i<files.length;i++){
                //递归压缩文件
                compress(zos,files[i],base+file.getName()+"/");
            }
        }else{
            zos.putNextEntry(new ZipEntry(base+file.getName()));
            FileInputStream fis=new FileInputStream(file);
            byte[] buffer=new byte[1024*2];
            int len;
            while((len=fis.read(buffer))!=-1){
                zos.write(buffer,0,len);
            }
            fis.close();
        }
    }

    public static void main(String[] args) {
        //测试压缩文件
        String zipFilePath="D:\\IdeaProjects\\md_doc\\src\\main\\resources\\doc";
        String zipFilePathName="D:\\IdeaProjects\\md_doc\\src\\main\\resources\\doc.zip";
        zip(zipFilePath,zipFilePathName);

    }
}

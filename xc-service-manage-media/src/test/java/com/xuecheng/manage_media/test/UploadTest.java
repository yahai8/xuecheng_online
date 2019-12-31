package com.xuecheng.manage_media.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author: MuYaHai
 * Date: 2019/12/10, Time: 20:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UploadTest {
    /**
     * 文件分片
     * @throws IOException
     */
    @Test
    public void testChunk() throws IOException {
        //资源文件位置
        File sourceFile = new File("E:\\ffmpegTest\\lucene.mp4");
        //分块地址
        String chunkPath = "E:\\ffmpegTest\\chunk\\";
        File chunkFile = new File(chunkPath);
        if (!chunkFile.exists()) {
            chunkFile.mkdirs();
        }
        //分块大小
        long chunkSize=1024*1024*1;//kb
        //分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        if (chunkNum <= 0) {
            chunkNum = 1;
        }
        byte[] bytes = new byte[1024];
        //使用ranndomaccessFile访问文件
        RandomAccessFile raf_read = new RandomAccessFile(sourceFile, "r");//只读
        for (int i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            //创建成功就进行分片
            boolean newFile = file.createNewFile();
            if (newFile) {
                RandomAccessFile raf_write = new RandomAccessFile(file, "rw");
                int len = -1;
                while ((len = raf_read.read(bytes)) != -1) {
                    raf_write.write(bytes, 0, len);
                    if (file.length() >chunkSize) {
                        break;
                    }
                }
                raf_write.close();
            }
        }
        raf_read.close();
    }


    /**
     * 合并文件
     */
    @Test
    public void merge() throws IOException {
        //资源文件
        File sourceFile = new File("E:\\ffmpegTest\\chunk\\");
        //合并后的文件
        File mergeFile = new File("E:\\ffmpegTest\\haha.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        mergeFile.createNewFile();
        //写文件
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");//读和写
        //指针指向文件顶端
        raf_write.seek(0);
        //缓冲区
        byte[] bytes = new byte[1024];
        File[] files = sourceFile.listFiles();
        //转成集合，便于排序
        ArrayList<File> fileArrayList = new ArrayList<>(Arrays.asList(files));
        //从小到大
        Collections.sort(fileArrayList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;//降序
                }
                return -1;//升序，0是等
            }
        });

        //合并文件
        for (File file : fileArrayList) {
            RandomAccessFile raf_read = new RandomAccessFile(file, "rw");
            int len = -1;
            while ((len = raf_read.read(bytes)) != -1) {
                raf_write.write(bytes, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}

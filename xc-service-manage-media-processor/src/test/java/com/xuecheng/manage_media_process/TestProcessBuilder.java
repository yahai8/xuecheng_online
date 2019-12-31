package com.xuecheng.manage_media_process;

import com.xuecheng.framework.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {

    @Test
    public void testProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder();
//        processBuilder.command("ping","121.40.74.83");
        processBuilder.command("ipconfig");
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            //启动进程
            Process start = processBuilder.start();
            //获取输入流
            inputStream = start.getInputStream();
            //转成字符流
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
            int len = -1;
            char[] chars = new char[1024];
            StringBuffer buffer = new StringBuffer();
            while ((len = inputStreamReader.read(chars)) != -1) {
                String string = new String(chars, 0, len);
                buffer.append(string);
                System.out.println(buffer.toString());
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFFmpeg() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> command = new ArrayList<>();
        command.add("E:\\develop\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe");
        command.add("-i");
        command.add("E:\\ffmpegTest\\1.avi");
        command.add("-y");//覆盖输出文件
        command.add("-c:v");
        command.add("libx264");
        command.add("-s");
        command.add("1280x720");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-b:a");
        command.add("63k");
        command.add("-b:v");
        command.add("753k");
        command.add("-r");
        command.add("18");
        command.add("E:\\ffmpegTest\\1.mp4");
        processBuilder.command(command);
        processBuilder.redirectErrorStream(true);
        try {
            Process start = processBuilder.start();
            InputStream inputStream = start.getInputStream();
            int len = -1;
            byte[] bytes = new byte[1024];
            StringBuffer stringBuffer = new StringBuffer();
            while ((len = inputStream.read(bytes)) != -1) {
                String s = new String(bytes, 0, len);
                stringBuffer.append(s);
                System.out.println(stringBuffer.toString());
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUtil() {
        String ffmpeg_path = "E:/develop/ffmpeg-20180227-fa0c9d6-win64-static/bin/ffmpeg.exe";
        //源视频路径
        String video_path = "E:\\ffmpegTest\\1.avi";
        String mp4_name = "1.mp4";
        String mp4_path = "E:\\ffmpegTest\\";
        //创建转换工具
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4_path);
        String s = mp4VideoUtil.generateMp4();
        System.out.println(s);
    }
}

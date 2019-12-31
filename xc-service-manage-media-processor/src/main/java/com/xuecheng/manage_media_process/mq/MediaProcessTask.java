package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: MuYaHai
 * Date: 2019/12/12, Time: 14:44
 */
@Component
public class MediaProcessTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaProcessTask.class);

    @Value("${xc-service-manage-media.ffmpeg-path}")
    String ffmpeg_path;

    @Value("${xc-service-manage-media.video-location}")
    String server_path;

    @Autowired
    MediaFileRepository mediaFileRepository;

    @RabbitListener(queues="${xc-service-manage-media.mq.queue-media-video-processor}",containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg) {
        Map map = JSON.parseObject(msg, Map.class);
        /**
         * 1）接收视频处理消息
         *
         * 2）判断媒体文件是否需要处理（本视频处理程序目前只接收avi视频的处理）
         *
         * 当前只有avi文件需要处理，其它文件需要更新处理状态为“无需处理”。
         *
         * 3）处理前初始化处理状态为“未处理”
         *
         * 4）处理失败需要在数据库记录处理日志，及处理状态为“处理失败”
         *
         * 5）处理成功记录处理状态为“处理成功”
         */
        //解析消息
        String mediaId = (String) map.get("mediaId");
        //获取媒资文件
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        if (!mediaFileOptional.isPresent()) {
            return;
        }
        MediaFile mediaFile = mediaFileOptional.get();
        //获取媒资文件类型
        String fileType = mediaFile.getFileType();

        if (fileType == null || !fileType.equals("avi")) {
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
            return;
        } else {
            mediaFile.setProcessStatus("303001");
            mediaFileRepository.save(mediaFile);
        }
        //地址
        String video_path = server_path + mediaFile.getFilePath() + mediaFile.getFileName();
        //视频名
        String mp4_name = mediaFile.getFileId() + ".mp4";
        String mp4Folder_path = server_path + mediaFile.getFilePath();
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4Folder_path);
        String result = mp4VideoUtil.generateMp4();
        if (result == null || !"success".equals(result)) {
            //操作失败
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 m3u8 = new MediaFileProcess_m3u8();
            m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }

        //生成m3u8
        video_path = server_path + mediaFile.getFilePath() + mp4_name;
        String m3u8_name = mediaFile.getFileId() + ".m3u8";
        String m3u8folder_path = server_path + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path, video_path, m3u8_name, m3u8folder_path);
        result = hlsVideoUtil.generateM3u8();
        if (result == null || !"success".equals(result)) {
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
        }

        //获取m3u8列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        //更新状态为成功
        mediaFile.setProcessStatus("303002");
        MediaFileProcess_m3u8 fileProcessM3u8 = new MediaFileProcess_m3u8();
        fileProcessM3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(fileProcessM3u8);
        //m3u8文件url
        mediaFile.setFileUrl(mediaFile.getFilePath() + "hls/" + m3u8_name);
        mediaFileRepository.save(mediaFile);
    }
}

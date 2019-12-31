package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.Map;

/**
 * @author: MuYaHai
 * Date: 2019/12/2, Time: 19:44
 */
@Service
public class FileSystemService {
    private static Logger LOGGER = LoggerFactory.getLogger(FileSystemService.class);

    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;

    @Autowired
    FileSystemRepository fileSystemRepository;

    //加载fdfs
    private void initFdfsConfig() {
        try{
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }


    //上传文件到fdfs
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metedata) {
        if (multipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //上传文件到fdfs
        String fileId = this.fdfs_upload(multipartFile);
        if (fileId == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        //创建文件信息对象
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFiletag(filetag);
        fileSystem.setFilePath(fileId);
        fileSystem.setBusinesskey(businesskey);
        //名称
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        //大小
        fileSystem.setFileSize(multipartFile.getSize());
        //文件类型
        fileSystem.setFileType(multipartFile.getContentType());
        if (StringUtils.isNotEmpty(metedata)) {
            try {
                Map map = JSON.parseObject(metedata, Map.class);
                fileSystem.setMetadata(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //保存到mongodb
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }

    private String fdfs_upload(MultipartFile multipartFile) {
        try {
            //加载配置文件
            this.initFdfsConfig();
            //创建tracker客户端
            TrackerClient trackerClient = new TrackerClient();
            //获取trackerServer服务端
            TrackerServer trackerServer = trackerClient.getConnection();
            //得到storage服务
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //创建storage客户端
            StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
            //获取文件原始名称
            String filename = multipartFile.getOriginalFilename();
            //扩展名
            String extName = filename.substring(filename.lastIndexOf(".") + 1);
            //存储到fdfs
            String fileId = storageClient.upload_file1(multipartFile.getBytes(), extName, null);
            return fileId;
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return null;
    }
}

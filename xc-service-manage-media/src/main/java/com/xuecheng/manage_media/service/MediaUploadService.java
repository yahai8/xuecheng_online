package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: MuYaHai
 * Date: 2019/12/10, Time: 21:26
 */
@Service
public class MediaUploadService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MediaUploadService.class);
    @Autowired
    MediaFileRepository mediaFileRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    //上传文件根目录
    @Value("${xc-service-manage-media.upload-location}")
    String uploadPath;
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    String routingkey_media_video;
    /**
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     * @param fileMd5 文件md5值
     * @param fileExt 文件扩展名
     * @return
     */
    private String getFilePath(String fileMd5, String fileExt) {
        //真正的文件路径，有后缀
        String filePath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
        return filePath;
    }


    //存放文件的目录路径，不是文件，没有根路径，用于存入mongodb
    private String getFileFolderRelativePath(String fileMd5, String Ext) {
        String filePath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/"+fileMd5+"/";
        return filePath;
    }

    //得到存放文件的目录,去掉文件扩展名
    private String getFileFolderPath(String fileMd5) {
        String filePath = uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        return filePath;
    }

    //创建文件目录
    private Boolean createFileFolder(String fileMd5) {
        //创建文件上传目录
        String fileFolderPath = getFileFolderPath(fileMd5);
        File file = new File(fileFolderPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            return mkdirs;
        }
        return true;
    }

    //文件注册
    public ResponseResult regsiter(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //检查文件是否上传
        //得到文件路径
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //查询数据库查看文件是否上传
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(fileMd5);
        //如果文件存在，且mongoDB也上传成功了，抛出异常
        if (file.exists() && mediaFileOptional.isPresent()) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //创建存放文件的目录
        Boolean fileFolder = createFileFolder(fileMd5);
        if (!fileFolder) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_CREATEFOLDER_FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //得到块文件所在目录
    private String getChunkFileFolderPath(String fileMd5) {
        //得到文件目录
        String fileFolderPath = this.getFileFolderPath(fileMd5);
        String fileChunkPath = fileFolderPath + "/chunks/";
        return fileChunkPath;
    }

    //检查块文件
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //得到块文件所在路径
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath+chunk);
        if (file.exists()) {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, true);
        } else {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, false);
        }
    }

    //块文件上传
    public ResponseResult  uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        if (file == null) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_IS_NULL);
        }
        //创建存放块文件目录
        Boolean fileFolder = this.creatChunkFileFolder(fileMd5);
        //块文件
        File chunkFile = new File(this.getChunkFileFolderPath(fileMd5) + chunk);
        //上传块文件
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("upload chunk file file:{}", e.getMessage());
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //创建存放块文件目录
    private Boolean creatChunkFileFolder(String fileMd5) {
        //得到存放块文件路径
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath);
        if (!file.exists()) {
            boolean newFile = file.mkdirs();
            return newFile;
        }
        return true;
    }

    //1）将块文件合并
    //
    //2）校验文件md5是否正确
    //
    //3）向Mongodb写入文件信息

    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //获取块文件路径
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkFile = new File(chunkFileFolderPath);
        if (!chunkFile.exists()) {
            chunkFile.mkdirs();
        }
        //合并文件路径
        File merge = new File(this.getFilePath(fileMd5, fileExt));
        //不存在则创建
        if (merge.exists()) {
            merge.delete();
        }
        boolean newFile = false;
        try {
           newFile= merge.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("mergechunks..create mergeFile fail:{}",e.getMessage());
        }
        if (!newFile) {
            ExceptionCast.cast(MediaCode.MEGER_FILE_CREATEFAIL);
        }

        //获取块文件列表
        List<File> chunkFileList = this.getChunkFileList(chunkFile);
        //合并文件
        merge = this.mergerFile(merge, chunkFileList);
        if (merge == null) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        //校验文件
        boolean checkResult = this.checkFileMd5(merge, fileMd5);
        if (!checkResult) {
            ExceptionCast.cast(MediaCode.CHECK_FILE_MD5_FAIL);
        }
        //将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5+"."+fileExt);
        mediaFile.setFileType(fileExt);
        mediaFile.setFileSize(fileSize);
        mediaFile.setFileOriginalName(fileName);
        //保存的相对路径
        mediaFile.setFilePath(this.getFileFolderRelativePath(fileMd5,fileExt));
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        //301002为上传成功状态码
        mediaFile.setFileStatus("301002");
        mediaFile.setFileUrl(this.getFileFolderRelativePath(fileMd5,fileExt) + fileMd5+"."+fileExt);
        MediaFile save = mediaFileRepository.save(mediaFile);
        String fileId = mediaFile.getFileId();
        //向mq发送消息
        this.sendProcessVideoMsg(fileId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private boolean checkFileMd5(File merge, String fileMd5) {
        if (merge == null || StringUtils.isEmpty(fileMd5)) {
            return false;
        }
        //进行md5校验
        FileInputStream mergeFileInputStream = null;
        try {
            mergeFileInputStream = new FileInputStream(merge);
            //得到文件的md5
            String md5Hex = DigestUtils.md5Hex(mergeFileInputStream);
            //比较md5
            if (fileMd5.equalsIgnoreCase(md5Hex)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("checkFileMd5 fail:{}",e.getMessage());
        }finally {
            try {
                mergeFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //合并文件
    private File mergerFile(File merge, List<File> chunkFileList) {
        try {
            //创建写文件对象
            RandomAccessFile raf_write = new RandomAccessFile(merge, "rw");
            int len = -1;
            byte[] bytes = new byte[1024];
            for (File file : chunkFileList) {
                //创建读文件对象
                RandomAccessFile raf_read = new RandomAccessFile(file, "r");
                while ((len = raf_read.read(bytes)) != -1) {
                    raf_write.write(bytes, 0, len);
                }
                raf_read.close();
            }
            raf_write.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("merger chunk file fail:{}", e.getMessage());
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
            return null;
        }
        return merge;
    }

    private List<File> getChunkFileList(File chunkFile) {
        File[] files = chunkFile.listFiles();
        ArrayList<File> fileArrayList = new ArrayList<>(Arrays.asList(files));
        Collections.sort(fileArrayList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });
        return fileArrayList;
    }


    //向mq发送消息
    public ResponseResult sendProcessVideoMsg(String mediaId) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mediaId", mediaId);
        String msg = JSON.toJSONString(hashMap);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video, msg);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("send media process task error,msg is:{},error:{}",msg,e.getMessage());
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}

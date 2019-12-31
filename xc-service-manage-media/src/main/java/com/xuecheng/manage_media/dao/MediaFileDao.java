package com.xuecheng.manage_media.dao;

import com.xuecheng.framework.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/12/12, Time: 16:42
 */
public interface MediaFileDao extends MongoRepository<MediaFile,String> {

    @Override
    void deleteById(String s);
}

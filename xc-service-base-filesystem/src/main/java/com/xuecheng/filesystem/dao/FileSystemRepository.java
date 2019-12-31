package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/12/2, Time: 19:43
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}

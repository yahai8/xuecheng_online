package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 15:44
 */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary,String> {
    SysDictionary findByDType(String type);
}

package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/11/25, Time: 20:34
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {

}

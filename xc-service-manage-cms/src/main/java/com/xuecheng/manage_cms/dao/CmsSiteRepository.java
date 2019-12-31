package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/11/23, Time: 18:33
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}

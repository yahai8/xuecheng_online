package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/11/28, Time: 18:41
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}

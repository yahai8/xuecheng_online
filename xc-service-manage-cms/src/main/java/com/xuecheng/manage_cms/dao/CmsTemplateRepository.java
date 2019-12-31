package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/11/23, Time: 19:33
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}

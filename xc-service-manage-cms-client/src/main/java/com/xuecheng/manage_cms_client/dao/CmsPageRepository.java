package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/11/28, Time: 18:40
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
}

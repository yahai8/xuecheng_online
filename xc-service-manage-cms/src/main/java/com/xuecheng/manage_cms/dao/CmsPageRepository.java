package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author: MuYaHai
 * Date: 2019/11/20, Time: 20:52
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    CmsPage findByPageName(String pageName);

    CmsPage findByPageNameAndPageType(String pageName, String pageType);

    int countBySiteIdAndPageType(String sitedId, String pageType);

    Page<CmsPage> findBySiteIdAndPageType(String sitedId, String pageType, Pageable pageable);

    CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId,String pageName,String pageWebPath);
}

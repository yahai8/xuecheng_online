package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * @author: MuYaHai
 * Date: 2019/11/28, Time: 18:45
 */
@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;

    public void savePageToServerPath(String pageId) {
        Optional<CmsPage> pageOptional = cmsPageRepository.findById(pageId);
        if (!pageOptional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISIT);
        }
        CmsPage cmsPage = pageOptional.get();
        String siteId = cmsPage.getSiteId();
        Optional<CmsSite> siteOptional = cmsSiteRepository.findById(siteId);
        if (!siteOptional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_SITE_ISNULL);
        }
        CmsSite cmsSite = siteOptional.get();
        //页面物理路径
        String pagePath =cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        System.out.println(pagePath);
        String htmlFileId = cmsPage.getHtmlFileId();
        InputStream inputStream = this.getFileById(htmlFileId);
        if (inputStream == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        FileOutputStream outputStream = null;
        try {
            //指定路径并初始化outputStream
            outputStream = new FileOutputStream(new File(pagePath));
            //保存到服务器静态页面路径下
            IOUtils.copy(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private InputStream getFileById(String htmlFileId) {
        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
            if (gridFSFile == null) {
                ExceptionCast.cast(CmsCode.CMS_GRIDFS_ISNULL);
            }
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

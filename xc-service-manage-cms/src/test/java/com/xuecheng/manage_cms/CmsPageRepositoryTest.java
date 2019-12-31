package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: MuYaHai
 * Date: 2019/11/20, Time: 20:51
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsRepository;

    @Test
    public void test01() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<CmsPage> all = cmsRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testInsert() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("张三");
        cmsPage.setDataUrl("http://www.xuecheng.com");
        cmsPage.setHtmlFileId("123");
        cmsPage.setSiteId("4545");
        List<CmsPageParam> cmsPageParamList = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("hahaha");
        cmsPageParam.setPageParamValue("7889");
        cmsPageParamList.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParamList);
        CmsPage save = cmsRepository.save(cmsPage);
        System.out.println(save);
    }

    @Test
    public void testUpdate() {
        Optional<CmsPage> optional = cmsRepository.findById("5dd539799547563830b5d288");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("李四");
            CmsPage save = cmsRepository.save(cmsPage);
            System.out.println(save);
        }
    }

    @Test
    public void testDelete() {
        cmsRepository.deleteById("5dd539799547563830b5d288");
    }
}

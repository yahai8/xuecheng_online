package com.xuecheng.manage_cms;

import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: MuYaHai
 * Date: 2019/12/3, Time: 23:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FreemarkerTest {
    @Autowired
    PageService pageService;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Test
    public void getHtml() throws IOException {
        String pageHtml = pageService.getPageHtml("5de67edc9547562cb47e1df8");
        System.out.println(pageHtml);
        InputStream inputStream = IOUtils.toInputStream(pageHtml, "utf-8");
        ObjectId objectId = gridFsTemplate.store(inputStream, "课程详情模板老王", "");
        System.out.println(objectId);
    }
}

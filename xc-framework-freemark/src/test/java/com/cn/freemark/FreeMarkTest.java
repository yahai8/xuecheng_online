package com.cn.freemark;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author: MuYaHai
 * Date: 2019/11/25, Time: 19:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FreeMarkTest {
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置模板路径
        String path = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path+"/templates/"));
        //设置字符集
        configuration.setDefaultEncoding("utf-8");
        //加载模板
        Template template = configuration.getTemplate("test1.ftl");
        //创建数据模型
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "迪丽热巴");
        //静态化
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(html);
        //转化为输出流
        InputStream inputStream = IOUtils.toInputStream(html);
        //输出文件
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d://test.html"));
        int copy = IOUtils.copy(inputStream, fileOutputStream);
        System.out.println(copy);
    }

    @Test
    public void testFreemarkerString() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>hello world</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h2>Hello ${name}</h2>\n" +
                "<hr>\n" +
                "</body>\n" +
                "</html>";
        configuration.setDefaultEncoding("utf-8");
        //模板加载器
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        //创建模板
        templateLoader.putTemplate("template", html);
        //设置模板加载器
        configuration.setTemplateLoader(templateLoader);
        //得到模板
        Template template = configuration.getTemplate("template");
        //创建模型
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "迪丽热巴");
        //静态化
        String htmlPage = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        System.out.println(htmlPage);
        InputStream inputStream = IOUtils.toInputStream(htmlPage);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d://test2.html"));
        int copy = IOUtils.copy(inputStream, fileOutputStream);
    }

    @Test
    public void testGridFs() throws FileNotFoundException {
        File file = new File("d:/index_banner.ftl");
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectId store = gridFsTemplate.store(fileInputStream,"轮播图测试文件05","");
        System.out.println(store.toString());
    }

    @Test
    public void queryFile() throws IOException {
        String fileId="5dde71a6d7dfd619b4e22f3d";
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        String s = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(s);
    }

    //文件存储2
    @Test
    public void testStore2() throws FileNotFoundException {
        File file = new File("D:\\course.ftl");
        FileInputStream inputStream = new FileInputStream(file);
        //保存模版文件内容
        ObjectId gridFSFile = gridFsTemplate.store(inputStream, "课程详情模板文件22","");
//        String fileId = gridFSFile.getId().toString();
        System.out.println(gridFSFile.toString());
    }


}

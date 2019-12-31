package com.xuecheng.search.test;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: MuYaHai
 * Date: 2019/12/7, Time: 19:30
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    //创建索引库
    @Test
    public void testCreate() throws IOException {
        CreateIndexRequest indexRequest = new CreateIndexRequest("xc_sms");
        indexRequest.settings(Settings.builder()
                .put("number_of_shards",1)
                .put("number_of_replicas",0));

        indexRequest.mapping("doc", " {\n" +
                " \t\"properties\": {\n" +
                "           \"name\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"description\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"studymodel\": {\n" +
                "              \"type\": \"keyword\"\n" +
                "           },\n" +
                "           \"price\": {\n" +
                "              \"type\": \"float\"\n" +
                "           }\n" +
                "        }\n" +
                "}", XContentType.JSON);

        //创建索引操作客户端
        IndicesClient indices = restHighLevelClient.indices();
        //创建响应对象
        CreateIndexResponse createIndexResponse = indices.create(indexRequest);
        //得到响应结果
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    //删除索引库
    @Test
    public void delete() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_sms");
        //删除索引
        DeleteIndexResponse xc_sms = restHighLevelClient.indices().delete(deleteIndexRequest);
        //删除响应结果
        boolean acknowledged = xc_sms.isAcknowledged();
        System.out.println(acknowledged);
    }

    //添加文档
    @Test
    public void testAdd() throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "spring cloud实战");
        map.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka。");
        map.put("studymodel", "201001");
        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("timestamp", dateFormat.format(new Date()));
        map.put("price", 5.6f);
        //索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_sms", "doc");
        //指定索引文档内容
        indexRequest.source(map);
        //索引响应对象
        IndexResponse index = restHighLevelClient.index(indexRequest);
        //获取响应结果
        DocWriteResponse.Result result = index.getResult();
        System.out.println(result);
    }

    //查询文档
    @Test
    public void getDoc() throws IOException {
        GetRequest getRequest = new GetRequest(
                "xc_sms",
                "doc",
                "KcQy4G4BojjCsczBhDiV"
        );

        GetResponse documentFields = restHighLevelClient.get(getRequest);
        boolean exists = documentFields.isExists();
        Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    //更新文档
    @Test
    public void update() throws IOException {
        UpdateRequest xc_sms = new UpdateRequest(
                "xc_sms",
                "doc",
                "KcQy4G4BojjCsczBhDiV");
        Map<String, String> map = new HashMap<>();
        map.put("description", "spring cloud实战");
        UpdateRequest updateRequest = xc_sms.doc(map);
        UpdateResponse update = restHighLevelClient.update(xc_sms);
        GetResult getResult = update.getGetResult();
        RestStatus status = update.status();
        System.out.println(getResult);
    }

    //删除文档
    @Test
    public void delDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(
                "xc_sms",
                "doc",
                "KcQy4G4BojjCsczBhDiV");
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest);
        RestStatus status = delete.status();
        DocWriteResponse.Result result = delete.getResult();
        System.out.println(result);
    }
}

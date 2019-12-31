package com.xuecheng.search.test;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author: MuYaHai
 * Date: 2019/12/7, Time: 20:15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSerach {

    @Autowired
    RestHighLevelClient client;

    //搜索type下全部记录
    @Test
    public void testSearchAll() throws IOException, ParseException {
        //创建搜索请求
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //设置类型
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
/*        //分页
        int page = 2;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);*/
        //搜索全部
        String[] ids = {"1", "2"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));
        //suorce源过滤
        searchSourceBuilder.fetchSource(
                new String[]{"name", "studymodel", "price", "timestamp"},
                new String[]{});
        //设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        SearchResponse search = client.search(searchRequest);
        //搜索匹配结果
        SearchHits hits = search.getHits();
        //搜索总记录
        long totalHits = hits.totalHits;
        //匹配度较高的前n个文档
        SearchHit[] hits1 = hits.getHits();
        //日期格式化对象
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit documentFields : hits1) {
            String id = documentFields.getId();
            //源文档内容
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
            //获取源文档name
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name);
        }
    }

    //根据关键字搜索
    @Test
    public void testMatchQuery() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //设置类型
        searchRequest.types("doc");
        //搜索源对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
/*        searchSourceBuilder.query(QueryBuilders.matchQuery("description", "spring开发框架")
                .operator(Operator.OR)
                .minimumShouldMatch("50%"));*/
        searchSourceBuilder.query(
                QueryBuilders.multiMatchQuery(
                        "spring css", "name","description")
                        .operator(Operator.OR)
                        .minimumShouldMatch("50%")
                        .field("name",10));
        //source源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        long totalHits = searchHits.totalHits;
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(price);
            System.out.println("===================================");
        }
    }

    //布尔查询
    @Test
    public void boolQuery() throws IOException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //设置类型
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%")
                .field("name", 10);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        long totalHits = searchHits.totalHits;
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String id = hit.getId();
            Map<String, Object> map = hit.getSourceAsMap();
            String name = (String) map.get("name");
            String studymodel = (String) map.get("studymodel");
            Double price = (Double) map.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(price);
            System.out.println("===================================");
        }
    }
}

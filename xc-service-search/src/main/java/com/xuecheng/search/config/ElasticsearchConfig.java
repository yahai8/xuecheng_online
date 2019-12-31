package com.xuecheng.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: MuYaHai
 * Date: 2019/12/7, Time: 19:07
 */
@Configuration
public class ElasticsearchConfig {

    //elasticsearch地址
    @Value("${xuecheng.elasticsearch.hostlist}")
    private String hostlist;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        //解析hostlist配置信息
        String[] split = hostlist.split(",");
        //创建httphost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHosts = new HttpHost[split.length];
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            httpHosts[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]));
        }
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }

    @Bean
    public RestClient restClient() {
        //解析hostlist配置信息
        String[] split = hostlist.split(",");
        //创建httphost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHosts = new HttpHost[split.length];
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            httpHosts[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]));
        }
        return RestClient.builder(httpHosts).build();
    }
}

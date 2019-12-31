package com.cn.controller;

import com.cn.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: MuYaHai
 * Date: 2019/11/25, Time: 18:40
 */
@Controller
public class BannerController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/getBanner")
    public String getName(Map<String, Object> map) {
        String url = "http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);
        Map body = forEntity.getBody();
        map.put("model", body);
        return "index_banner";
    }

    @GetMapping("/getCourseView")
    public String getCourseView(Map<String, Object> map) {

        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31200/course/courseview/402885816240d276016240f7e5000002", Map.class);
        Map body = forEntity.getBody();
        map.put("model", body);
        return "course";
    }
}

package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.course.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 15:53
 */
@RestController
@RequestMapping("/sys")
public class SysDictionaryController implements SysDictionaryControllerApi {
    @Autowired
    SysDictionaryService sysDictionaryService;

    @Override
    @GetMapping("/dictionary/get/{type}")
    public SysDictionary getDictionary(@PathVariable("type") String type) {
        return sysDictionaryService.get(type);
    }
}

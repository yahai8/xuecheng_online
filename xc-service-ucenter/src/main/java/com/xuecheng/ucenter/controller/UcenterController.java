package com.xuecheng.ucenter.controller;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: MuYaHai
 * Date: 2019/12/17, Time: 16:49
 */
@RestController
@RequestMapping("/ucenter")
public class UcenterController implements com.xuecheng.api.ucenter.UcenterControllerApi {
    @Autowired
    UserService userService;

    @Override
    @GetMapping("/getuserext")
    public XcUserExt getXcUserExt(@RequestParam("username") String name) {
        return userService.geXcUserExt(name);
    }
}

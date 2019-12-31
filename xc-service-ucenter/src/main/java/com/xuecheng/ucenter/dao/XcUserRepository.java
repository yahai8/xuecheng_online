package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: MuYaHai
 * Date: 2019/12/17, Time: 16:39
 */
public interface XcUserRepository extends JpaRepository<XcUser, String> {

    //根据用户名查询用户信息
    XcUser findXcUserByUsername(String username);
}

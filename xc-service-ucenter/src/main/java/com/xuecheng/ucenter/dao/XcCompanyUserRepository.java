package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: MuYaHai
 * Date: 2019/12/17, Time: 16:40
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser, String> {

    //根据用户id查询公司信息
    XcCompanyUser findXcCompanyUserByUserId(String userId);
}

package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.framework.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: MuYaHai
 * Date: 2019/12/16, Time: 19:53
 */
@RestController
public class AuthController extends BaseController implements AuthControllerApi {
    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Autowired
    AuthService authService;

    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) {
        //检验账号是否输入
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        AuthToken authToken = authService.login(loginRequest.getUsername(), loginRequest.getPassword(), clientId, clientSecret);
        //将令牌写入cookie父域名下
        //得到token
        String access_token = authToken.getAccess_token();
        this.saveCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS, access_token);
    }

    //将令牌保存到cookie中
    private void saveCookie(String access_token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //添加cookie认证令牌，最后一个参数设置为false，表示允许浏览器获取
        CookieUtil.addCookie(response,cookieDomain,"/","uid",access_token,cookieMaxAge,false);
    }

    //用户退出
    @Override
    @PostMapping("/userlogout")
    public ResponseResult logout() {
        //取出身份令牌
        String token = this.getTokenFormCookie();
        //删除redis中的token
        authService.delToken(token);
        //删除cookie
        this.clearCookie(token);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //删除用户cookie
    private void clearCookie(String token) {
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
    }

    @Override
    @GetMapping("/userjwt")
    public JwtResult userjwt() {
        String token = this.getTokenFormCookie();
        AuthToken authToken = authService.getUserToken(token);
        if (authToken == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        String jwt_token = authToken.getJwt_token();
        return new JwtResult(CommonCode.SUCCESS, jwt_token);
    }

    //从cookie中获取token
    private String getTokenFormCookie() {
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String token = cookieMap.get("uid");
        return token;
    }
}

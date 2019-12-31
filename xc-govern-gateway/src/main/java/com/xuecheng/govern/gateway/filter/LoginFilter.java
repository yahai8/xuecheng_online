package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: MuYaHai
 * Date: 2019/12/17, Time: 19:49
 */
@Component
public class LoginFilter extends ZuulFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

    @Autowired
    AuthService authService;

    @Override
    public String filterType() {
        //四种类型 pre ,routing, post, error
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0; //int值来定义过滤器的执行顺序，数值越小优先级越高
    }

    @Override
    public boolean shouldFilter() {
        return true;//该过滤器需要执行
    }

    @Override
    public Object run() throws ZuulException {
        //上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //响应对象
        HttpServletResponse response = requestContext.getResponse();
        //请求对象
        HttpServletRequest request = requestContext.getRequest();
        //查询身份令牌
        String token = authService.getTokenFromCookie(request);
        if (token == null) {
            access_denied();
        }
        //查询jwt令牌
        String jwt = authService.getJwtFromHeader(request);
        if (jwt == null) {
            access_denied();
        }
        return null;
    }

    private void access_denied() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.setSendZuulResponse(false);//拒绝访问
        //设置响应内容
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String jsonString = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(jsonString);
        requestContext.getResponse().setContentType("application/json;charset=utf-8");
    }
}

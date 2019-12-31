package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: MuYaHai
 * Date: 2019/12/17, Time: 20:22
 */
@Service
public class AuthService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //从header中查询jwt令牌
    public String getJwtFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            //拒绝访问
            return null;
        }
        if (!authorization.startsWith("Bear ")) {
            //拒绝访问
            return null;
        }
        return authorization;
    }

    //查询身份令牌
    public String getTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String token = cookieMap.get("uid");
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return token;
    }

    //查询令牌有效期
    public long getExpire(String token) {
        String key = "user_token:" + token;
        Long expire = stringRedisTemplate.getExpire(key);
        return expire;
    }
}

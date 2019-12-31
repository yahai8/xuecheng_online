package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import com.google.inject.internal.cglib.core.$KeyFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

/**
 * @author: MuYaHai
 * Date: 2019/12/16, Time: 19:10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestCreateJwt {

    @Test
    public void testCreateJwt() throws IOException {
        //证书文件
        String key_location = "xc.keystore";
        //密码
        String keystore_password = "xuechengkeystore";
        //访问证书路径
        ClassPathResource classPathResource = new ClassPathResource("/"+key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keystore_password.toCharArray());
        String keyPassword = "xuecheng";
        String alias = "xckey";
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keyPassword.toCharArray());
        //私钥
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", 123);
        hashMap.put("name", "张三");
        hashMap.put("roles", "r01,r02");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(hashMap), new RsaSigner(rsaPrivateKey));
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
    //eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6InIwMSxyMDIiLCJuYW1lIjoi5byg5LiJIiwiaWQiOjEyM30.jwwROe-BU6CTbAVF7n34lrRe5JJRL2Z0WLGhJCzkYgBnnutF3XG6SMv7otm7xdbBxrAuvSTd1zQNgOL41rbsjENvGjefZGTFJ2i4EkGLFMfSsM_n35N_DLFsjxaEMzoWP_A21sgAh6gdHFbmA_N6GG3RM3qf0MA-_O-BZa2qLeczjjHF9tG0va4oT5fFFA7euoRs-Jcn6_Yh7tdP6SnWsEI5pSub9_xuMjI_EH7lsIusJu6QrQsxYbnm1tE85NVoOWcA0yqi24Qi91be3tdDWv8oDgGkPRNi9N4t1ELmAYX2TyWMPEFPXl0KlT12ljm1lsOdT3RvrT6khRJcX_sU2w

    @Test
    public void testVerify() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6InIwMSxyMDIiLCJuYW1lIjoi5byg5LiJIiwiaWQiOjEyM30.jwwROe-BU6CTbAVF7n34lrRe5JJRL2Z0WLGhJCzkYgBnnutF3XG6SMv7otm7xdbBxrAuvSTd1zQNgOL41rbsjENvGjefZGTFJ2i4EkGLFMfSsM_n35N_DLFsjxaEMzoWP_A21sgAh6gdHFbmA_N6GG3RM3qf0MA-_O-BZa2qLeczjjHF9tG0va4oT5fFFA7euoRs-Jcn6_Yh7tdP6SnWsEI5pSub9_xuMjI_EH7lsIusJu6QrQsxYbnm1tE85NVoOWcA0yqi24Qi91be3tdDWv8oDgGkPRNi9N4t1ELmAYX2TyWMPEFPXl0KlT12ljm1lsOdT3RvrT6khRJcX_sU2w";

    }
}

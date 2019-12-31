package com.xuecheng.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: MuYaHai
 * Date: 2019/12/17, Time: 17:03
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestBcryptPasswordEncoder {

    @Test
    public void testEncoder() {
        BCryptPasswordEncoder cryptPasswordEncoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 10; i++) {
            String encode = cryptPasswordEncoder.encode("123");
            System.out.println(encode);
            boolean b = cryptPasswordEncoder.matches("123", encode);
            System.out.println(b);
        }
    }
}

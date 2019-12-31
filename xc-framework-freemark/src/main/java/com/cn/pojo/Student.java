package com.cn.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author: MuYaHai
 * Date: 2019/11/25, Time: 18:36
 */
@Data
@ToString
public class Student {
    private String name;
    private int age;
    private Date birthday;
    private Float money;
    private List<Student> friends;
    private Student besetFriend;
}

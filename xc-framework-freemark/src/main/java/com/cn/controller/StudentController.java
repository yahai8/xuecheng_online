package com.cn.controller;

import com.cn.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: MuYaHai
 * Date: 2019/11/25, Time: 18:40
 */
@Controller
public class StudentController {

    @RequestMapping("get")
    public String getName(Map<String,Object> map) {
        map.put("name", "佟丽娅");
        Student liukaiwei = new Student();
        liukaiwei.setName("刘恺威");
        liukaiwei.setAge(35);

        Student yangmi = new Student();
        yangmi.setName("杨幂");
        yangmi.setAge(33);
        yangmi.setBesetFriend(liukaiwei);

        Student gulinazha = new Student();
        gulinazha.setName("古力娜扎");
        gulinazha.setAge(22);

        Student dilireba = new Student();
        dilireba.setName("迪丽热巴");
        dilireba.setAge(25);
        ArrayList<Student> tongliyaFriends = new ArrayList<>();
        tongliyaFriends.add(gulinazha);
        tongliyaFriends.add(dilireba);

        Student tongliya = new Student();
        tongliya.setName("佟丽娅");
        tongliya.setAge(25);
        tongliya.setFriends(tongliyaFriends);

        ArrayList<Student> students = new ArrayList<>();
        students.add(yangmi);
        students.add(tongliya);
        map.put("students", students);


        Map<String, Student> studentMap = new HashMap<>();
        studentMap.put("yangmi", yangmi);
        studentMap.put("tongliya", tongliya);
        map.put("studentMap", studentMap);
        return "test1";
    }
}

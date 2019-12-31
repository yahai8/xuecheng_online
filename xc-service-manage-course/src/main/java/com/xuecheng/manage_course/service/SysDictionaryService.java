package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: MuYaHai
 * Date: 2019/11/29, Time: 15:45
 */
@Service
public class SysDictionaryService {
    @Autowired
    SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary get(String type) {
        SysDictionary sysDictionary = sysDictionaryRepository.findByDType(type);
        if (sysDictionary==null) {
            ExceptionCast.cast(CommonCode.DICTIONARY_ISNULL);
        }

        return sysDictionary;
    }
}

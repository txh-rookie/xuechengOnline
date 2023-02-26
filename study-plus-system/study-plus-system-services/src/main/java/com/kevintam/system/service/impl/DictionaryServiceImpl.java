package com.kevintam.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.system.entity.Dictionary;
import com.kevintam.system.mapper.DictionaryMapper;
import com.kevintam.system.service.DictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/19
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {


    @Override
    public List<Dictionary> queryAll() {
        List<Dictionary> list = this.list();
        return list;
    }

    @Override
    public Dictionary getByCode(String code) {
        Dictionary byId = this.getById(code);
        return byId;
    }
}

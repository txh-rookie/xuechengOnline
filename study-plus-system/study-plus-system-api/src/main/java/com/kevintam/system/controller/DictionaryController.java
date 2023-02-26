package com.kevintam.system.controller;

import com.kevintam.system.entity.Dictionary;
import com.kevintam.system.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/2/19
 */
@Slf4j
@RestController
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @GetMapping("/dictionary/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @GetMapping("/dictionary/code/{code}")
    public Dictionary getByCode(@PathVariable String code) {
        return dictionaryService.getByCode(code);
    }
}

package com.kevintam.content.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2023/3/1
 */
@Controller
public class FreemarkerController {

    @RequestMapping("/hello")
    public ModelAndView hello(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hello");
        modelAndView.addObject("name","Freemarker");
        return modelAndView;
    }
    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") String courseId){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("model",null);
        modelAndView.setViewName("course_template");
        return modelAndView;
    }
}

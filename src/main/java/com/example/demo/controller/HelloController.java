package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.utils.RestBean;


@Controller
@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public RestBean<String> hello()
    {
        return RestBean.success("你访问了/hello");
    }
    
    @GetMapping("/")
    public RestBean<String> hello_html()
    {
        return RestBean.success("index");
    }

}

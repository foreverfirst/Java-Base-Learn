package com.lc.controller;

import com.lc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: lc
 * @CreateDate: 2020/05/02 15:03
 **/
@RestController
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping(value = "/hello")
    public String hello(){
        return "hello";

    }

    @GetMapping(value = "time")
    public String getTime() {
        return helloService.getTime();
    }
}

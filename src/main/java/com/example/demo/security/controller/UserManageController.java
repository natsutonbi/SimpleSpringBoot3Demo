package com.example.demo.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.security.service.MyUserManager;
import com.example.demo.utils.RestBean;

public class UserManageController {

    @Autowired
    MyUserManager manager;

    @RequestMapping("/delete")
    public RestBean<String> delete(@RequestParam("username") String username){
        manager.deleteUser(username);
        return RestBean.success("success delete user %s".formatted(username));
    }
}

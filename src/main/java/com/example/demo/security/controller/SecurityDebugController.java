package com.example.demo.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user/debug")
public class SecurityDebugController {

    @RequestMapping("/whoami")
    public String whoami(Authentication currentAuth){
        if(currentAuth == null)
            return "you are visitor";
        return currentAuth.getName();
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping("/admin")
    public String admin(){
        return "success";
    }

    @PreAuthorize("hasRole('nonexist')")
    @RequestMapping("/nonexistrole")
    public String nonexistRole(){
        return "success";
    }

    @PreAuthorize("hasAuthority('nonexist')")
    @RequestMapping("/nonexistauth")
    public String nonexistAuth(){
        return "success";
    }

    @PreAuthorize("hasAuthority('all')")
    @RequestMapping("/all")
    public String allAuth(){
        return "success";
    }
}

package com.example.demo.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.entity.dto.MyUser;
import com.example.demo.security.entity.vo.request.EmailRegistVO;
import com.example.demo.security.mapper.entity.Account;
import com.example.demo.security.service.AccountService;
import com.example.demo.security.service.JwtService;
import com.example.demo.security.service.MailService;
import com.example.demo.security.service.MyUserManager;
import com.example.demo.utils.RestBean;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;


@RestController
@RequestMapping("/api/user")
@Tag(name = "用户基础接口", description = "注册/jwt续签/注销")
public class UserBaseController {

    @Autowired
    MyUserManager manager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    GrantedAuthorityDefaults defaults;

    @Autowired
    MailService mailService;

    @Autowired
    AccountService accountService;

    @PostMapping("/simpleregist")
    public RestBean<String> regist(@RequestParam("password") String password){
        String username = manager.getNewUsername();
        Account account = new Account()
                                .setUsername(username)
                                .setPassword(encoder.encode(password))
                                .setEnabled(true);
        MyUser user = new MyUser(account, null).addRole("USER");
        manager.createUser(user);
        return RestBean.success("你的用户id为"+username);
    }

    @GetMapping("/mailcode")
    public RestBean<String> genMailCode(@RequestParam @Email String email, 
                              HttpServletRequest request) {
        mailService.generateEmailVerifyCode(email, request.getRemoteAddr());
        return RestBean.success(null);
    }
    

    @PostMapping("/regist")
    public RestBean<String> fullRegist(@RequestBody @Valid EmailRegistVO emailRegistVO){
        String email = emailRegistVO.getEmail();
        String code = emailRegistVO.getCode();

        if(code == null)
            return RestBean.failure(400, "请填写验证码");
        if(accountService.exsitEmail(email))
            return RestBean.failure(400, "邮箱已经被注册");
        if(!mailService.checkEmailVerifyCode(email, code))
            return RestBean.failure(400, "验证码错误");
        
        mailService.delEmailVerifyCode(email);
        String username = manager.getNewUsername();
        Account newAccount = new Account()
                                    .setUsername(username)
                                    .setPassword(encoder.encode(emailRegistVO.getPassword()))
                                    .setEmail(email)
                                    .setEnabled(true);
        MyUser user = new MyUser(newAccount, null).addRole("USER");
        manager.createUser(user);
        return RestBean.success("你的用户id为"+username);
    }

    @GetMapping("/refresh")
    public RestBean<String> refresh(HttpServletRequest request, Authentication authentication){
        jwtService.invalidateJwt(request);
        return RestBean.success(jwtService.creatJwt(authentication));
    }


}

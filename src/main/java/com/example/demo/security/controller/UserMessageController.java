package com.example.demo.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.entity.dto.MyUser;
import com.example.demo.security.entity.vo.request.EmailMessageVO;
import com.example.demo.security.mapper.entity.Account;
import com.example.demo.security.service.MailService;
import com.example.demo.security.service.MyUserManager;
import com.example.demo.utils.RestBean;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/message")
@Tag(name = "用户消息通告接口", description = "邮件发送")
public class UserMessageController {
    @Autowired
    MailService mailService;

    @Autowired
    MyUserManager userManager;

    @PostMapping("/mail")
    public RestBean<String> postMethodName(@RequestBody EmailMessageVO messageVO,Authentication currentAuth) {
        if(messageVO.getContent() == null)
            return RestBean.failure(400,"消息内容不能为空");
        
        //获取接收方邮件
        String mail = messageVO.getReciever_email();
        Account currentUserAccount = ((MyUser)userManager.loadUserByUsername(currentAuth.getName())).getAccount();
        if(mail == null){
            String username = messageVO.getReciever_username();
            if(username == null)
                return RestBean.failure(400, "未指定接收人");
            mail = currentUserAccount.getEmail();
            if(mail == null)
                return RestBean.failure(550, "无法找到接收人的邮件");
        }
        
        //获取落款
        String sender = currentUserAccount.getNickname();
        if(sender == null)
            sender = "["+currentUserAccount.getUsername()+"]";
        
        String subject = "A message from "+sender;
        mailService.sendTextMail(mail, subject, messageVO.getContent());
        return RestBean.success("邮件发送成功");
    }
    
}

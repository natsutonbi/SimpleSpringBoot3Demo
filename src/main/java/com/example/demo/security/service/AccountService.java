package com.example.demo.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.security.mapper.AccountMapper;
import com.example.demo.security.mapper.entity.Account;
import com.example.demo.security.utils.FlowUtils;

@Service
public class AccountService extends ServiceImpl<AccountMapper, Account>{
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FlowUtils flow;

    public Account findAccountByEmail(String email){
        return this.query()
                .eq("email", email)
                .one();
    }

    public Account findAccountByUsername(String username){
        return this.query()
                .eq("username", username)
                .one();
    }

    public boolean exsitEmail(String email){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }
}

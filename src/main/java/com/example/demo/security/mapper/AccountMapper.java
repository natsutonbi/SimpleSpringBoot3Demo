package com.example.demo.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.security.mapper.entity.Account;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}

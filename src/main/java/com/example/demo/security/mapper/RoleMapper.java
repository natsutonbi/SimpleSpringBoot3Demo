package com.example.demo.security.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.security.mapper.entity.Role;

@Mapper
public  interface RoleMapper extends BaseMapper<Role>{
}

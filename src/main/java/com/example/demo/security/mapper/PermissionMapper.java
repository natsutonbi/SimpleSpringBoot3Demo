package com.example.demo.security.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.security.mapper.entity.Permission;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}

package com.example.demo.security.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
@TableName("role")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends Model<Role> {
    String username;
    String role;
}

package com.example.demo.security.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("account")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Account extends Model<Account>{
    @TableId
    String username;
    String nickname;
    String email;
    String phone;
    String password;
    boolean enabled;
}

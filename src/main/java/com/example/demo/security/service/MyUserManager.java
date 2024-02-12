package com.example.demo.security.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.demo.security.config.securityConfig;
import com.example.demo.security.entity.dto.MyUser;
import com.example.demo.security.mapper.PermissionMapper;
import com.example.demo.security.mapper.RoleMapper;
import com.example.demo.security.mapper.AccountMapper;
import com.example.demo.security.mapper.entity.Account;
import com.example.demo.security.mapper.entity.Role;
import com.example.demo.security.utils.SnowFlake;


import com.example.demo.security.mapper.entity.Permission;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyUserManager implements UserDetailsService {

    static final String rolePrefix = securityConfig.rolePrefix;

    @Autowired
    AccountMapper userMapper;

    @Autowired
    AccountService accountService;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    PasswordEncoder encoder;

    @Value("${snowflake.user.workId}")
    private static long workId;

    @Value("${snowflake.datacenterId}")
    private static long datacenterId;

    private static SnowFlake usernameGenerator = new SnowFlake(workId,datacenterId);

    private Collection<GrantedAuthority> loadAuthorities(String username){
        QueryWrapper<Permission> permQueryWrapper = new QueryWrapper<Permission>().eq("username", username).select("permission");
        Collection<GrantedAuthority> authorities = permissionMapper
                                                    .selectList(permQueryWrapper)
                                                    .stream()
                                                    .map(permission->{return new SimpleGrantedAuthority(permission.getPermission());})
                                                    .collect(Collectors.toCollection(HashSet::new));
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<Role>().eq("username", username).select("role");
        authorities.addAll(
            roleMapper.selectList(roleQueryWrapper)
            .stream()
            .map(role->{return new SimpleGrantedAuthority(role.getRole());})
            .collect(Collectors.toCollection(HashSet::new))
        );
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.findAccountByUsername(username);
        if(account == null)  throw new UsernameNotFoundException("用户不存在");
        return new MyUser(account,loadAuthorities(username));
    }

    public UserDetails loadUserByEmail(String email){
        Account account = accountService.findAccountByEmail(email);
        if(account == null)  throw new UsernameNotFoundException("无法找到邮箱对应的用户");
        return new MyUser(account,loadAuthorities(account.getUsername()));
    }

    public String getNewUsername() {
        return Long.toString(usernameGenerator.nextId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void createUser(MyUser myUser){
        Account account = myUser.getAccount();
        userMapper.insert(account);
        Collection<? extends GrantedAuthority> authorities = myUser.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        while (iter.hasNext()) {
            String auth = iter.next().getAuthority();
            if(auth.startsWith(rolePrefix)){
                roleMapper.insert(new Role(account.getUsername(),auth));
            }
            else{
                permissionMapper.insert(new Permission(account.getUsername(),auth));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUser(MyUser updateInfo) throws IllegalAccessException{ //查一遍后对比
        if(updateInfo == null) return;
        String username = updateInfo.getUsername();
        MyUser oldUser = (MyUser)loadUserByUsername(username);
        Account oldAccount = oldUser.getAccount(), newAccount = updateInfo.getAccount();
        if(newAccount != null){//MyUser的newAccount不可能为null
            newAccount.setPassword(oldAccount.getPassword()); //密码不能通过这种方法修改
            userMapper.updateById(newAccount);
        }
        
        Collection<GrantedAuthority> newAuthorities = updateInfo.getAuthorities(),oldAuthorities = oldUser.getAuthorities();
        if(newAuthorities == null) return;
        
        Collection<GrantedAuthority> common = new HashSet<>(newAuthorities);
        common.retainAll(oldAuthorities);
        Collection<GrantedAuthority> remove = oldAuthorities,add = newAuthorities;
        remove.removeAll(common);
        add.removeAll(common);
        
        for(GrantedAuthority del:remove) {
            String auth = del.getAuthority();
            if(auth.startsWith(rolePrefix)){
                QueryWrapper<Role> delRoleWrapper = new QueryWrapper<Role>().eq("username", username).eq("role",auth);
                roleMapper.delete(delRoleWrapper);
            }else{
                QueryWrapper<Permission> delRoleWrapper = new QueryWrapper<Permission>().eq("username", username).eq("permission",auth);
                permissionMapper.delete(delRoleWrapper);
            }
        }
        for(GrantedAuthority ins:add) {
            String auth = ins.getAuthority();
            if(auth.startsWith(rolePrefix)){
                Role role = new Role(username,auth);
                roleMapper.insert(role);
            }else{
                Permission permission = new Permission(username,auth);
                permissionMapper.insert(permission);
            }
        }
    }

    public void changePassword(String oldPassword, String newPassword){
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            throw new IllegalArgumentException("旧密码或新密码不能为空");
        }
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if(currentAuth == null|| !currentAuth.isAuthenticated())
            throw new IllegalStateException("用户未登录，无法修改密码");
        Account account = userMapper.selectById(currentAuth.getName());
        if(encoder.matches(oldPassword, account.getPassword())){
            account.setPassword(encoder.encode(newPassword));
            userMapper.updateById(account);
        }else throw new BadCredentialsException("旧密码验证失败");
    }

    public void deleteUser(String username){
        if(!userExists(username)) 
            throw new EmptyResultDataAccessException(1);
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        userMapper.delete(wrapper.eq("username", username));
    }

    public boolean userExists(String username) {
        Account account = userMapper.selectById(username);
        if(account == null){
            return false;
        }
        return true;
    }
    
}

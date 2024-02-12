package com.example.demo.security.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.stereotype.Service;

@Service
public class MyGroupManager implements GroupManager {

    @Override
    public void addGroupAuthority(String groupName, GrantedAuthority authority) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addGroupAuthority'");
    }

    @Override
    public void addUserToGroup(String username, String group) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUserToGroup'");
    }

    @Override
    public void createGroup(String groupName, List<GrantedAuthority> authorities) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createGroup'");
    }

    @Override
    public void deleteGroup(String groupName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteGroup'");
    }

    @Override
    public List<String> findAllGroups() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllGroups'");
    }

    @Override
    public List<GrantedAuthority> findGroupAuthorities(String groupName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findGroupAuthorities'");
    }

    @Override
    public List<String> findUsersInGroup(String groupName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUsersInGroup'");
    }

    @Override
    public void removeGroupAuthority(String groupName, GrantedAuthority authority) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeGroupAuthority'");
    }

    @Override
    public void removeUserFromGroup(String username, String groupName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeUserFromGroup'");
    }

    @Override
    public void renameGroup(String oldName, String newName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renameGroup'");
    }
    
}

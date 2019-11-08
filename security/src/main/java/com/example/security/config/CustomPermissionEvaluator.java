package com.example.security.config;

import com.example.security.dao.SysPermissionMapper;
import com.example.security.dao.SysRoleMapper;
import com.example.security.pojo.SysPermission;
import com.example.security.pojo.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object targetPermission) {
        User user= (User) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String roleName = authority.getAuthority();
            Integer roleId = roleMapper.selectByName(roleName).getId();
            List<SysPermission> permissionList = permissionMapper.listByRoleId(roleId);
            for (SysPermission permission : permissionList) {
                List permissions = permission.getPermissions();
                if(targetUrl.equals(permission.getUrl())&&permissions.contains(targetPermission)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}

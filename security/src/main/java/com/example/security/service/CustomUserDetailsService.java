package com.example.security.service;

import com.example.security.dao.SysRoleMapper;
import com.example.security.dao.SysUserMapper;
import com.example.security.dao.SysUserRoleMapper;
import com.example.security.pojo.SysRole;
import com.example.security.pojo.SysUser;
import com.example.security.pojo.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired(required = false)
    private SysUserMapper userMapper;

    @Autowired(required = false)
    private SysRoleMapper roleMapper;

    @Autowired(required = false)
    private SysUserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities=new ArrayList<>();
        SysUser user = userMapper.selectByName(username);
        // 判断用户是否存在
        if(user==null){
            System.out.println("用户不存在");
            throw new UsernameNotFoundException("用户不存在");
        }
        // 添加权限
        List<SysUserRole> userRoles = userRoleMapper.listByUserId(user.getId());
        for (SysUserRole userRole : userRoles) {
            SysRole role = roleMapper.selectById(userRole.getUserId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new User(username,user.getPassword(),authorities);
    }
}

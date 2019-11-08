package com.example.security.dao;

import com.example.security.pojo.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {
    @Select("select * from sys_user_role where user_id=#{user_id}")
    List<SysUserRole> listByUserId(int userId);
}

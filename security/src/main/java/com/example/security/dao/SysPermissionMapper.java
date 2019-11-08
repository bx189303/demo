package com.example.security.dao;

import com.example.security.pojo.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysPermissionMapper {

    @Select("select * from sys_permission where role_id=#{id}")
    List<SysPermission> listByRoleId(int id);
}

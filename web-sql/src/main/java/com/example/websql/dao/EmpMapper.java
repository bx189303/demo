package com.example.websql.dao;

import com.example.websql.bean.Emp;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpMapper {
    Emp getEmpById1(int id);

    Emp getEmpById2(int id);
}

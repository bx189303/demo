package com.example.websql.dao;

import com.example.websql.bean.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {

    int add(Student student);

    int delete(Student student);

    int update(Student student);

    Student query(int id);

    List<Student> queryAll();
}

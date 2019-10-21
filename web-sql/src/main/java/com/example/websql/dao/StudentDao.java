package com.example.websql.dao;

import com.example.websql.bean.Student;

import java.util.List;

public interface StudentDao {

    int add(Student student);

    int delete(Student student);

    int update(Student student);

    Student query(int id);

    List<Student> queryAll();
}

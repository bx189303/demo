package com.example.websql.dao.impl;

import com.example.websql.bean.Student;
import com.example.websql.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StudentDaoJDBCImpl implements StudentDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public int add(Student student) {
        String sql = "insert into student(id,name,gender,age) values(null,:name,:gender,:age)";
        Map<String, Object> param = new HashMap<>();
        param.put("name", student.getName());
        param.put("gender", student.getGender());
        param.put("age", student.getAge());
        return jdbcTemplate.update(sql, param);
    }

    @Override
    public int delete(Student student) {
        String sql = "delete from student where name= :name";
        Map<String, Object> param = new HashMap<>();
        param.put("name", student.getName());
        return jdbcTemplate.update(sql, param);
    }

    @Override
    public int update(Student student) {
        String sql = "update student set gender=:gender,age=:age where name=:name";
        Map<String, Object> param = new HashMap<>();
        param.put("name", student.getName());
        param.put("gender", student.getGender());
        param.put("age", student.getAge());
        return jdbcTemplate.update(sql, param);
    }

    @Override
    public Student query(int id) {
        String sql = "select * from student where id=:id";
        Map<String, Object> param = new HashMap<>();
        param.put("id",id);
        List<Student> students = jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<Student>(Student.class));
        return students.get(0);
    }

    @Override
    public List<Student> queryAll() {
        String sql="select * from student";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Student>(Student.class));
    }
}

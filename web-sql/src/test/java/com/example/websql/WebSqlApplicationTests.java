package com.example.websql;

import com.alibaba.fastjson.JSON;
import com.example.websql.bean.Student;
import com.example.websql.dao.StudentDao;
import com.example.websql.dao.StudentMapper;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Service
public class WebSqlApplicationTests {

    @Autowired
    private StudentDao studentDao;

    @Autowired(required = false)
    private StudentMapper studentMapper;

    @Test
    public void mybatisTest(){
        System.out.println(JSON.toJSONString(studentMapper.queryAll()));
        System.out.println(JSON.toJSONString(studentMapper.query(1)));
        //增加
        Student s=new Student("mybatis","man",77);
        System.out.println(studentMapper.add(s));
        //删除
        s.setName("mybatis");
//        System.out.println(studentMapper.delete(s));
        //更新
        s.setGender("woman");
        s.setAge(88);
        System.out.println(studentMapper.update(s));
    }


    @Test
    public void jdbcTest() {
//        jdbcAddTest();
//        jdbcDeleteTest();
//        jdbcUpdateTest();
        jdbcQueryTest();
        jdbcQueryAllTest();
    }

    private void jdbcDeleteTest(){
        Student s=new Student();
        s.setName("jdbc");
        System.out.println(studentDao.delete(s));
    }

    private void jdbcAddTest() {
        Student s=new Student();
        s.setName("jdbc");
        s.setGender("男");
        s.setAge(22);
        System.out.println(studentDao.add(s));
    }

    private void jdbcUpdateTest() {
        Student s=new Student();
        s.setName("jdbc");
        s.setGender("女");
        s.setAge(36);
        System.out.println(studentDao.update(s));
    }

    private void jdbcQueryTest(){
        System.out.println(JSON.toJSONString(studentDao.query(1)));
    }

    private void jdbcQueryAllTest(){
        System.out.println(JSON.toJSONString(studentDao.queryAll()));
    }

}

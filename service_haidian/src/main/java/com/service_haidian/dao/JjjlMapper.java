package com.service_haidian.dao;

import com.service_haidian.bean.JqJjjl;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JjjlMapper {

    List<JqJjjl> queryJjjlByDh(String dh);
}

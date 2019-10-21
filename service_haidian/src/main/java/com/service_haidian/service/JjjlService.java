package com.service_haidian.service;

import com.service_haidian.bean.JqJjjl;
import com.service_haidian.dao.JjjlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JjjlService {

    @Autowired
    JjjlMapper jjjlMapper;

    public List<JqJjjl> getJjjlByDh(String dh){
        return jjjlMapper.queryJjjlByDh(dh);
    };
}

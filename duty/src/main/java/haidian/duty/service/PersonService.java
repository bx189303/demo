package haidian.duty.service;

import haidian.duty.dao.PersonMapper;
import haidian.duty.pojo.Person;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PersonService {

    @Resource
    PersonMapper personMapper;

    public List<Person> getPersonByUnitAndNameOrNum(String unit,String input){
        input="%"+input+"%";
        List<Person> persons = personMapper.getPersonByUnitAndNameOrNum(unit, input);
        return persons;
    }

}

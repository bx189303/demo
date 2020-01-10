package haidian.duty.service;

import haidian.duty.dao.GroupMapper;
import haidian.duty.dao.PersonMapper;
import haidian.duty.pojo.Group;
import haidian.duty.pojo.Person;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Resource
    GroupMapper groupMapper;

    @Resource
    PersonMapper personMapper;

    public List<Group> getZhzByNum(String userId){
        List<Group> groupUsers=new ArrayList<>();
        List<Group> groupIds=groupMapper.getByUserId(userId);
        for (Group group : groupIds) {
            List<String> userIds = groupMapper.getUserByGroupId(group.getId());
            List<Person> persons = personMapper.getPersonByNums(userIds);
            group.setUsers(persons);
            groupUsers.add(group);
        }
        return groupUsers;
    }
}

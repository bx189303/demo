package haidian.duty.service;

import com.alibaba.fastjson.JSON;
import haidian.duty.config.ApplicationPre;
import haidian.duty.dao.PersonMapper;
import haidian.duty.dao.UnitMapper;
import haidian.duty.pojo.Person;
import haidian.duty.pojo.Unit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class UnitService {

    @Resource
    PersonMapper personMapper;

    @Resource
    UnitMapper unitMapper;

    public List<Unit> getUnits(String unitId){
        List<Unit> units=new ArrayList<>();
        //查自己的单位
        Unit myUnit = unitMapper.selectByPrimaryKey(unitId);
        units.add(myUnit);
        //递归查父级
        getParents(units,myUnit);
        //递归查子级
        getChilds(units,myUnit);
        return units;
    }

    public void getParents(List<Unit> units,Unit unit){
        Unit parentUnit = unitMapper.selectByPrimaryKey(unit.getsParent());
        if(parentUnit==null){
            return;
        }
        units.add(parentUnit);
        getParents(units,parentUnit);
    }
    public void getChilds(List<Unit> units,Unit unit){
        List<Unit> childUnits = unitMapper.selectByParentId(unit.getsId());
        if(childUnits.size()==0||childUnits==null){
            return;
        }
        units.addAll(childUnits);
        for (Unit childUnit : childUnits) {
            getChilds(units,childUnit);
        }
    }


    public List<Unit> getUnitUsers() {
        List<Unit> units= getUnits("110108000000");
        List<Person> allps = personMapper.getAll();
        for (Person p : allps) {
            if("110108000000".equals(p.getsUnit())){
               continue;
            }
            Unit u=new Unit();
            u.setsId(p.getsPolicenum());
            u.setsName(p.getsName());
            u.setsParent(p.getsUnit());
            units.add(u);
        }
        return units;
    }

    public void getChildsAndUser(List<Unit> units,Unit unit){
        //添加根节点机构的人员
        if(!"110108000000".equals(unit.getsId())){
            List<Person> users = personMapper.getPersonByUnitId(unit.getsId());
            for (Person user : users) {
                Unit u=new Unit();
                u.setsParent(unit.getsId());
                u.setsId(user.getsPolicenum());
                u.setsName(user.getsName());
                units.add(u);
            }
        }
        //查询子机构，如果无则返回
        List<Unit> childUnits = unitMapper.selectByParentId(unit.getsId());
        if(childUnits.size()==0||childUnits==null){
            return;
        }
        units.addAll(childUnits);
        for (Unit childUnit : childUnits) {
            getChildsAndUser(units,childUnit);
        }
    }
}

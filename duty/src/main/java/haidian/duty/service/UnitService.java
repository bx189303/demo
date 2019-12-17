package haidian.duty.service;

import haidian.duty.dao.UnitMapper;
import haidian.duty.pojo.Unit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnitService {

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
}

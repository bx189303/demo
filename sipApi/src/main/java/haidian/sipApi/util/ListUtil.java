package haidian.sipApi.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ListUtil {

    public static List noDuplicate(List list){
        LinkedHashSet s=new LinkedHashSet(list);
        List ll=new ArrayList(s);
        return new ArrayList(s);
    }

}

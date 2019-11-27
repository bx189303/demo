package haidian.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class JavaTest {
    public static void main(String[] args) {
        List<Date> l=new ArrayList<>();
        l.add(DateUtil.getDateTime("2010-11-27 16:21:32"));
        l.add(DateUtil.getDateTime("2012-11-27 16:21:32"));
        l.add(DateUtil.getDateTime("2012-12-17 16:21:32"));
        l.add(DateUtil.getDateTime("2012-12-27 16:21:32"));
        l.add(DateUtil.getDateTime("2012-12-27 18:21:32"));
        l.add(DateUtil.getDateTime("2012-12-27 18:25:32"));
        l.add(DateUtil.getDateTime("2012-12-27 18:25:52"));
        System.out.println(JSON.toJSONString(l));
        Collections.sort(l, new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                if (o1.getTime() > o2.getTime()) {
                    return -1;
                } else if (o1.getTime() < o2.getTime()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        System.out.println(JSON.toJSONString(l));


    }

    private static void jsonListTest() {
        List<JSONObject> l=new ArrayList<>();
        JSONObject j=new JSONObject();
        j.put("id",1);
        l.add(j);
        System.out.println(JSON.toJSONString(l));
        j.put("id",2);
        l.add(j);
        System.out.println(JSON.toJSONString(l));
    }

    private static void jsontest2() {
        String j="{\n" +
                "  \"type\": \"msg\",\n" +
                "  \"data\": {\n" +
                "    \"type\": \"group/single\",\n" +
                "    \"src\": \"A\",\n" +
                "    \"dst\": \"G2\",\n" +
                "    \"sendTime\": \"消息发送时间\",\n" +
                "    \"content\": {\n" +
                "      \"type\": \"file\",\n" +
                "      \"content\": {\n" +
                "        \"type\": \"video\",\n" +
                "        \"size\": 512 ,\n" +
                "        \"duration\": 10000,\n" +
                "        \"uuid\": \"3_9782\",\n" +
                "        \"time\": \"文件上传成功时间\",\n" +
                "        \"content\": \"/user/local/3_9782\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        String data= JSON.parseObject(j).getString("data");
        System.out.println(JSON.parseObject(JSON.parseObject(j).getString("data")));
    }

    private static void listTest() {
        List<String> list=new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println(JSON.toJSONString(list));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("b")){
                list.set(i,"q");
            }
        }
        System.out.println(JSON.toJSONString(list));
    }

    private static void jsontest() {
        JSONObject j=new JSONObject();
        j.put("name","qwe");
        System.out.println(JSON.toJSONString(j));
        j.put("name","asdf");
        System.out.println(JSON.toJSONString(j));
    }


    private static void datetest() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        System.out.println("格式化后的日期：" + dateNowStr);
    }
}

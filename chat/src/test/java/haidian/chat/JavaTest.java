package haidian.chat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import haidian.chat.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.*;

public class JavaTest {
    public static void main(String[] args) {
        String s="qwe.zxc";
        System.out.println(JSON.toJSONString(s.split("\\.")));
    }

    private static void jiaojitest() {
        List<String> l=new ArrayList<>();
        l.add("1");
        l.add("2");
        l.add("3");
        l.add("4");
        l.add("5");
        List<String> ll=new ArrayList<>();
        ll.add("6");
        ll.add("7");
        ll.add("3");
        ll.add("4");
        ll.add("5");
        l.retainAll(ll);
        System.out.println(JSON.toJSON(l));
    }

    private static void subListtest() {
        List<String> l=new ArrayList<>();
        l.add("1");
        l.add("2");
        l.add("3");
        l.add("4");
        l.add("5");
        l.add("6");
        l.add("7");
        List<String> ll = l.subList(0, 5);
        System.out.println(JSON.toJSONString(ll));
        List<String> rl = l.subList(l.size() - 5, l.size());
        System.out.println(JSON.toJSONString(rl));
    }

    private static void listeachcompare() {
        List<String> l=new ArrayList<>();
        for (int i = 0; i <1000 ; i++) {
            l.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }
        List<String> ll=new ArrayList<>();
        for (int i = 0; i <1000 ; i++) {
            ll.addAll(l);
        }
        long t1= System.currentTimeMillis();
        for (int i = 0; i <1000 ; i++) {
            l.get(i);
        }
        long t2= System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ll.get(i);
        }
        long t3=System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            ll.get(i);
        }
        long t4=System.currentTimeMillis();
        for (String s : l) {

        }
        long t5=System.currentTimeMillis();
        System.out.println("1000长度增强for循环全部遍历："+new Double(t5-t4)/1000+"s");
        System.out.println("1000长度普通循环全部遍历："+new Double(t2-t1)/1000+"s");
        System.out.println("1000000长度普通循环遍历1000次："+new Double(t3-t2)/1000+"s");
        System.out.println("1000000长度普通循环全部遍历："+new Double(t4-t3)/1000+"s");
    }

    private static void jsonarraytest() {
        JSONObject j=new JSONObject();
        j.put("l","[]");
        JSONArray l = j.getJSONArray("l");
        l.add("001");
        System.out.println(l.get(0));
    }

    private static void comparetest() {
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

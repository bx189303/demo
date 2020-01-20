package haidian.chat.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

public class httpUtil {

    public static String sendGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static Response sendPostRequest(String url, Map<String, Object> params){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以什么方式提交，自行选择，一般使用json，或者表单
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        //将请求头部和参数合成一个请求
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用Response类格式化
        ResponseEntity<Response> response = client.exchange(url, method, requestEntity, Response.class);
        return response.getBody();
    }

    public static void main(String[] args) throws IOException {
//        String url="http://localhost:8080/updateGroup";
//        Map<String,Object> map=new HashMap<>();
//        map.put("groupId","429e56aa-09bc-46c5-83aa-6a64290336b7");
//        map.put("groupName","http测试");
//        System.out.println(JSON.toJSONString(sendPostRequest(url,map)));
        String url="http://localhost:23269/download/1b353d3b-819b-4cdd-9e99-48bc0d110275.txt/test2.txt";
        sendGetRequest(url);
    }
}

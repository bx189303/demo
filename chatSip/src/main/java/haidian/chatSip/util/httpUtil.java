package haidian.chatSip.util;

import okhttp3.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

public class httpUtil {

    public static String zxUploadFile() throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("yyid", "A00111010810000002901")
                .addFormDataPart("scrsfzh", "130526199502025218")
                .addFormDataPart("scrxm", "边祥")
                .addFormDataPart("scrjg", "海淀分局")
                .addFormDataPart("files","/C:/Users/bx/Desktop/centos7 jdk.txt",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("/C:/Users/bx/Desktop/centos7 jdk.txt")))
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:23269/upload")
                .method("POST", body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static Response sendPostRequest(String url, Map<String, Object> params){
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以什么方式提交，自行选择，一般使用json，或者表单
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8);
        //将请求头部和参数合成一个请求
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用Response类格式化
        ResponseEntity<Response> response = client.exchange(url, method, requestEntity, Response.class);
        return response.getBody();
    }

    public static void main(String[] args) throws Exception {
//        String url="http://localhost:8080/updateGroup";
//        Map<String,Object> map=new HashMap<>();
//        map.put("groupId","429e56aa-09bc-46c5-83aa-6a64290336b7");
//        map.put("groupName","http测试");
//        System.out.println(JSON.toJSONString(sendPostRequest(url,map)));
        String url="http://localhost:23269/download/1b353d3b-819b-4cdd-9e99-48bc0d110275.txt/test2.txt";
//        url="http://localhost:23269/getUserByUserId/0001";

        String p=zxUploadFile();
        System.out.println(p);
    }
}

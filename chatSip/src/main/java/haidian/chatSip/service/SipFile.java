package haidian.chatSip.service;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SipFile {

    @Value("${filePath}")
    String filePath;

    @Value("${zxUploadFileUrl}")
    String zxUploadFileUrl;

    @Value("${zxUploadFileYyid}")
    String zxUploadFileYyid;

    @Value("${zxUploadFileXzrsfzh}")
    String zxUploadFileXzrsfzh;

    @Value("${zxUploadFileXzrxm}")
    String zxUploadFileXzrxm;

    @Value("${zxUploadFileXzrjg}")
    String zxUploadFileXzrjg;

    public String zxUploadFile(String fileName) throws Exception {
        String uploadFilePath=filePath+"/"+fileName;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("yyid", zxUploadFileYyid)
                .addFormDataPart("scrsfzh", zxUploadFileXzrsfzh)
                .addFormDataPart("scrxm", zxUploadFileXzrxm)
                .addFormDataPart("scrjg", zxUploadFileXzrjg)
                .addFormDataPart("fileName",uploadFilePath,
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(uploadFilePath)))
                .build();
        Request request = new Request.Builder()
                .url(zxUploadFileUrl)
                .method("POST", body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();

        String res=response.body().string();
        String resJsonStr=(String)JSON.parseArray(res).get(0);
        String uploadPath=JSON.parseObject(resJsonStr).getString("path");
        return uploadFilePath;
    }

}

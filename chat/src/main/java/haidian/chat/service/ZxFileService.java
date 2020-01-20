package haidian.chat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ZxFileService {

    @Value("${filePath}")
    String filePath;

    @Value("${zxdownloadFileUrl}")
    String zxdownloadFileUrl;

    @Value("${zxdownloadFileYyid}")
    String zxdownloadFileYyid;

    @Value("${zxdownloadFileXzrsfzh}")
    String zxdownloadFileXzrsfzh;

    @Value("${zxdownloadFileXzrxm}")
    String zxdownloadFileXzrxm;

    @Value("${zxdownloadFileXzrjg}")
    String zxdownloadFileXzrjg;

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

    //向总线上传文件
    public JSONObject zxUploadFile(JSONObject data) throws Exception {
        if(StringUtils.isEmpty(zxUploadFileUrl)){
            return data;
        }
        String msgContentType=data.getJSONObject("content").getString("type");
        if("file".equals(msgContentType)){
            String msgFilePath=data.getJSONObject("content").getJSONObject("content").getString("content");
            String zxPath=zxUploadFilePost(msgFilePath);
            data.getJSONObject("content").getJSONObject("content").put("zxPath",zxPath);
        }
        return data;
    }

    //总线上传文件请求
    public String zxUploadFilePost(String fileName) throws Exception {
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
        JSONObject resJson=(JSONObject) JSON.parseArray(res).get(0);
        String uploadPath=resJson.getString("path");
        return uploadPath;
    }

    //从总线下载文件到服务器上
    public void zxDownloadFile(JSONObject data) throws Exception {
        String msgContentType=data.getJSONObject("content").getString("type");
        if("file".equals(msgContentType)){
            String msgFilePath=data.getJSONObject("content").getJSONObject("content").getString("content");
            //如果文件路径为group开头，则来自二类网
            if(msgFilePath.startsWith("group")){
//                System.out.println("开始处理二类网文件");
                //二类网文件路径为多级，下载时判断文件目录是否存在
                String downloadPath=filePath+msgFilePath;
                String downloadDir=downloadPath.substring(0,downloadPath.lastIndexOf("/"));
                File downloadDirFile=new File(downloadDir);
                if(!downloadDirFile.exists()){
                    downloadDirFile.mkdirs();
                }
                //下载文件到nginx的路径下
                String url=zxdownloadFileUrl+"?yyid="+zxdownloadFileYyid+"&downloadPath="+msgFilePath+"&xzrsfzh"+zxdownloadFileXzrsfzh+"&xzrxm="+zxdownloadFileXzrxm+"&xzrjg="+zxdownloadFileXzrjg;
                String downloadcmd="curl -o "+downloadPath+" \""+url+"\"";
                centosCmd(downloadcmd);
//                System.out.println("下载完成");
            }
        }
    }

    public void centosCmd(String cmd) throws Exception {
        String[] cmds = { "/bin/sh", "-c", cmd};
        Process pro = Runtime.getRuntime().exec(cmds);
        pro.waitFor();
    }
}

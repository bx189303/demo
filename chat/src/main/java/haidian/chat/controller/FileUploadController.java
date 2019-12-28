package haidian.chat.controller;

import com.alibaba.fastjson.JSONObject;
import haidian.chat.util.Result;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class FileUploadController {

    @Value("${filePath}")
    String filePath;

    @RequestMapping(value = "/upload", consumes = "multipart/form-data", method = RequestMethod.POST)
    public Result upload(HttpServletRequest request) {
        try {
            String type="file";
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
            if (files == null || files.size() == 0) {
                return Result.build(500,"上传文件为空！");
            }
            String uuid= UUID.randomUUID()+"";
            String fileSaveName="";
            for (MultipartFile file : files) {
                //保存文件到指定目录下
//                File dest = new File("file/" + file.getOriginalFilename());
                String fileName=file.getOriginalFilename();
                //if(suffix=="BMP"||suffix=="JPG"||suffix=="JPEG"||suffix=="PNG"||suffix=="GIF"){
                String fileSuffix=fileName.substring(fileName.lastIndexOf(".")+1);
                if("BMP".equalsIgnoreCase(fileSuffix)||"JPG".equalsIgnoreCase(fileSuffix)||"JPEG".equalsIgnoreCase(fileSuffix)||"PNG".equalsIgnoreCase(fileSuffix)||"GIF".equalsIgnoreCase(fileSuffix)){
                    type="img";
                }
                fileSaveName=uuid+"."+fileSuffix;
                File dest = new File(filePath + fileSaveName);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdir();//如果路径不存在，需要提前创建，否则异常
                }
                FileOutputStream fos = new FileOutputStream(dest, false);
                //注意这里引用的包是org.apache.commons.io.IOUtils
                IOUtils.copy(file.getInputStream(), fos);
                fos.close();
                System.out.println(file.getOriginalFilename() + " ---->>>> " + dest.getAbsolutePath());
//                System.out.println(request.getParameter("name"));
            }
            JSONObject json=new JSONObject();
            json.put("uuid",fileSaveName);
            json.put("type",type);
            return Result.build(200,"file upload success",json) ;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.build(500,ex.getMessage());
        }
    }

    @RequestMapping(value = "download/{fileUrl}/{fileName}", produces = "application/json;charset=UTF-8")
    public void downloadFile( HttpServletResponse response, @PathVariable String fileUrl, @PathVariable String fileName){
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            String encodeName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            fin = new FileInputStream(new File(filePath+fileUrl));
            BufferedInputStream bis = new BufferedInputStream(fin);
            out = response.getOutputStream();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");//设置允许跨域的key
            response.setHeader("Content-Disposition", "attachment;fileName=" + encodeName);
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                out.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            out.flush();
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fin != null) fin.close();
                if(out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/test")
    public String test() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //输出request.getServerPort()的目的是用来测试zuul的负载的
        return "Test success,port:" + request.getServerPort() + ".Time:" + new Date().toString();
    }


}

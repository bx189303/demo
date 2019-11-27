package haidian.chat.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

@RestController
public class FileUploadController {

    @RequestMapping(value = "/upload", consumes = "multipart/form-data", method = RequestMethod.POST)
    public String upload(HttpServletRequest request) {
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
            if (files == null || files.size() == 0) {
                return "上传文件为空！";
            }
            for (MultipartFile file : files) {
                //保存文件到指定目录下
                File dest = new File("file/" + file.getOriginalFilename());
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdir();//如果路径不存在，需要提前创建，否则异常
                }
                FileOutputStream fos = new FileOutputStream(dest, false);
                //注意这里引用的包是org.apache.commons.io.IOUtils
                IOUtils.copy(file.getInputStream(), fos);
                fos.close();
                System.out.println(file.getOriginalFilename() + " ---->>>> " + dest.getAbsolutePath());
                System.out.println(request.getParameter("name"));
            }
            return "file upload success";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "file upload fail:" + ex.getMessage();
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

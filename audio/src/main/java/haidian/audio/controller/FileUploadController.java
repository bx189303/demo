package haidian.audio.controller;

import haidian.audio.config.ApplicationPre;
import haidian.audio.pojo.po.Person;
import haidian.audio.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class FileUploadController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 对外暴露方法
     */
    @RequestMapping(value = "audioFileDownload", produces = "application/json;charset=UTF-8")
    public void DownloadNetAudio( HttpServletResponse response,HttpServletRequest request){
        try {
            //处理参数-文件名
            String fileUrlAndName=request.getParameter("fileUrlAndName");
            if(fileUrlAndName.indexOf(";")!=-1){
                downloadZipNetFile(response,request);
            }else{
                downloadSingleNetFile(response, request);
            }
        } catch (Exception e) {
            log.info("下载文件异常 ： "+ e.getMessage());
            e.printStackTrace();
        }
    }

    //批量下载
    public void downloadZipNetFile(HttpServletResponse response,HttpServletRequest request) {
        try {
            String fileUrlAndName=request.getParameter("fileUrlAndName");
            Map<String, byte[]> files=new HashMap<>();
            String[] fileUrlAndNameArray = fileUrlAndName.split(";");
            for (String file : fileUrlAndNameArray) {
                String[] fileArray = file.split(",");
                String fileUrl=fileArray[0];
                String fileName=fileArray[1];
                URL url =new URL(fileUrl); // 创建URL
                URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
                urlconn.connect();
                HttpURLConnection httpconn =(HttpURLConnection)urlconn;
                int HttpResult = httpconn.getResponseCode();
                if(HttpResult != HttpURLConnection.HTTP_OK) {
                    log.info("下载网络文件时，无法连接到 "+fileUrl);
                }
                urlconn.getInputStream();
                InputStream is = urlconn.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = is.read(buff)) != -1) {
                    os.write(buff, 0, len);
                }
                byte[] bytes = os.toByteArray();
                fileName=getName(fileName.substring(0,fileName.indexOf("_")))+ fileName.substring(fileName.indexOf("_"));
                files.put(fileName,bytes);
            }
            String userId=fileUrlAndNameArray[0].split(",")[1].split("_")[0];
            String userName=getName(userId);
            String zipName= userName+"_"+DateUtil.getDateToGaplessString(new Date())+"_"+fileUrlAndNameArray.length;
            response.reset();
            zipName = URLEncoder.encode(zipName, StandardCharsets.UTF_8.toString());
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + zipName + ".zip");
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            BufferedOutputStream bos = new BufferedOutputStream(zos);
            for(Map.Entry<String, byte[]> entry : files.entrySet()){
                String fileName = entry.getKey();            //每个zip文件名
                byte[]    file = entry.getValue();            //这个zip文件的字节
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(file));
                zos.putNextEntry(new ZipEntry(fileName));
                int len = 0;
                byte[] buf = new byte[10 * 1024];
                while( (len=bis.read(buf, 0, buf.length)) != -1){
                    bos.write(buf, 0, len);
                }
                bis.close();
                bos.flush();
            }
            bos.close();
        } catch (Exception ex) {
            log.info("批量下载文件异常 ： "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

    //单个下载
    public void downloadSingleNetFile( HttpServletResponse response,HttpServletRequest request){
        //处理参数-文件名
        String fileUrlAndName=request.getParameter("fileUrlAndName");
        String[] fileUrlAndNameArray = fileUrlAndName.split(",");
        String fileUrl=fileUrlAndNameArray[0];
        String fileName=fileUrlAndNameArray[1];
//        System.out.println(fileUrl+"  "+fileName);
        String[] fileNameArray = fileName.split("\\.");
        String fileSuffix=fileNameArray[1];
        String fileNameOld=fileNameArray[0];
        String[] fileNameOldArray = fileNameOld.split("_");
        String userId=fileNameOldArray[0];
        String userName=getName(userId);
        String callId=fileNameOldArray[1];
        String callDate=fileNameOldArray[2];
        callDate=callDate.replace(" ","").replace("-","").replace(":","");
        String fileNameNew=userName+"_"+callId+"_"+callDate+"."+fileSuffix;
//        System.out.println("文件名改为："+fileNameNew);
        //下载文件
        int HttpResult; // 服务器返回的状态
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            String encodeName = URLEncoder.encode(fileNameNew, StandardCharsets.UTF_8.toString());
            URL url =new URL(fileUrl); // 创建URL
            URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
            urlconn.connect();
            HttpURLConnection httpconn =(HttpURLConnection)urlconn;
            HttpResult = httpconn.getResponseCode();
            if(HttpResult != HttpURLConnection.HTTP_OK) {
                log.info("下载网络文件时，无法连接到 "+fileUrl);
                return;
            }
//            int filesize = urlconn.getContentLength(); // 取数据长度
//            System.out.println("取数据长度===="+filesize);
            urlconn.getInputStream();
            fin = urlconn.getInputStream();
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
            log.info("下载文件异常 ： "+ e.getMessage());
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

    public String getName(String idOrTel){
        Person user=ApplicationPre.personMap.get(idOrTel);
        String userName=user==null?idOrTel:user.getsName();
        return userName;
    }

    @RequestMapping(value = "/zipFiles")
    public void zipFiles(HttpServletResponse response,HttpServletRequest request) {
        try {
            String fileUrlAndName=request.getParameter("fileUrlAndName");
            Map<String, byte[]> files=new HashMap<>();
            String[] fileUrlAndNameArray = fileUrlAndName.split(";");
            for (String file : fileUrlAndNameArray) {
                String[] fileArray = file.split(",");
                String fileUrl=fileArray[0];
                String fileName=fileArray[1];
                URL url =new URL(fileUrl); // 创建URL
                URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
                urlconn.connect();
                HttpURLConnection httpconn =(HttpURLConnection)urlconn;
                int HttpResult = httpconn.getResponseCode();
                if(HttpResult != HttpURLConnection.HTTP_OK) {
                    log.info("下载网络文件时，无法连接到 "+fileUrl);
                }
                urlconn.getInputStream();
                InputStream is = urlconn.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = is.read(buff)) != -1) {
                    os.write(buff, 0, len);
                }
                byte[] bytes = os.toByteArray();
                files.put(fileName,bytes);
            }
            String userId=fileUrlAndNameArray[0].split(",")[1].split("_")[0];
            Person user=ApplicationPre.personMap.get(userId);
            String userName=user==null?userId:user.getsName();
            String zipName= userName+"_"+DateUtil.getDateToGaplessString(new Date())+"_"+fileUrlAndNameArray.length;
            response.reset();
            zipName = URLEncoder.encode(zipName, StandardCharsets.UTF_8.toString());
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + zipName + ".zip");
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            BufferedOutputStream bos = new BufferedOutputStream(zos);
            for(Map.Entry<String, byte[]> entry : files.entrySet()){
                String fileName = entry.getKey();            //每个zip文件名
                byte[] file = entry.getValue();            //这个zip文件的字节
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(file));
                zos.putNextEntry(new ZipEntry(fileName));
                int len = 0;
                byte[] buf = new byte[10 * 1024];
                while( (len=bis.read(buf, 0, buf.length)) != -1){
                    bos.write(buf, 0, len);
                }
                bis.close();
                bos.flush();
            }
            bos.close();
        } catch (Exception ex) {
            log.info("批量下载文件异常 ： "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

}

package com.example.springboot_demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    public static Logger logger = LoggerFactory.getLogger(Logger.class);

    @RequestMapping("/testMethod")
    public Map<String,Object> testMethod(){
        System.out.println("this is a test method.");

        Map<String,Object> retData = new HashMap<>();
        retData.put("status","200");
        retData.put("methodName", "testMethod");
        return retData;
    }

    @RequestMapping(value = "/upload",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> uploadFile(@RequestParam("file") MultipartFile file){
        Map<String,Object> resultData = new HashMap<>();
        String msg = "";
        try {
            if(file.isEmpty()){
                msg = "上传文件为空.";
                logger.info(msg);
                return resultData;
            }else {
                String filename = file.getOriginalFilename();
                logger.info("上传文件名:{}",filename);
                String filenameSuffix = filename.substring(filename.lastIndexOf("."));
                logger.info("文件名后缀:{}",filenameSuffix);

                //存储文件
                String savePath = "E://demofile//";
                String filePath = savePath+filename;

                File tempFile = new File(filePath);
                //检测目录是否存在
                if (!tempFile.exists()){
                    tempFile.getParentFile().mkdirs();//新建文件
                }
                InputStream is = file.getInputStream();
                OutputStream os = new FileOutputStream(tempFile);
                byte[] bt = new byte[1024];
                Long start = System.currentTimeMillis();
                while (is.read(bt)!=-1){
                    os.write(bt);
                }
                Long end = System.currentTimeMillis();
                System.out.println(end-start);
                os.flush();
                is.close();
                os.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        resultData.put("msg",msg);
        return resultData;
    }

}

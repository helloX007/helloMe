package com.example.springboot_demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Controller
@RequestMapping("/file")
public class FileController {

    public static Logger logger = LoggerFactory.getLogger(Logger.class);

    //存储路径
    private static String saveDir = "E://demofile//";

    @ResponseBody
    @RequestMapping(value = "/upload",method = {RequestMethod.GET,RequestMethod.POST})
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
                String filePath = saveDir+filename;

                File tempFile = new File(filePath);
                //检测目录是否存在
                if (!tempFile.exists()){
                    tempFile.getParentFile().mkdirs();//新建文件
                }
                InputStream is = file.getInputStream();
                OutputStream os = new FileOutputStream(tempFile);
                byte[] bt = new byte[1024];
                int length = 0;
                while ((length = is.read(bt))!=-1){
                    os.write(bt,0,length); // 读了多少个字节就写多少字节,不能每次都写入byte数组大小那么多的字节,如下
//                    os.write(bt);  这样写会导致byte中的空字符也写进文件中,
                }
                os.flush();
                is.close();
                os.close();
                msg = "上传成功.";
            }
        }catch (Exception e){
            msg = "上传失败.";
            e.printStackTrace();
        }
        resultData.put("msg",msg);
        return resultData;
    }

    // 如果本地存在同名同类型文件,会被覆盖
    @RequestMapping("/uploadBatchFile")
    @ResponseBody
    public Map<String,Object> uploadBatch(@RequestParam("files") LinkedList<MultipartFile> files){
        Map<String,Object> resultData = new HashMap<>();
        String flag = "-1";
        String error = "";
        int count=0;
        MultipartFile file = null;
        BufferedOutputStream bos = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()){
                try {
                    String originalFilename = file.getOriginalFilename();
                    logger.info("文件名:{}",originalFilename);
                    bos = new BufferedOutputStream(new FileOutputStream(new File(saveDir+originalFilename)));
                    byte[] bytes = file.getBytes();
                    bos.write(bytes);
                    bos.close();
                    count++;
                    logger.info("文件:{} 上传完成.",originalFilename);
                } catch (FileNotFoundException e) {
                    error = "文件不存在.";
                    e.printStackTrace();
                } catch (IOException e) {
                    error = "文件没有上传完成.";
                    e.printStackTrace();
                }finally {
                    try {
                        if (bos!=null){
                            bos.close();
                        }
                    } catch (IOException e) {
                        bos = null;
                        e.printStackTrace();
                    }
                }
            }else {
                error = "文件是空的.";
            }
        }
        if (count==files.size()){
            flag = "1";
        }
        resultData.put("flag",flag);
        resultData.put("error",error);
        return resultData;
    }



}

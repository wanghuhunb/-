package com.itheima.controller;

import com.itheima.entity.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private HttpServletResponse response;
    /**
     * 文件上传功能
     * @param file
     * @return
     */
    private String imgspath="D:\\imgs\\";
    @PostMapping("/upload")
    public R upload(MultipartFile file) throws IOException {
        //获取文件名字
        String imgs = file.getOriginalFilename();
        //重新给文件起名字
        String suffis = imgs.substring(imgs.lastIndexOf("."));
        String replace = UUID.randomUUID().toString().replace("-", "");
        String fileName=replace+suffis;
        File file1 =new File(imgspath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        file.transferTo(new File(imgspath+fileName));
        return R.success(fileName);
    }

    @GetMapping("download")
    public void download(String name) throws IOException {
        //找到文件
        File file = new File(imgspath+name);
        //读取文件
        FileInputStream fileInputStream = new FileInputStream(file);
        ServletOutputStream outputStream = response.getOutputStream();
        //然后再输出
        byte [] bytes =new byte[1024];

        while (true){
            int read = fileInputStream.read(bytes);
            if(read==-1){
                break;
            }
            outputStream.write(bytes,0,read);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
    }
}

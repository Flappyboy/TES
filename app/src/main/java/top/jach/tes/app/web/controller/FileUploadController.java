package top.jach.tes.app.web.controller;

import org.n3r.idworker.Sid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.jach.tes.app.web.utils.DownloadFileUtil;
import top.jach.tes.app.web.utils.FileUtil;
import top.jach.tes.plugin.tes.utils.FileCompress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;


import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

@CrossOrigin
//@RestController
@RequestMapping(value = "/api")
@Controller
public class FileUploadController {

    public static final String path = "E:/tmp/tes/file";

    //处理文件上传
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity uploadImg(@RequestParam("file") MultipartFile file,
                                    HttpServletRequest request) throws UnsupportedEncodingException {
        String fileName = file.getOriginalFilename();  //图片名字

//        String filePath = "/Users/yaya/Desktop/upload/";
        String filePath = path+"/upload/"+ Sid.nextShort()+"/";
        System.out.println("======:   "+path);

        //调用文件处理类FileUtil，处理文件，将文件写入指定位置
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            // TODO: handle exception
        }

        Map<String, String> result = new HashMap<String, String>();
        result.put("path", URLEncoder.encode(filePath + fileName, "utf-8"));
        // 返回图片的存放路径
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/download")
    public ResponseEntity downloadFile(String path, HttpServletResponse response) throws UnsupportedEncodingException {
        DownloadFileUtil.download(URLDecoder.decode(FileUploadController.path+path, "utf-8"), response);
        return ResponseEntity.ok().build();
    }
}

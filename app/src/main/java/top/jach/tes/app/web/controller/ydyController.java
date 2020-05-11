package top.jach.tes.app.web.controller;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.jach.tes.app.web.entity.DataSetEntity;
import top.jach.tes.app.web.entity.TaskEntity;
import top.jach.tes.app.web.repository.DataSetEntityRepository;
import top.jach.tes.app.web.utils.FileUtil;
import top.jach.tes.core.api.domain.Task;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/ydy")
public class ydyController {
    @Autowired
    private DataSetEntityRepository dataSetEntityRepository;

    @RequestMapping(value = "/dataset", method = RequestMethod.POST)
    public ResponseEntity Create(DataSetEntity dataset) {
        dataSetEntityRepository.save(dataset);
     return ResponseEntity.ok(dataset);
    }

    @RequestMapping(value = "/dataset", method = RequestMethod.GET)
    public ResponseEntity Read() {
        List<DataSetEntity> list = dataSetEntityRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @RequestMapping(value = "/calculate/{datasetId}", method = RequestMethod.GET)
    public ResponseEntity calculate(@PathVariable Long datasetId) {
        DataSetEntity dataSetEntity = new DataSetEntity();
        dataSetEntity.setId(datasetId);
        dataSetEntity = dataSetEntityRepository.findOne(Example.of(dataSetEntity)).get();
        String path = dataSetEntity.getDatapath();
        return ResponseEntity.ok().build();
    }

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
}

package top.jach.tes.app.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
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
import top.jach.tes.plugin.jhkt.metrics.Metrics;
import top.jach.tes.plugin.jhkt.metrics.MetricsInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController()
@RequestMapping("/api/ydy")
public class ydyController {
    @Autowired
    private DataSetEntityRepository dataSetEntityRepository;

    @RequestMapping(value = "/dataset", method = RequestMethod.POST)
    public ResponseEntity Create(DataSetEntity dataset) throws UnsupportedEncodingException {
        dataset.setDatapath(URLDecoder.decode(dataset.getDatapath(), "utf-8"));
        dataSetEntityRepository.save(dataset);
     return ResponseEntity.ok(dataset);
    }

    @RequestMapping(value = "/dataset/delete", method = RequestMethod.POST)
    public ResponseEntity Delete(@RequestBody List<Long> datasetIds) {
        for (Long datasetId :
                datasetIds) {
            dataSetEntityRepository.deleteById(datasetId);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/dataset", method = RequestMethod.GET)
    public ResponseEntity Read() {
        List<DataSetEntity> list = dataSetEntityRepository.findAll();
        return ResponseEntity.ok(list);
    }

    @RequestMapping(value = "/calculate/{datasetId}", method = RequestMethod.GET)

    public ResponseEntity calculate(@PathVariable Long datasetId) throws IOException, IOException {

        DataSetEntity dataSetEntity = new DataSetEntity();

        dataSetEntity.setId(datasetId);

        dataSetEntity = dataSetEntityRepository.findOne(Example.of(dataSetEntity)).get();

        String path = dataSetEntity.getDatapath();
        //String path = "E:\\tmp\\tes\\ydy\\data\\data.json";

        String jsonStr = FileUtils.readFileToString(new File(path), "utf8");
        List<MetricsInfo> res=new ArrayList<>();

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        for (Object o :
                jsonObject.values()) {
            JSONObject data = (JSONObject) o;
            String versionName = data.getString("VersionName");
            MicroservicesInfo microservices = data.getJSONObject("Microservices").toJavaObject(MicroservicesInfo.class);
            HashMap<String, GoPackagesInfo> packagesMap = new HashMap<>();
            JSONObject pkgs = data.getJSONObject("Packages");
            for (String repoName :
                    pkgs.keySet()) {
                GoPackagesInfo goPackagesInfo = pkgs.getJSONObject(repoName).toJavaObject(GoPackagesInfo.class);
                packagesMap.put(repoName, goPackagesInfo);
            }

            //存储单个版中所有微服务名称
            List<String> microserviceNames = new ArrayList<>();
            for (Microservice microservice : microservices) {
                microserviceNames.add(microservice.getElementName());
            }

            res.add(MetricsResult(versionName,packagesMap,microserviceNames,microservices));

            System.out.println(versionName);
        }

        return ResponseEntity.ok(res);

    }


    public static MetricsInfo MetricsResult(String version, HashMap<String, GoPackagesInfo> packagesInfoHashMap, List<String> microserviceName, MicroservicesInfo microserviceInfo){

        //if(version.equals("20191101")) {
        MetricsInfo metricsInfo = MetricsInfo.createInfo(version, packagesInfoHashMap, microserviceName, microserviceInfo);
        for (Metrics metrics : metricsInfo.getMetricsList()) {
//                    if(metrics.getElementName().equals("x_13/x_46f")){

            System.out.println(metrics);
//                    }
        }
        System.out.println("版本号：" + version + "中共有" + metricsInfo.getMetricsList().size() + "个微服务");
        //}
        return metricsInfo;


    }

    public static final String path = "E:/tmp/tes/ydy/file";

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

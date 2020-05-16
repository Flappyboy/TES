package top.jach.tes.app.jhkt.lijiaqi.forydy;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.io.FileUtils;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.plugin.jhkt.DataAction;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.metrics.Metrics;
import top.jach.tes.plugin.jhkt.metrics.MetricsInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.version.Version;
import top.jach.tes.plugin.tes.code.git.version.VersionsInfo;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;
import top.jach.tes.plugin.tes.utils.FileCompress;
import top.jach.tes.plugin.tes.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: qiming
 * @date: 2020/1/11 16:41
 * @description:
 */
public class MetricsMain extends DevApp {

    public static void main(String[] args) throws IOException {
        Context context = Environment.contextFactory.createContext(Environment.defaultProject);

        ReposInfo reposInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.TargetSystem, ReposInfo.class);

        VersionsInfo versionsInfoForRelease = DataAction.queryLastInfo(context, InfoNameConstant.VersionsForRelease, VersionsInfo.class);

        File dir = new File("E:\\tmp\\tes\\ydy");
        File dataDir = new File(dir.getAbsolutePath()+"/data");
        if(!dataDir.exists())
            dataDir.mkdirs();
        FileUtils.cleanDirectory(dataDir);

        Map<String, Map<String, Object>> allData = new LinkedHashMap<>();
        for (Version version :
                versionsInfoForRelease.getVersions()) {
            // 查询version版本的微服务
            MicroservicesInfo microservices = DataAction.queryLastMicroservices(context, reposInfo.getId(), null, version);

            //存储单个版本中（仓库名，goPackageInfo）
            HashMap<String, GoPackagesInfo> packagesMap = new HashMap<>();

            for (Repo repo :
                    reposInfo.reposFromNames(version.repos())) {

                GoPackagesInfo goPackagesInfo = DataAction.queryLastGoPackagesInfo(context, reposInfo.getId(), repo.getName(), version);

//                }
                if (goPackagesInfo != null) {
                    packagesMap.put(repo.getName(), goPackagesInfo);
                }

            }

            //存储单个版中所有微服务名称
            List<String> microserviceNames = new ArrayList<>();
            for (Microservice microservice : microservices) {
                microserviceNames.add(microservice.getElementName());
            }

            MetricsResult(version.getVersionName(),packagesMap,microserviceNames,microservices);
            System.out.println(reposInfo + " " + versionsInfoForRelease + " " + microservices + " " +packagesMap);
            //需要代码仓，版本，微服务，包的info，reposInfo、VersionsInfo、MicroservicesInfo、GoPackagesInfo

            Map<String, Object> data = new HashMap<>();
            allData.put(version.getVersionName(), data);
            data.put("VersionName", version.getVersionName());
            data.put("Microservices", microservices);
            data.put("Packages", packagesMap);
        }

        File dataFile = new File(dataDir+"/data.json");

        FileUtils.write(dataFile, JSONObject.toJSONString(allData, SerializerFeature.PrettyFormat), "utf8");

        readFile(dataFile);
    }
    public static void readFile(File file) throws IOException {
        String jsonStr = FileUtils.readFileToString(file, "utf8");
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
            System.out.println(versionName);
        }
    }

    public static void MetricsResult(String version, HashMap<String, GoPackagesInfo> packagesInfoHashMap, List<String> microserviceName, MicroservicesInfo microserviceInfo){

        if(version.equals("20191101")) {
            MetricsInfo metricsInfo = MetricsInfo.createInfo(version, packagesInfoHashMap, microserviceName, microserviceInfo);
            for (Metrics metrics : metricsInfo.getMetricsList()) {
//                    if(metrics.getElementName().equals("x_13/x_46f")){

                System.out.println(metrics);
//                    }
            }
            System.out.println("版本号：" + version + "中共有" + metricsInfo.getMetricsList().size() + "个微服务");
        }
        //return metricsInfo;


    }

    public static void de(File file){

    }
}

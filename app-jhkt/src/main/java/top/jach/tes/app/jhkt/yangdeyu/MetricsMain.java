package top.jach.tes.app.jhkt.yangdeyu;


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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: qiming
 * @date: 2020/1/11 16:41
 * @description:
 */
public class MetricsMain extends DevApp {

    public static void main(String[] args) {
        Context context = Environment.contextFactory.createContext(Environment.defaultProject);

        ReposInfo reposInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.TargetSystem, ReposInfo.class);

        VersionsInfo versionsInfoForRelease = DataAction.queryLastInfo(context, InfoNameConstant.VersionsForRelease, VersionsInfo.class);

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
        }


    }

    public static void MetricsResult(String version, HashMap<String, GoPackagesInfo> packagesInfoHashMap, List<String> microserviceName, MicroservicesInfo microserviceInfo){
        //if(version.equals("x_1635-x_95d.x_4af.x_893_x_1ff_x_e0af_x_e0a3_x_e0b1")){
            MetricsInfo metricsInfo = MetricsInfo.createInfo(version,packagesInfoHashMap,microserviceName,microserviceInfo);
            for(Metrics metrics:metricsInfo.getMetricsList()){
//                    if(metrics.getElementName().equals("x_13/x_46f")){

                        System.out.println(metrics);
//                    }
            }
            System.out.println("版本号："+version+"中共有"+metricsInfo.getMetricsList().size()+"个微服务");
       // }

    }

}

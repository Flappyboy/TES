package top.jach.tes.app.jhkt.lijiaqi;

import top.jach.tes.app.jhkt.chenjiali.CorrelationDataInfo;
import top.jach.tes.app.jhkt.lijiaqi.result.Result;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.plugin.jhkt.DataAction;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.analysis.MicroserviceAttrsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmellAction;
import top.jach.tes.plugin.jhkt.arcsmell.cyclic.CyclicAction;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.metrics.Metrics;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.version.Version;
import top.jach.tes.plugin.tes.code.git.version.VersionsInfo;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DemoMain extends DevApp {
    public static void main(String[] args) {
        //微服务和关系
        /*MicroservicesInfo microservices = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroservicesForRepos, MicroservicesInfo.class);
        microservices = MicroservicesInfo.createInfoByExcludeMicroservice(microservices,
                "x_2b", "x_1b", "x_23", "x_1d/x_6eed",
                "x_39","x_1f","x_27/x_25","c_demo/c_demoa","c_demo/c_demob",
                "x_13/x_ae5", "x_25", "x_21/7103");
        microservices.setName(InfoNameConstant.MicroservicesForReposExcludeSomeHistory);

        PairRelationsInfo pairRelationsInfo = microservices.callRelationsInfoByTopic(true);
        pairRelationsInfo.setName(InfoNameConstant.MicroserviceExcludeSomeCallRelation);
        System.out.println(pairRelationsInfo);*/
        /*Method[] ms = Metrics.class.getMethods();
        for (Method m :
                ms) {
            System.out.println(m.getName());
        }*/
//        InfoTool.saveInputInfos(pairRelationsInfo);

//        microservices.noRelationsByTopic();

        Context context = Environment.contextFactory.createContext(Environment.defaultProject);

        ReposInfo reposInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.TargetSystem, ReposInfo.class);

        VersionsInfo versionsInfoForRelease = DataAction.queryLastInfo(context, InfoNameConstant.VersionsForRelease, VersionsInfo.class);

        System.out.println("source,target");
        for (int i = 0; i < versionsInfoForRelease.getVersions().size()-1; i++) {
            Version version = versionsInfoForRelease.getVersions().get(i);
        /*}
        for (Version version:
                versionsInfoForRelease.getVersions()) {//每一轮循环代表一个sheet页*/

            //查询version name
            String n_version = version.getVersionName();

            // 查询version版本下的所有微服务
            MicroservicesInfo microservices = DataAction.queryLastMicroservices(context, reposInfo.getId(), null, version);

            // 计算并存储微服务间的调用关系，用于后续架构异味的计算
            PairRelationsInfo pairRelationsInfoWithoutWeight = microservices.callRelationsInfoByTopic(false).deWeight();
            pairRelationsInfoWithoutWeight.setName(InfoNameConstant.MicroserviceExcludeSomeCallRelation);
//            System.out.println(n_version+"   "+(i+1));
            for (PairRelation pr :
                    pairRelationsInfoWithoutWeight) {
                String sourceName = pr.getSourceName()+"_v"+(i+1);
                String targetName = pr.getTargetName()+"_v"+(i+1);
                System.out.println(sourceName+","+targetName);
            }
        }
    }
}

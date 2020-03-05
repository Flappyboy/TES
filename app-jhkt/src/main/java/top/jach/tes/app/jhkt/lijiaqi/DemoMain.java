package top.jach.tes.app.jhkt.lijiaqi;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmellAction;
import top.jach.tes.plugin.jhkt.arcsmell.cyclic.CyclicAction;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.metrics.Metrics;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

import java.lang.reflect.Method;
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
        Method[] ms = Metrics.class.getMethods();
        for (Method m :
                ms) {
            System.out.println(m.getName());
        }
//        InfoTool.saveInputInfos(pairRelationsInfo);

//        microservices.noRelationsByTopic();
    }
}

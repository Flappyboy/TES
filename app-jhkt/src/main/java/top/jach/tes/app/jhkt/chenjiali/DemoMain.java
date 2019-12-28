package top.jach.tes.app.jhkt.chenjiali;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.api.domain.action.DefaultInputInfos;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.arcsmell.DemoAction;
import top.jach.tes.plugin.jhkt.arcsmell.cyclic.CyclicAction;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

public class DemoMain extends DevApp {
    public static void main(String[] args) {
        //微服务和关系
        MicroservicesInfo microservices = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroservicesForRepos, MicroservicesInfo.class);

        microservices = MicroservicesInfo.createInfoByExcludeMicroservice(microservices,  "x_2b", "x_1b", "x_23", "x_1d/x_6eed");
        microservices.setName(InfoNameConstant.MicroservicesForReposExcludeSomeHistory);
        InfoTool.saveInputInfos(microservices);

        PairRelationsInfo pairRelationsInfo = microservices.callRelationsInfoByTopic();
        pairRelationsInfo.setName(InfoNameConstant.MicroserviceExcludeSomeCallRelation);
        InfoTool.saveInputInfos(pairRelationsInfo);


        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .addInfoProfile(DemoAction.Elements_INFO, microservices)
                .addInfoProfile(DemoAction.PAIR_RELATIONS_INFO, pairRelationsInfo)
                ;

        Action action = new HublinkAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
        action = new CyclicAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}
package top.jach.tes.app.jhkt.lijiaqi;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.arcsmell.DemoAction;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

public class DemoMain extends DevApp {
    public static void main(String[] args) {
        MicroservicesInfo microservices = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroservicesForRepos, MicroservicesInfo.class);
        PairRelationsInfo callRelations = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroserviceCallRelation, PairRelationsInfo.class);

        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .addInfoProfile(DemoAction.Elements_INFO, microservices)
                .addInfoProfile(DemoAction.PAIR_RELATIONS_INFO, callRelations)
                ;

        Action action = new DemoAction();
        TaskTool.excuteActionAndSaveInfo(action, infoProfileMap);
    }
}

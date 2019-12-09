package top.jach.tes.jhkt.lijiaqi;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.easy.InfoTool;
import top.jach.tes.core.easy.InputInfoProfiles;
import top.jach.tes.core.easy.TaskTool;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.dev.app.DevApp;
import top.jach.tes.plugin.jhkt.DataAction;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.arcsmell.DemoAction;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.tree.GitTreeAction;

import java.io.File;

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

package top.jach.tes.app.jhkt.lijiaqi;

import org.n3r.idworker.Id;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultInputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.data.ExportDataAction;

import java.io.File;

public class ExportDataMain extends DevApp {
    public static void main(String[] args) throws ActionExecuteFailedException {
        Info info = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroservicesForRepos, MicroservicesInfo.class);
        Info info2 = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroserviceCallRelation, PairRelationsInfo.class);

        InputInfos inputInfos = new DefaultInputInfos()
                .putInfo(ExportDataAction.InfoPrefix+1, info)
                .putInfo(ExportDataAction.InfoPrefix+2, info2)
                .putInfo(ExportDataAction.ExportDir, FileInfo.createInfo(new File("E://tmp/tes/export")))
                ;

        Action action = new ExportDataAction();
        action.execute(inputInfos, Environment.contextFactory.createContext(Environment.defaultProject));
    }
}

package top.jach.tes.plugin;

import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.info.Info;

import java.util.Arrays;

public class InfoTool {
    public static void saveInfos(Info... infos){
        Environment.infoService().saveInfos(ProjectTool.DevAppProject(), Arrays.asList(infos));
    }

    public static void saveInputInfos(Info... infos){
        for (Info info :
                infos) {
            info.setName(InputInfos.INFO_NAME);
        }
        Environment.infoService().saveInfos(ProjectTool.DevAppProject(), Arrays.asList(infos));
    }
}

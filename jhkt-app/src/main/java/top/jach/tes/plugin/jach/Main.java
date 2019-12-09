package top.jach.tes.plugin.jach;

import top.jach.tes.core.easy.InputInfoProfiles;
import top.jach.tes.core.easy.TaskTool;
import top.jach.tes.dev.app.DevApp;
import top.jach.tes.plugin.jhkt.DataAction;

import java.io.File;

public class Main extends DevApp {
    public static void main(String[] args) {
        String dataFilePath = null;
        if (args.length>0){
            dataFilePath = args[0];
        }
        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .createSaveValueInfos(DataAction.DATA_FILE,
                        new File(dataFilePath))
                ;
        DataAction dataAction = new DataAction();
        TaskTool.excuteAction(dataAction, infoProfileMap);
    }
}

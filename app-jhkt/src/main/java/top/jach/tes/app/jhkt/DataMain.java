package top.jach.tes.app.jhkt;

import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.plugin.jhkt.DataAction;

import java.io.File;

// 用于导入从企业获取的
public class DataMain extends DevApp {
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

package top.jach.tes.app.jhkt;

import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.app.mock.TaskTool;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.plugin.jhkt.DataAction;
import top.jach.tes.plugin.tes.data.DataActionUtils;

import java.io.File;

/**
 * 用于导入从企业获取的数据，输入需要是一个data目录:
 * - data
 *   - data_v20191220001
 *     - xxx_xxx.json
 *     - xxx_xxx.json
 *   - data_v20191221001
 *     - xxx_xxx.json
 *     - xxx_xxx.json
 */
public class DataMain extends DevApp {
    public static void main(String[] args) {
        // 数据源idWorker: 0189, 0223, 导入数据时需要检查本地IdWorker与数据源机器上的IdWorker不冲突，否则可能会发生Id的冲突
        DataActionUtils.checkIdWorker(189L, 223L);

        String dataFilePath ="D:\\FILE\\work\\data";
        if (args.length>0){
            dataFilePath = args[0];
        }
        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .createSaveValueInfos(DataAction.DATAS_DIR,
                        new File(dataFilePath))
                ;
        DataAction dataAction = new DataAction();
        TaskTool.excuteAction(dataAction, infoProfileMap);
    }
}

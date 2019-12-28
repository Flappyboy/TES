package top.jach.tes.plugin.tes.data;

import org.n3r.idworker.Id;
import org.n3r.idworker.utils.Utils;

import java.io.File;

public class DataActionUtils {
    public static void checkIdWorker(Long... invalidIds){
        long idWoker = Id.getWorkerId();
        for (Long id :
                invalidIds) {
            if(id == idWoker){
                StringBuilder ids = new StringBuilder();
                for (Long invalidId :
                        invalidIds) {
                    ids.append(invalidId);
                    ids.append(", ");
                }
                File idWorkerHome = Utils.createIdWorkerHome();
                throw new RuntimeException(String.format("当前的IdWoker为：%d，与数据源IdWoker冲突，请修改用户目录下.idwoker/目录（可能的路径是： %s）下文件名后四位\n" +
                        "无效的Id如下： %s", idWoker, idWorkerHome.getAbsolutePath(), ids.toString()));
            }
        }
    }
}

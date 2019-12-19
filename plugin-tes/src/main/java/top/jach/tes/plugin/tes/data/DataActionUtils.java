package top.jach.tes.plugin.tes.data;

import org.n3r.idworker.Id;

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
                throw new RuntimeException(String.format("当前的IdWoker为：%d，与数据源IdWoker冲突，请修改用户目录下.idwoker/目录下文件名后四位\n" +
                        "无效的Id如下： %s", idWoker, ids.toString()));
            }
        }
    }
}

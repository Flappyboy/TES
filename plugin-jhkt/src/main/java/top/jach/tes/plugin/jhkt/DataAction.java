package top.jach.tes.plugin.jhkt;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.info.DefaultInputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;
import top.jach.tes.plugin.tes.data.ImportDataAction;

import java.util.List;

public class DataAction implements Action {
    public static final String DATA_FILE = "data_file";

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Meta getInputMeta() {
        return null;
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        List<Info> infos = new ImportDataAction().execute(new DefaultInputInfos().
                putInfo(ImportDataAction.DATA_FILE, inputInfos.getInfo(DATA_FILE, FileInfo.class)), context).getInfoList();

        ReposInfo reposInfo = findOne(infos, ReposInfo.class);

        MicroservicesInfo microservices = MicroservicesInfo.createInfo(infos.toArray(new Info[infos.size()])).setReposId(reposInfo.getId());
        microservices.setName(InfoNameConstant.MicroservicesForRepos);
        // TODO 去除重复的microservice

        new SaveInfoAction().execute(new DefaultInputInfos().putInfo("1", microservices),context);

        PairRelationsInfo pairRelationsInfo = microservices.callRelationsInfoByTopic();
        pairRelationsInfo.setName(InfoNameConstant.MicroserviceCallRelation);
        new SaveInfoAction().execute(new DefaultInputInfos().putInfo("1", pairRelationsInfo),context);

        return null;
    }

    private <I> I findOne(List objects, Class<I> clazz){
        for (Object o :
                objects) {
            if (clazz.isAssignableFrom(o.getClass())) {
                return (I) o;
            }
        }
        return null;
    }
}

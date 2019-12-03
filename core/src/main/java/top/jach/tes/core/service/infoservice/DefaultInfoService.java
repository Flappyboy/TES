package top.jach.tes.core.service.infoservice;

import top.jach.tes.core.domain.context.BaseContext;
import top.jach.tes.core.domain.Project;
import top.jach.tes.core.domain.action.Action;
import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.action.SaveInfoAction;
import top.jach.tes.core.domain.info.DefaultInputInfos;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.factory.ContextFactory;

public class DefaultInfoService implements InfoService{
    ContextFactory contextFactory;

    public DefaultInfoService(ContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }

    @Override
    public void saveInfos(Project project, Iterable<Info> infos) {
        InputInfos inputInfos = new DefaultInputInfos();
        Long i = -1l;
        for (Info info :
                infos) {
            i++;
            inputInfos.put(String.valueOf(i), info);
        }
        Action action = new SaveInfoAction();
        action.execute(inputInfos, contextFactory.createContext(project));
    }
}

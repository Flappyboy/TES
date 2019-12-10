package top.jach.tes.core.impl.service;

import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.service.InfoService;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.api.domain.action.DefaultInputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.api.factory.ContextFactory;

public class DefaultInfoService implements InfoService {
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
        try {
            action.execute(inputInfos, contextFactory.createContext(project));
        } catch (ActionExecuteFailedException e) {
            e.printStackTrace();
        }
    }
}

package top.jach.tes.core.domain.action;

import top.jach.tes.core.context.Context;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.meta.Meta;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

public class SaveInfoAction implements Action {

    public SaveInfoAction() {
    }

    @Override
    public String getName() {
        return "SAVE_INFO";
    }

    @Override
    public String getDesc() {
        return "选择合适的InfoRepository进行存储";
    }

    @Override
    public Meta getInputMeta() {
        return null;
    }

    @Override
    public OutputInfos execute(InputInfos inputInfo, Context context) {
        InfoRepositoryFactory infoRepositoryFactory = context.InfoRepositoryFactory();
        for (Info info :
                inputInfo.values()) {
            infoRepositoryFactory.getRepository(info.getInfoClass()).saveProfile(info, context.currentTask().getProject().getId());
            infoRepositoryFactory.getRepository(info.getInfoClass()).saveDetail(info);
        }
        return null;
    }
}

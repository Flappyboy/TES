package top.jach.tes.core.impl.domain.action;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

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
            if(info.getId()==null){
                info.initBuild();
            }
            if(!info.getName().startsWith("TES")) {
                context.Logger().info("Save: id: {}, name: {}, infoClass: {}", info.getId(), info.getName(), info.getInfoClass());
            }
            infoRepositoryFactory.getRepository(info.getInfoClass()).saveProfile(info, context.currentProject().getId());
            infoRepositoryFactory.getRepository(info.getInfoClass()).saveDetail(info);
        }
        return null;
    }
}

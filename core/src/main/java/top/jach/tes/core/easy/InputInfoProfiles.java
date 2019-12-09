package top.jach.tes.core.easy;

import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.info.DefaultInputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.impl.domain.info.value.ValueInfo;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

import java.util.HashMap;
import java.util.Map;

public class InputInfoProfiles extends HashMap<String, InfoProfile> {
    public static InputInfoProfiles InputInfoProfiles(){
        return new InputInfoProfiles();
    }

    public InputInfoProfiles addInfoProfile(String name, Info info){
        put(name, InfoProfile.createFromInfo(info));
        return this;
    }

    public InputInfoProfiles createSaveValueInfos(String name, Object object){
        ValueInfo info = InfoTool.wrapperValueInfo(object);
        InfoTool.saveInputInfos(info);
        addInfoProfile(name, info);
        return this;
    }

    public InputInfos toInputInfos(InfoRepositoryFactory infoRepositoryFactory){
        InputInfos inputInfos = new DefaultInputInfos();
        for (Map.Entry<String, InfoProfile> entry :
                this.entrySet()) {
            inputInfos.putInfoFromProfile(entry.getKey(), entry.getValue(), infoRepositoryFactory);
        }
        return inputInfos;
    }
}

package top.jach.tes.dev.app;

import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.info.DefaultInputInfos;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoProfile;
import top.jach.tes.core.domain.info.value.IntegerInfo;
import top.jach.tes.core.domain.info.value.LongInfo;
import top.jach.tes.core.domain.info.value.StringInfo;
import top.jach.tes.core.domain.info.value.ValueInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

import java.io.File;
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

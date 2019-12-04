package top.jach.tes.dev.app;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoProfile;
import top.jach.tes.core.domain.info.value.IntegerInfo;
import top.jach.tes.core.domain.info.value.LongInfo;
import top.jach.tes.core.domain.info.value.StringInfo;
import top.jach.tes.core.domain.info.value.ValueInfo;

import java.io.File;
import java.util.HashMap;

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
}

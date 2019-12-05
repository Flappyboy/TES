package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoProfile;
import top.jach.tes.core.domain.info.value.FileInfo;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface InputInfos extends Map<String, Info> {
    String INFO_NAME = "TES_INPUT_INFO";

    <I extends Info> I getInfo(String fieldName, Class<I> clazz);

    default InputInfos putInfoFromProfile(String fieldName, InfoProfile infoProfile, InfoRepositoryFactory infoRepositoryFactory){
        Info info = infoProfile.toInfoWithDetail(infoRepositoryFactory);
        if(info != null){
            put(fieldName, info);
        }
        return this;
    }
}

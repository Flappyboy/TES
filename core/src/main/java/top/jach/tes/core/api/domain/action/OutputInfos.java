package top.jach.tes.core.api.domain.action;

import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public interface OutputInfos {

    List<OutputInfo> getOutputInfoList();

    default List<Info> getInfoList(){
        List<Info> infos = new ArrayList<>();
        for (OutputInfo outputInfo :
                getOutputInfoList()) {
            infos.add(outputInfo.getInfo());
        }
        return infos;
    }

    default <I extends Info> I getFirstByInfoClass(Class<I> infoClass){
        for (Info info :
                getInfoList()) {
            if (info.getClass().equals(infoClass)) {
                return (I) info;
            }
        }
        return null;
    }

    default <I extends Info> InfoProfile getFirstProfileByInfoClass(Class<I> infoClass){
        return getFirstProfileByInfoClassAndName(infoClass, null);
    }

    default boolean contains(Long infoId){
        for (Info info :
                getInfoList()) {
            if (info.getId().equals(infoId)) {
                return true;
            }
        }
        return false;
    }

    default <I extends Info> InfoProfile getFirstProfileByInfoClassAndName(Class<I> infoClass, String infoName){
        for (Info info :
                getInfoList()) {
            if (!info.getInfoClass().equals(infoClass)) {
                continue;
            }
            if(infoName != null && !infoName.equals(info.getName())){
                continue;
            }
            return InfoProfile.createFromInfo(info);
        }
        return null;
    }

    default <I extends Info> I getFirstFromProfileByInfoClassAndName(Class<I> infoClass, String infoName, InfoRepositoryFactory infoRepositoryFactory) {
        InfoProfile infoProfile = getFirstProfileByInfoClassAndName(infoClass, infoName);
        if (infoProfile == null){
            return null;
        }
        return infoProfile.toInfoWithDetail(infoRepositoryFactory, infoClass);
    }

        default <I extends Info> I getFirstByInfoClassAndName(Class<I> infoClass, String infoName){
        for (Info info :
                getInfoList()) {
            if (info.getClass().equals(infoClass) && infoName.equals(info.getName())) {
                return (I) info;
            }
        }
        return null;
    }
}
package top.jach.tes.core.api.domain.action;

import top.jach.tes.core.api.domain.info.Info;

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
}
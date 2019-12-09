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
}
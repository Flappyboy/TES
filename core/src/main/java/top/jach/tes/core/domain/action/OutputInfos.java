package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.info.Info;

import java.util.List;
import java.util.Set;

public interface OutputInfos {

    List<OutputInfo> getOutputInfoList();

    interface OutputInfo{
        Info getInfo();
        Set<String> flags();

        enum Flag{
            SAVE(),
        }
    }
}
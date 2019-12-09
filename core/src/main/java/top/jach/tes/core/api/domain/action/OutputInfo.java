package top.jach.tes.core.api.domain.action;

import top.jach.tes.core.api.domain.info.Info;

import java.util.Set;

public interface OutputInfo{
    Info getInfo();
    Set<String> flags();

    enum Flag{
        SAVE(),
    }
}

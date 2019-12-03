package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.info.Info;

import java.util.Set;

public interface OutputInfo{
    Info getInfo();
    Set<String> flags();

    enum Flag{
        SAVE(),
    }
}

package top.jach.tes.core.domain.info.matching;

import top.jach.tes.core.domain.info.Info;

import java.util.Set;

public interface InfoClassToNameMatching {
    Set<String> getNamesByClass(Class<? extends Info> clazz);
    Set<Class<? extends Info>> getClassByName(String name);

    String descForNameAndClass(String name, Class<? extends Info> clazz);
}


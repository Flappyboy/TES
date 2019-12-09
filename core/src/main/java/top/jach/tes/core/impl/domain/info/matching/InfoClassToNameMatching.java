package top.jach.tes.core.impl.domain.info.matching;

import top.jach.tes.core.api.domain.info.Info;

import java.util.Set;

/**
 * 原先考虑Info Name 具备一定的业务属性，现在暂不考虑
 */
public interface InfoClassToNameMatching {
    Set<String> getNamesByClass(Class<? extends Info> clazz);
    Set<Class<? extends Info>> getClassByName(String name);

    String descForNameAndClass(String name, Class<? extends Info> clazz);
}


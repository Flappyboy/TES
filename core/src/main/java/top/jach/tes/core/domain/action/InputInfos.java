package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.info.Info;

import java.util.Map;

public interface InputInfos extends Map<String, Info> {
    <I> I getInfo(String fieldName, Class<I> clazz);
}

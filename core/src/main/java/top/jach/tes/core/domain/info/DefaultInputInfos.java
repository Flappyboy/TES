package top.jach.tes.core.domain.info;

import top.jach.tes.core.domain.action.InputInfos;

import java.util.HashMap;

public class DefaultInputInfos extends HashMap<String, Info> implements InputInfos {
    @Override
    public <I> I getInfo(String fieldName, Class<I> clazz) {
        return (I) get(fieldName);
    }
}

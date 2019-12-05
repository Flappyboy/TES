package top.jach.tes.core.domain.info.value;

import top.jach.tes.core.domain.action.InputInfos;
import top.jach.tes.core.domain.info.Info;

public abstract class ValueInfo<V> extends Info {

    abstract V getValue();

    public static <V> V getValueFromInputInfos(InputInfos inputInfos, String fieldName, Class<? extends ValueInfo<V>> valueInfoClass){
        return inputInfos.getInfo(fieldName, valueInfoClass)==null ? null : inputInfos.getInfo(fieldName, valueInfoClass).getValue();
    }
}

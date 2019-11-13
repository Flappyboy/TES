package top.jach.tes.core.factory;

import top.jach.tes.core.domain.info.ValueInfo;

public class ValueInfoFactory {
    public static <V> ValueInfo<V> createValueInfo(V v, String name, Class<V> clazz){
        ValueInfo<V> valueInfo = new ValueInfo<>();
        valueInfo.setV(v)
                .setName(name)
                .initBuild();
        return valueInfo;
    }
}

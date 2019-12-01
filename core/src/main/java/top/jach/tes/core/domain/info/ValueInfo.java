package top.jach.tes.core.domain.info;

import lombok.Getter;

@Getter
public class ValueInfo<V> extends Info {
    private V v;

    public ValueInfo<V> setV(V v) {
        this.v = v;
        return this;
    }

    public static <V> ValueInfo<V> createValueInfo(V v, String name, Class<V> clazz){
        ValueInfo<V> valueInfo = new ValueInfo<>();
        valueInfo.setV(v)
                .setName(name)
                .initBuild();
        return valueInfo;
    }
}

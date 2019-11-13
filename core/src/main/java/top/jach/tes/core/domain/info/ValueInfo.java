package top.jach.tes.core.domain.info;

import lombok.Getter;

@Getter
public class ValueInfo<V> extends Info {
    private V v;

    public ValueInfo<V> setV(V v) {
        this.v = v;
        return this;
    }
}

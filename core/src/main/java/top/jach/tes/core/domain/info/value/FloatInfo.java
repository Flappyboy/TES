package top.jach.tes.core.domain.info.value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FloatInfo extends ValueInfo<Float> {
    Float value;
    public static FloatInfo createInfo(Float value){
        FloatInfo info = new FloatInfo();
        info.setValue(value);
        info.initBuild();
        return info;
    }
}

package top.jach.tes.core.impl.domain.info.value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoubleInfo extends ValueInfo<Double> {
    Double value;
    public static DoubleInfo createInfo(Double value){
        DoubleInfo info = new DoubleInfo();
        info.setValue(value);
        info.initBuild();
        return info;
    }
}

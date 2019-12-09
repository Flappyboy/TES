package top.jach.tes.core.impl.domain.info.value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LongInfo extends ValueInfo<Long>  {
    Long value;
    public static LongInfo createInfo(Long value){
        LongInfo info = new LongInfo();
        info.setValue(value);
        info.initBuild();
        return info;
    }
}

package top.jach.tes.core.domain.info.value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegerInfo extends ValueInfo<Integer>  {
    Integer value;
    public static IntegerInfo createInfo(Integer value){
        IntegerInfo info = new IntegerInfo();
        info.setValue(value);
        info.initBuild();
        return info;
    }
}

package top.jach.tes.core.impl.domain.info.value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringInfo extends ValueInfo<String>  {
    String value;
    public static StringInfo createInfo(String value){
        StringInfo info = new StringInfo();
        info.setValue(value);
        info.initBuild();
        return info;
    }
}

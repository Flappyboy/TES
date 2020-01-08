package top.jach.tes.core.impl.domain.element;

import lombok.Getter;
import lombok.Setter;
import top.jach.tes.core.api.domain.info.Info;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ElementsValue extends Info {
    private Map<String, Double> valueMap = new HashMap<>();

    public static ElementsValue createInfo(){
        ElementsValue info = new ElementsValue();
        info.initBuild();
        return info;
    }

    public ElementsValue put(String elementName, Double value){
        valueMap.put(elementName, value);
        return this;
    }
    public Map getValue(){
        return valueMap;
    }

}

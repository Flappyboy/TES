package top.jach.tes.plugin.jhkt.arcsmell;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.jach.tes.core.api.domain.info.Info;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class ArcSmellsInfo extends Info {
    private Map<String, ArcSmell> elementNameArcSmellMap = new HashMap<>();

    public static ArcSmellsInfo createInfo(){
        ArcSmellsInfo info = new ArcSmellsInfo();
        info.initBuild();
        return info;
    }

    public ArcSmellsInfo put(String elementName, ArcSmell arcSmell) {
        elementNameArcSmellMap.put(elementName, arcSmell);
        return this;
    }

    public ArcSmell find(String elementName){
        return elementNameArcSmellMap.get(elementName);
    }
}

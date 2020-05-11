package top.jach.tes.plugin.jhkt.arcsmell.result;

import top.jach.tes.core.api.domain.info.Info;

import java.util.*;

public class ArcSmellsVersionsInfo extends Info {
    Map<String, ResultForMs> results = new LinkedHashMap<>();

    public static ArcSmellsVersionsInfo createInfo(){
        ArcSmellsVersionsInfo info = new ArcSmellsVersionsInfo();
        info.initBuild();
        return info;
    }

    public void put(String version, ResultForMs resultForMs){
        results.put(version, resultForMs);
    }

    public Set<String> allMicroservices(){
        Set<String> microservices = new HashSet<>();
        for (ResultForMs rfm :
                results.values()) {
            microservices.addAll(rfm.microservice);
        }
        microservices.remove("x_1f");
        return microservices;
    }
}

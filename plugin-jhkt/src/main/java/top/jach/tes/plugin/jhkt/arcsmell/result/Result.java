package top.jach.tes.plugin.jhkt.arcsmell.result;

import lombok.Getter;

import java.util.*;

@Getter
public class Result {
    Map<String, ResultForMs> results = new LinkedHashMap<>();

    public Result() {
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

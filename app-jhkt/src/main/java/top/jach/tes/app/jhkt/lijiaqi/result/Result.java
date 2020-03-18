package top.jach.tes.app.jhkt.lijiaqi.result;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Result {
    Map<String, ResultForMs> results = new HashMap<>();

    public Result() {
    }

    public void put(String version, ResultForMs resultForMs){
        results.put(version, resultForMs);
    }
}

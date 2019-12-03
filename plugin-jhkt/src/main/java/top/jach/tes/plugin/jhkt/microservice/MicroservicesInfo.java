package top.jach.tes.plugin.jhkt.microservice;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class MicroservicesInfo extends Info {
    private List<Microservice> microservices = new ArrayList<>();

    public static MicroservicesInfo createInfo(){
        MicroservicesInfo info = new MicroservicesInfo();
        info.initBuild();
        return info;
    }

    public MicroservicesInfo addMicroservice(Microservice... microservices){
        this.microservices.addAll(Arrays.asList(microservices));
        return this;
    }
}

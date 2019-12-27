package top.jach.tes.plugin.jhkt.analysis;

import lombok.Getter;
import lombok.Setter;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.jhkt.microservice.Microservice;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MicroserviceAttrsInfo extends Info {
    String version;
    List<MicroserviceAttr> microserviceAttrs = new ArrayList<>();
    public static MicroserviceAttrsInfo createInfo(){
        MicroserviceAttrsInfo info = new MicroserviceAttrsInfo();
        info.initBuild();
        return info;
    }
    public MicroserviceAttrsInfo addMicroserviceAttr(MicroserviceAttr microserviceAttr){
        microserviceAttrs.add(microserviceAttr);
        return this;
    }
}

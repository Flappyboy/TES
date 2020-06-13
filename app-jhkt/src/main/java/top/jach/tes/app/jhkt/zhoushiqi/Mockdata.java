package top.jach.tes.app.jhkt.zhoushiqi;

import lombok.Getter;
import top.jach.tes.plugin.jhkt.maintain.size.AllVersionMicroServices;
import top.jach.tes.plugin.jhkt.maintain.size.MicroService;
import top.jach.tes.plugin.jhkt.maintain.size.Microservices;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
@Getter
public class Mockdata {
    AllVersionMicroServices allVersionMicroServices = new AllVersionMicroServices();

    public Mockdata(){
        for (int i = 0; i < 5; i++){
            Microservices microservices = new Microservices();
            for (int j = 0; j < 5; j++){
                MicroService microService;
                if (j%2 == 0){
                    microService = new MicroService(true, true, false,
                            true, false, false, false, false,
                            j+1);
                }else {
                    microService = new MicroService(true, false, true,
                            false, true, false, false, false,
                            j+1);
                }
                microservices.getMicroServices().add(microService);
            }
//            something wrong
//            allVersionMicroServices.setVersion(i);

            allVersionMicroServices.getAllVMicroservices().add(microservices);
        }

    }

}

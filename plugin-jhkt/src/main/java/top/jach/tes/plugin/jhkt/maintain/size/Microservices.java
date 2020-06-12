package top.jach.tes.plugin.jhkt.maintain.size;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
@Setter
@Getter
public class Microservices {

    //Directly Connected Services
    private int numOfConnectedServices;

    private List<MicroService> microServices = new ArrayList<>();

    public int getNumOfConnectedServices() {
        if (microServices == null){
            return 0;
        }
        int res = 0;
        for (MicroService microService: microServices){
            if (microService.isDirectlyConnected()){
                res ++;
            }
        }
        return res;
    }
}

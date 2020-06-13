package top.jach.tes.plugin.jhkt.maintain.size;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/13
 */
public class NumberOfProducerServices {
    public static int NOPS(Microservices microservices){
        int res = 0;
        for (MicroService microService : microservices.getMicroServices()){
            if (microService.isProducer()){
                res ++;
            }
        }
        return res;
    }
}

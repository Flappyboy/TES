package top.jach.tes.plugin.jhkt.maintain.size;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/13
 */
public class NumberOfConsumerServices {

    public static int NOCS(Microservices microservices){
        int res = 0;
        for (MicroService microService : microservices.getMicroServices()){
            if (microService.isConsumer()){
                res ++;
            }
        }
        return res;
    }
}

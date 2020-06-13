package top.jach.tes.plugin.jhkt.maintain.size;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
public class NumberOfDCConsumer {
    public static int NODCCS(Microservices microservices){
        int res = 0;
        for (MicroService microService : microservices.getMicroServices()){
            if (microService.isDCConsumer()){
                res ++;
            }
        }
        return res;
    }
}

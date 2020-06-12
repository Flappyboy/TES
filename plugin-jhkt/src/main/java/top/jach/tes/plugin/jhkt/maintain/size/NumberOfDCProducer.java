package top.jach.tes.plugin.jhkt.maintain.size;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
public class NumberOfDCProducer {
    public static int NODCPS(Microservices microservices){
        int res = 0;
        for (MicroService microService : microservices.getMicroServices()){
            if (microService.isDCProcuder()){
                res ++;
            }
        }
        return res;
    }
}

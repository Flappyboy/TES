package top.jach.tes.plugin.jhkt.maintain.size;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
public class NumberOfSynInterfaces {
    public static int SynInterfaces(Microservices microservices){
        int res = 0;
        for (MicroService microService : microservices.getMicroServices()){
            res += microService.getNumOfSynInterfaces();
        }
        return res;
    }
}

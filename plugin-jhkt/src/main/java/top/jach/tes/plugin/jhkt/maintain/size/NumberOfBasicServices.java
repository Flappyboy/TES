package top.jach.tes.plugin.jhkt.maintain.size;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
public class NumberOfBasicServices {
    public static int NOBS(Microservices microservices){
        int res = 0;
        for (MicroService microService : microservices.getMicroServices()){
            if (microService.isBasic()){
                res ++;
            }
        }
        return res;
    }
}

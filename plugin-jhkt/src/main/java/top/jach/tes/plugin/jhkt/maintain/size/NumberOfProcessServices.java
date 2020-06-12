package top.jach.tes.plugin.jhkt.maintain.size;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
public class NumberOfProcessServices {
    public static int NOPS(Microservices microservices){
        int res = 0;
        for (MicroService microService : microservices.getMicroServices()){
            if (microService.isProcess()){
                res ++;
            }
        }
        return res;
    }
}

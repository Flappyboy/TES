package top.jach.tes.plugin.jhkt.maintain.size;


/**
 * @Author: zhoushiqi
 * @date: 2020/4/13
 */

//size类NOS实现
public class NumberOfServices {
    public static int getNOS(Microservices microservices){
        return microservices.getMicroServices().size();
    }
}

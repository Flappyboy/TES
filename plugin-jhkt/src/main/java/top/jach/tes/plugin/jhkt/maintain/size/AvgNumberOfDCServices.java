package top.jach.tes.plugin.jhkt.maintain.size;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
public class AvgNumberOfDCServices {
    public static int AvgNODCS(AllVersionMicroServices allVersionMicroServices){
        return (int) allVersionMicroServices.getAllVMicroservices()
                .stream()
                .mapToInt(Microservices::getNumOfConnectedServices)
                .average()
                .getAsDouble();
    }
}

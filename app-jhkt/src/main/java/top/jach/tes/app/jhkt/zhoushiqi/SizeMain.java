package top.jach.tes.app.jhkt.zhoushiqi;

import top.jach.tes.plugin.jhkt.maintain.size.*;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
public class SizeMain {
    public static void main(String[] args) {
        //mock data
        Mockdata mockdata = new Mockdata();

        System.out.println("AvgNumberOfDCService");
        System.out.println(AvgNumberOfDCServices.AvgNODCS(mockdata.getAllVersionMicroServices()));

        System.out.println("NumerOfService");
        System.out.println(NumberOfServices.getNOS(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));

        System.out.println("NumberOfProducer");
        System.out.println(NumberOfProducerServices.NOPS(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));

        System.out.println("NumberOfConsumer");
        System.out.println(NumberOfConsumerServices.NOCS(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));

        System.out.println("NumberOfDCProducer");
        System.out.println(NumberOfDCProducer.NODCPS(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));

        System.out.println("NumberOfDCConsumer");
        System.out.println(NumberOfDCConsumer.NODCCS(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));

        System.out.println("NumberOfProcess");
        System.out.println(NumberOfProcessServices.NOPS(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));

        System.out.println("NumberOfIntermediary");
        System.out.println(NumberOfIntermediaryServices.NOIS(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));

        System.out.println("NumberOfBasic");
        System.out.println(NumberOfBasicServices.NOBS(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));

        System.out.println("NumberOfSynInterfaces");
        System.out.println(NumberOfSynInterfaces.SynInterfaces(mockdata
                .getAllVersionMicroServices().getAllVMicroservices().get(2)));
    }
}

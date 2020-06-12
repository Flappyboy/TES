package top.jach.tes.plugin.jhkt.maintain.size;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Getter;
import lombok.Setter;
import top.jach.tes.plugin.jhkt.microservice.Microservice;

/**
 * @Author: zhoushiqi
 * @date: 2020/4/18
 */
//该类为size类指标所需输入，继承于原系统的Microservice
//主要进行了一些服务判断
//具体判断方法需要在数据处理时进行处理
@Getter
@Setter
public class MicroService extends Microservice {

    //Directly Connected Service
    private boolean isDirectlyConnected;
    //Directly Connected Producer Serivce
    private boolean isDCProcuder;
    //Directly Connected Consumer Serivce
    private boolean isDCConsumer;
    //Producer Service
    private boolean isProducer;
    //Consumer Service
    private boolean isConsumer;
    //Process Service
    private boolean isProcess;
    //Intermediary Service
    private boolean isIntermediary;
    //Basic Service
    private boolean isBasic;
    //synchronous interface
    private int numOfSynInterfaces;

    public MicroService(boolean isDirectlyConnected,
                        boolean isDCProcuder,
                        boolean isDCConsumer,
                        boolean isProducer,
                        boolean isConsumer,
                        boolean isProcess,
                        boolean isIntermediary,
                        boolean isBasic,
                        int numOfSynInterfaces) {
        this.isDirectlyConnected = isDirectlyConnected;
        this.isDCProcuder = isDCProcuder;
        this.isDCConsumer = isDCConsumer;
        this.isProducer = isProducer;
        this.isConsumer = isConsumer;
        this.isProcess = isProcess;
        this.isIntermediary = isIntermediary;
        this.isBasic = isBasic;
        this.numOfSynInterfaces = numOfSynInterfaces;
    }
}

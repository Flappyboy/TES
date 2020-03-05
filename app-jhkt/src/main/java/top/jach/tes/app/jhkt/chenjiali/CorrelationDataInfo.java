package top.jach.tes.app.jhkt.chenjiali;

import top.jach.tes.core.api.domain.info.Info;

import java.util.ArrayList;
import java.util.List;

public class CorrelationDataInfo extends Info {
    String version;
    List<CorrelationData> correlationDatas=new ArrayList<>();

    public static CorrelationDataInfo creatInfo(){
        CorrelationDataInfo info=new CorrelationDataInfo();
        info.initBuild();
        return info;
    }

    public CorrelationDataInfo addCorrelationDta(CorrelationData correlationData){
        correlationDatas.add(correlationData);
        return this;
    }

    public void setVersion(String version){
        this.version=version;
    }

    public String getVersion(){
        return this.version;
    }
    public List<CorrelationData> getCorrelationDatas(){
        return this.correlationDatas;
    }



}

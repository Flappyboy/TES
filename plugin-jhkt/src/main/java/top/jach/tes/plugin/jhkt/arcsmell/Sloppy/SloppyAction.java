package top.jach.tes.plugin.jhkt.arcsmell.Sloppy;

import org.apache.commons.compress.utils.Lists;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.arcsmell.ud.UdAction;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

import java.util.*;

/**
 * @author:AdminChen
 * @date:2020/4/29
 * @description:
 */
public class SloppyAction implements Action {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Meta getInputMeta() {
        return null;
    }


    public static ElementsValue calculateSD(MicroservicesInfo microservices, PairRelationsInfo pairRelationsInfo,double thsd){
        Map<String,List<String>> targetServiceMap= UdAction.getDependencies(microservices,pairRelationsInfo);
        Map<String,String> parentChildMap=new HashMap<>();//用于存放存在SD异味的一对微服务，A的少量功能是B的所有功能，<A,B>对应一组map
        ElementsValue inElement= HublinkAction.calculateHublikeIn(pairRelationsInfo);//所有微服务的in依赖计算结果
        ElementsValue outElement=HublinkAction.calculateHublikeOut(pairRelationsInfo);
        //所有微服务对应的sd检测结果
        Map<String,Double> resultMap=new HashMap<>();
        for(Microservice ms:microservices){
            String microName=ms.getElementName();
            double sdCount=0.0;
            for(String str:targetServiceMap.get(microName)){//遍历当前微服务对应的targetservice集合
                //由于double不能直接比较是否相等，存在精度差，因此第二个判断直接用<1.0代替==0，因为hublink_out绝对是正数不会是小数
                if(inElement.getValueMap().get(str)<thsd&&outElement.getValueMap().get(str)<1.0){
                    parentChildMap.put(microName,str);//该map左边母微服务，右边是子微服务
                    sdCount=sdCount+1.0;
                }
            }
            resultMap.put(microName,sdCount);
        }
        ElementsValue element=ElementsValue.createInfo();
        for(String key:resultMap.keySet()){
            double value=resultMap.get(key);
            element.put(key,value);
        }
        return element;
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        return null;
    }
}

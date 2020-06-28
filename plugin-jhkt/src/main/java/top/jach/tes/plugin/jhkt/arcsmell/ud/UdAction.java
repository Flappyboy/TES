package top.jach.tes.plugin.jhkt.arcsmell.ud;

import org.apache.commons.compress.utils.Lists;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsInfo;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

import java.util.*;

/**
 * @author:AdminChen
 * @date:2020/4/9
 * @description: detect for unstable dependency
 */
public class UdAction implements Action{
    public static final String Elements_INFO = "elements_info";
    public static final String PAIR_RELATIONS_INFO = "PairRelationsInfo";
    public static final String HUBLINK_IN_AND_OUT="hublinkElements";
    public static final String HUBLINK_IN="hublinkElements_e";
    public static final String HUBLINK__OUT="hublinkElements_s";
    public static final String UD="udElements";
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
        return () -> Arrays.asList(
                InfoField.createField(Elements_INFO).setInfoClass(ElementsInfo.class),
                InfoField.createField(PAIR_RELATIONS_INFO).setInfoClass(PairRelationsInfo.class)
        );
    }
//计算所有微服务对应的instability值
    public static Map<String,Double> calInstability(MicroservicesInfo microservices,PairRelationsInfo pairRelationsInfo){
        Map<String,Double> insMap=new HashMap<>();
        HublinkAction hubAction=new HublinkAction();
       // List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        ElementsValue inElement=hubAction.calculateHublikeIn(pairRelationsInfo);//所有微服务的in依赖计算结果
        ElementsValue outElement=hubAction.calculateHublikeOut(pairRelationsInfo);
        for(Microservice micro:microservices){
            String microName=micro.getElementName();
            double inValue=0.0;
            double outValue=0.0;
            double value=1.0;
            if(inElement.getValueMap().containsKey(microName)){
                inValue=inElement.getValueMap().get(microName);
            }
           if(outElement.getValueMap().containsKey(microName)){
               outValue=outElement.getValueMap().get(microName);
           }
           if(inValue+outValue>0){//若两者均为0，则value默认为0
               value=outValue/(inValue+outValue);
               insMap.put(microName,value);
           }
            insMap.put(microName,value);
        }
        return insMap;
    }
    //遍历pairrelations得到每个微服务所依赖的服务名称的集合，每个微服务与其依赖集合构成map中一个键值对
    public static Map<String,List<String>> getDependencies(MicroservicesInfo microservices,PairRelationsInfo pairRelationsInfo){
        Map<String,List<String>> microDependenciesMap=new HashMap<>();

        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        //遍历relations得到当前服务所依赖的服务名称的集合relist
        for(Microservice micro:microservices){
            String microName=micro.getElementName();
            List<String> relist=new ArrayList<>();
            for(int i=0;i<relations.size();i++){
                if(microName.equals(relations.get(i).getSourceName())){//当前服务处于开始节点位置，则说明依赖
                    relist.add(relations.get(i).getTargetName());
                }
            }
            microDependenciesMap.put(microName,relist);
        }
        return microDependenciesMap;
    }

    public static ElementsValue calculateUd(MicroservicesInfo microservices,PairRelationsInfo pairRelationsInfo){
        //所有微服务对应的instability值（得到的instability值不是0.5就是1,1表示不依赖别人也不被别人依赖）
        Map<String,Double> microInstabMap=calInstability(microservices,pairRelationsInfo);
        //所有微服务对应的依赖服务集合（√）注意有可能存在某微服务对应的依赖服务集合为空，比如x_1f,x_f/x_125,x_13/x_843这三个
        Map<String,List<String>> microDependenciesMap=getDependencies(microservices,pairRelationsInfo);
        //所有微服务对应的ud检测结果
        Map<String,Double> resultMap=new HashMap<>();
        for(Microservice micro:microservices){
            String microName=micro.getElementName();
            double udValue=0.0;
            List<String> dependlist=microDependenciesMap.get(microName);
            if(dependlist.size()==0){
                resultMap.put(microName,udValue);
                continue;
            }else{
                double badCount=0.0;
                double microIns=microInstabMap.get(microName);
                for(String str:dependlist){
                    double dependIns=microInstabMap.get(str);
                    if(microIns<dependIns){
                        badCount=badCount+1.0;//为什么用+=1.0时，上面badCount显示变量未使用？
                    }
                }

                udValue=badCount/(dependlist.size());//UoUD值
                resultMap.put(microName,udValue);
            }


        }
        ElementsValue element=ElementsValue.createInfo();
        for(String key:resultMap.keySet()){
            double value=resultMap.get(key);
            element.put(key,value);
        }
        return element;
    }

    public static ElementsValue calculateUi(MicroservicesInfo microservices,PairRelationsInfo pairRelationsInfo,double impact,double cochange,double change){
        ElementsValue element=ElementsValue.createInfo();

        return element;
    }


    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        return null;
    }
}

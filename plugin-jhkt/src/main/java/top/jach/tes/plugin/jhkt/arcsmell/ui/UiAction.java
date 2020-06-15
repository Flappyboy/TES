package top.jach.tes.plugin.jhkt.arcsmell.ui;

import org.apache.commons.compress.utils.Lists;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.ElementsInfo;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvAction;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvResult;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;

import java.util.*;

/**
 * @author:AdminChen
 * @date:2020/4/9
 * @description: detect for unstable interface
 */
public class UiAction implements Action{
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


    public static ElementsValue calculateUi(List<GitCommit> gitCommits, int len, MicroservicesInfo microservices, PairRelationsInfo pairRelationsInfo, double impact, double cochange, double change){
        ElementsValue element=ElementsValue.createInfo();
        Map<String,Set<String>> resultDetail=new HashMap<>();//存储中间结果，而非只给一个值
//目前已可以通过MvAction类中的detectMvResultForUi方法获取每个微服务对应的与其共同变更的微服务集合及共同变更次数
        //得到的string对应microservice类的getAllPath()方法获得的路径
        ElementsValue inElement=HublinkAction.calculateHublikeIn(pairRelationsInfo);
        List<Microservice> microservices1=microservices.getMicroservices();
        MvResult mvResult= MvAction.detectMvResultForUi(gitCommits,len,microservices1);
        Map<String,Map<String,Integer>> microResult=mvResult.getResultFiles();
        for(Microservice microservice:microservices1){
            //String mname=microservice.getAllPath();
            String name=microservice.getElementName();
            Set<String> cochangeSet=new HashSet<>();//与当前遍历到的微服务共同变更次数超过cochange次的微服务集合
            double inValue=0.0;
            if(inElement.getValueMap().get(name)!=null){
                inValue=inElement.getValueMap().get(name);
            }
            if(inValue>impact){
                Map<String, Integer> cochangeMap=microResult.computeIfAbsent(name,k -> new HashMap<>());
                if(cochangeMap.size()>0){
                    for(String key:cochangeMap.keySet()){
                        if(cochangeMap.get(key)>cochange){
                            cochangeSet.add(key);//共同变更次数超过cochange的微服务名的集合
                        }
                    }
                    if(cochangeSet.size()>change){
                        resultDetail.put(name,cochangeSet);//每个微服务的满足ui的微服务集合
                    }
                }
            }
        }
        for(String key:resultDetail.keySet()){
            int value=resultDetail.get(key).size();
            element.put(key,Double.valueOf(value));
        }
        return element;
    }


    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        return null;
    }
}

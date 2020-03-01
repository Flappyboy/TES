package top.jach.tes.plugin.jhkt.arcsmell.hublink;

import org.apache.commons.compress.utils.Lists;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsInfo;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.info.value.StringInfo;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;

import java.util.*;

public class HublinkAction implements Action {
    public static final String Elements_INFO = "elements_info";
    public static final String PAIR_RELATIONS_INFO = "PairRelationsInfo";
    public static final String HUBLINK_IN_AND_OUT="hublinkElements";
    public static final String HUBLINK_IN="hublinkElements_e";
    public static final String HUBLINK__OUT="hublinkElements_s";


    public static final int MAX_NODE_COUNT = 20;
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
    //计算每个节点在各个集合中出现次数+排序+输出所有步骤抽取成一个方法
    public ElementsValue cal(HashMap<String,Double> nodes,HashMap<String,Double> allnodes,HashMap<String,Double> map,String flag){
        Set<String> nset=nodes.keySet();
        for(String key:nset){
            if(map.containsKey(key)){
                double tmp2=map.get(key)+nodes.get(key);
                map.put(key,tmp2);
            }
            else{
                map.put(key,nodes.get(key));
            }
        }

        //不存在该异味的微服务也要加上
        Set<String> allset=allnodes.keySet();
        for(String allkey:allset){
            if(!map.containsKey(allnodes.get(allkey))){
                map.put(allkey,0.0);
            }
        }
        Set set=map.entrySet();
        List<Map.Entry<String,Double>> list=new ArrayList<Map.Entry<String,Double>>(set);
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        ElementsValue element=ElementsValue.createInfo();
        element.setName(flag);
        for(Map.Entry<String,Double> entry:list){
            String key=entry.getKey();
            double value=entry.getValue();
            element.put(key,(double)value);
        }
        return element;
    }

//该方法根据元素和元素之间的关系，以此为参数调用方法，输出架构异味
    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        PairRelationsInfo pairRelationsInfo = inputInfos.getInfo(PAIR_RELATIONS_INFO, PairRelationsInfo.class);
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        HashMap<String, Double> nodes=new HashMap<>();//存储所有节点名
        HashMap<String, Double> sourceNodes=new HashMap<>();//存储开始节点名
        HashMap<String, Double> endNodes=new HashMap<>();//存储结束节点名
        for(PairRelation pr:relations){
            sourceNodes.put(pr.getSourceName(),pr.getValue());
            endNodes.put(pr.getTargetName(),pr.getValue());
            nodes.put(pr.getTargetName(),pr.getValue());
            nodes.put(pr.getSourceName(),pr.getValue());
        }

        HashMap allnodes=(HashMap) ((HashMap<String, Double>) nodes).clone();//赋值所有节点名，三个计算都需要用到
        //排序
        HashMap<String, Double> map = new HashMap<>();
        HashMap<String, Double> sourceMap = new HashMap<>();
        HashMap<String, Double> endMap = new HashMap<>();
        ElementsValue elementHublink=cal(nodes,allnodes,map,HUBLINK_IN_AND_OUT);
        ElementsValue elementHublink_s=cal(sourceNodes,allnodes,sourceMap,HUBLINK__OUT);
        ElementsValue elementHublink_e=cal(endNodes,allnodes,endMap,HUBLINK_IN);
        //return DefaultOutputInfos.WithSaveFlag(elementHublink,elementHublink_s,elementHublink_e);
        //输出,可删除
        Set set=elementHublink.getValue().entrySet();
        Set s_set=elementHublink_s.getValue().entrySet();
        Set e_set=elementHublink_e.getValue().entrySet();
        List<Map.Entry<String,Double>> list=new ArrayList<Map.Entry<String,Double>>(set);
        List<Map.Entry<String,Double>> s_list=new ArrayList<Map.Entry<String,Double>>(s_set);
        List<Map.Entry<String,Double>> e_list=new ArrayList<Map.Entry<String,Double>>(e_set);

        System.out.println("-------Results of hublink AS detecting---------");
        for(Map.Entry<String, Double> entry : list){
            String key=entry.getKey();
            double value=entry.getValue();
            System.out.println(key+"--"+value);
        }
        System.out.println("-------Results of hublink AS detecting_HublinkForIn---------");
        for(Map.Entry<String, Double> entry : e_list){
            String key=entry.getKey();
            double value=entry.getValue();
            System.out.println(key+"--"+value);
        }
        System.out.println("-------Results of hublink AS detecting_HublinkForOut---------");
        for(Map.Entry<String, Double> entry : s_list){
            String key=entry.getKey();
            double value=entry.getValue();
            System.out.println(key+"--"+value);
        }

        return DefaultOutputInfos.WithSaveFlag(elementHublink,elementHublink_s,elementHublink_e);

    }
}

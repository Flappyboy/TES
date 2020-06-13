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
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

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
    public static ElementsValue cal(List<String> nodes,List<Double> nodesValue,List<String> allnodes,HashMap<String,Double> map,String flag){
       // Set<String> nset=nodes.keySet();
        //////这个nodes是Map格式的，不允许同样的值存在，这个for循环相当于只是把nodes复制给map罢了
        for(int i=0;i<nodes.size();i++){
            if(map.containsKey(nodes.get(i))){
                double tmp2=map.get(nodes.get(i))+nodesValue.get(i);
                map.put(nodes.get(i),tmp2);
            }
            else{
                map.put(nodes.get(i),nodesValue.get(i));
            }
        }
         //就是下面这个for循环把所有的hubink值变成了0
        //不存在该异味的微服务也要加上
        for(int j=0;j<allnodes.size();j++){
            if(!map.containsKey(allnodes.get(j))){
                map.put(allnodes.get(j),0.0);
            }
        }
        Set set=map.entrySet();
        //为了使map能按照value值排序
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
    public static ElementsValue calculateHublike2(PairRelationsInfo pairRelationsInfo){
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        List<String> nodes=new ArrayList<>();
        List<Double> nodesValue=new ArrayList<>();
        Map<String, Double> hublikeMap = new HashMap();
        for(PairRelation pairRelation: pairRelationsInfo.getRelations()){
            String source = pairRelation.getSourceName();
            String target = pairRelation.getTargetName();
            hublikeMap.put(source,hublikeMap.computeIfAbsent(source, k -> 0d)+pairRelation.getValue());
            hublikeMap.put(target,hublikeMap.computeIfAbsent(target, k -> 0d)+pairRelation.getValue());
        }

        List<String> allnodes=new ArrayList<>(new ArrayList<>(nodes));
        HashMap<String, Double> map = new HashMap<>();
        return cal(nodes,nodesValue,allnodes,map,HUBLINK_IN_AND_OUT);
    }

    public static ElementsValue calculateHublike(PairRelationsInfo pairRelationsInfo){
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        List<String> nodes=new ArrayList<>();//存储所有节点名
        List<Double> nodesValue=new ArrayList<>();
        //map存储的不允许重复，而hublink就是为了计算重复次数，改成两个list同步记录节点名和对应的权重值
        //从而允许重复
        for(int i=0;i<relations.size();i++){
            nodes.add(relations.get(i).getSourceName());
            nodesValue.add(relations.get(i).getValue());
            nodes.add(relations.get(i).getTargetName());
            nodesValue.add(relations.get(i).getValue());
        }

        List<String> allnodes=new ArrayList<>(new ArrayList<>(nodes));
        HashMap<String, Double> map = new HashMap<>();
        return cal(nodes,nodesValue,allnodes,map,HUBLINK_IN_AND_OUT);
    }
    //static 方法不能跨包调用，要在别的包调用这个方法，就不能声明成static方法
    public ElementsValue calculateHublikeOut(PairRelationsInfo pairRelationsInfo){
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        List<String> nodes=new ArrayList<>();//存储所有节点名
        List<String> sourceNodes=new ArrayList<>();//存储开始节点名
        List<Double> nodesValue=new ArrayList<>();//存储节点对应权重
        for(int i=0;i<relations.size();i++){
            nodes.add(relations.get(i).getSourceName());
            nodes.add(relations.get(i).getTargetName());
            sourceNodes.add(relations.get(i).getSourceName());
            nodesValue.add(relations.get(i).getValue());
        }

       // List<String> allnodes=new ArrayList<>(new ArrayList<>(nodes));
        HashMap<String, Double> map = new HashMap<>();
        return cal(sourceNodes,nodesValue,nodes,map,HUBLINK__OUT);
    }

    public ElementsValue calculateHublikeIn(PairRelationsInfo pairRelationsInfo){
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        List<String> nodes=new ArrayList<>();//存储所有节点名
        List<String> endNodes=new ArrayList<>();//存储结束节点名
        List<Double> nodesValue=new ArrayList<>();//存储节点对应权重

        for(int i=0;i<relations.size();i++){
            nodes.add(relations.get(i).getSourceName());
            nodes.add(relations.get(i).getTargetName());
            endNodes.add(relations.get(i).getSourceName());
            nodesValue.add(relations.get(i).getValue());
        }

        // List<String> allnodes=new ArrayList<>(new ArrayList<>(nodes));
        HashMap<String, Double> map = new HashMap<>();
        return cal(endNodes,nodesValue,nodes,map,HUBLINK_IN);
    }


//该方法根据元素和元素之间的关系，以此为参数调用方法，输出架构异味
    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        PairRelationsInfo pairRelationsInfo = inputInfos.getInfo(PAIR_RELATIONS_INFO, PairRelationsInfo.class);
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        List<String> nodes=new ArrayList<>();//存储所有节点名
        List<String> sourceNodes=new ArrayList<>();//存储开始节点名
        List<String> endNodes=new ArrayList<>();//存储结束节点名
        List<Double> nodesValue=new ArrayList<>();
        List<Double> sourceNodesValue=new ArrayList<>();
        List<Double> endNodesValue=new ArrayList<>();
        /*HashMap<String, Double> nodes=new HashMap<>();
        HashMap<String, Double> sourceNodes=new HashMap<>();
        HashMap<String, Double> endNodes=new HashMap<>();//*/
        //map存储的不允许重复，而hublink就是为了计算重复次数，改成两个list同步记录节点名和对应的权重值
        //从而允许重复
        for(int i=0;i<relations.size();i++){
            sourceNodes.add(relations.get(i).getSourceName());
            sourceNodesValue.add(relations.get(i).getValue());
            endNodes.add(relations.get(i).getTargetName());
            endNodesValue.add(relations.get(i).getValue());
            nodes.add(relations.get(i).getSourceName());
            nodesValue.add(relations.get(i).getValue());
            nodes.add(relations.get(i).getTargetName());
            nodesValue.add(relations.get(i).getValue());
        }

        List<String> allnodes=new ArrayList<>(new ArrayList<>(nodes));
        //HashMap allnodes=(HashMap) ((HashMap<String, Double>) nodes).clone();//赋值所有节点名，三个计算都需要用到
        //排序
        HashMap<String, Double> map = new HashMap<>();
        HashMap<String, Double> sourceMap = new HashMap<>();
        HashMap<String, Double> endMap = new HashMap<>();
        ElementsValue elementHublink=cal(nodes,nodesValue,allnodes,map,HUBLINK_IN_AND_OUT);
        ElementsValue elementHublink_s=cal(sourceNodes,sourceNodesValue,allnodes,sourceMap,HUBLINK__OUT);
        ElementsValue elementHublink_e=cal(endNodes,endNodesValue,allnodes,endMap,HUBLINK_IN);
        //return DefaultOutputInfos.WithSaveFlag(elementHublink,elementHublink_s,elementHublink_e);
/*        //输出,可删除
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
        }*/

        return DefaultOutputInfos.WithSaveFlag(elementHublink,elementHublink_s,elementHublink_e);

    }
}

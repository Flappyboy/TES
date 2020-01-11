package top.jach.tes.plugin.jhkt.arcsmell.hublink;

import org.apache.commons.compress.utils.Lists;
import org.junit.Test;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.relation.PairRelation;

import java.util.*;

import static org.junit.Assert.*;

public class HublinkActionTest {
    public static final String HUBLINK_IN_AND_OUT="hublinkElements";
    public static final String HUBLINK_IN="hublinkElements_e";
    public static final String HUBLINK__OUT="hublinkElements_s";

    public ElementsValue cal(List<String> nodes,List<String> allnodes,HashMap<String,Double> map,String flag){
        for(int i=0;i<nodes.size();i++){
            if (map.containsKey(nodes.get(i))) {
                double temp2 = map.get(nodes.get(i));
                map.put(nodes.get(i), temp2 + 1.0);
            } else {
                map.put(nodes.get(i), 1.0);
            }
        }
        //不存在该异味的微服务也要加上
        for(int i=0;i<allnodes.size();i++){
            if(!map.containsKey(allnodes.get(i))){
                map.put(allnodes.get(i),0.0);
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

    @Test
    public void execute() {
        List<PairRelation> relations = Lists.newArrayList();//表示节点间关系
        relations.add(new PairRelation("0","1"));
        relations.add(new PairRelation("1","2"));
        relations.add(new PairRelation("2","3"));
        relations.add(new PairRelation("3","2"));
        relations.add(new PairRelation("3","4"));
        relations.add(new PairRelation("0","5"));
        relations.add(new PairRelation("5","6"));
        relations.add(new PairRelation("6","7"));
        relations.add(new PairRelation("7","8"));
        relations.add(new PairRelation("8","4"));
        relations.add(new PairRelation("4","6"));
        relations.add(new PairRelation("6","3"));
        relations.add(new PairRelation("6","1"));

/*        List<String> nodes=new ArrayList<String>();//存储节点名
        nodes.add("c");
        nodes.add("h");
        nodes.add("e");
        nodes.add("n");
        nodes.add("h");
        nodes.add("e");
        nodes.add("e");
        nodes.add("n");
        //排序
        HashMap<String, Double> map = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (map.containsKey(nodes.get(i))) {
                int temp = map.get(nodes.get(i));
                map.put(nodes.get(i), temp + 1);
            } else {
                map.put(nodes.get(i), 1);
            }
        }
        Set set=map.entrySet();
        List<Map.Entry<String,Double>> list=new ArrayList<Map.Entry<String,Double>>(set);
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        //输出
        for(Map.Entry<String, Double> entry : list){
            String key=entry.getKey();
            int value=entry.getValue();
            System.out.println(key+"---"+value);
        }*/
        /*List<String> sourceNodes=new ArrayList<String>();//存储开始节点名
        List<String> endNodes=new ArrayList<String>();//存储结束节点名
        for(PairRelation pr:relations){
            sourceNodes.add(pr.getSourceName());
            endNodes.add(pr.getTargetName());
        }*/
        List<String> nodes=new ArrayList<String>();//存储所有节点名
        List<String> sourceNodes=new ArrayList<String>();//存储开始节点名
        List<String> endNodes=new ArrayList<String>();//存储结束节点名
        for(PairRelation pr:relations){
            sourceNodes.add(pr.getSourceName());
            endNodes.add(pr.getTargetName());
            nodes.add(pr.getTargetName());
            nodes.add(pr.getSourceName());
        }
       // context.Logger().info("-------Results of hublink AS detecting_depend on others---------");
        /*//排序
        HashMap<String, Double> sourceMap = new HashMap<>();
        HashMap<String, Double> endMap = new HashMap<>();
        for (int i = 0; i < sourceNodes.size(); i++) {
            if (sourceMap.containsKey(sourceNodes.get(i))) {
                int temp1 = sourceMap.get(sourceNodes.get(i));
                sourceMap.put(sourceNodes.get(i), temp1 + 1);
            } else {
                sourceMap.put(sourceNodes.get(i), 1);
            }
            if (endMap.containsKey(endNodes.get(i))) {
                int temp2 = endMap.get(endNodes.get(i));
                endMap.put(endNodes.get(i), temp2 + 1);
            } else {
                endMap.put(endNodes.get(i), 1);
            }

        }*/
        List<String> allnodes=new ArrayList<String>(nodes);//赋值所有节点名
        //排序
        HashMap<String, Double> map = new HashMap<>();
        HashMap<String, Double> sourceMap = new HashMap<>();
        HashMap<String, Double> endMap = new HashMap<>();
        ElementsValue elementHublink=cal(nodes,allnodes,map,HUBLINK_IN_AND_OUT);
        ElementsValue elementHublink_s=cal(sourceNodes,allnodes,sourceMap,HUBLINK__OUT);
        ElementsValue elementHublink_e=cal(endNodes,allnodes,endMap,HUBLINK_IN);

        //输出
        Set set=elementHublink.getValue().entrySet();
        Set s_set=elementHublink_s.getValue().entrySet();
        Set e_set=elementHublink_e.getValue().entrySet();
        List<Map.Entry<String,Double>> list=new ArrayList<Map.Entry<String,Double>>(set);
        List<Map.Entry<String,Double>> s_list=new ArrayList<Map.Entry<String,Double>>(s_set);
        List<Map.Entry<String,Double>> e_list=new ArrayList<Map.Entry<String,Double>>(e_set);

        /*Set s_set=sourceMap.entrySet();
        Set e_set=endMap.entrySet();
        List<Map.Entry<String,Double>> s_list=new ArrayList<Map.Entry<String,Double>>(s_set);
        Collections.sort(s_list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<Map.Entry<String,Double>> e_list=new ArrayList<Map.Entry<String,Double>>(e_set);
        Collections.sort(e_list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));*/

        //输出

        System.out.println("-------Results of hublink AS detecting---------");
        for(Map.Entry<String, Double> entry : list){
            String key=entry.getKey();
            double value=entry.getValue();
            System.out.println(key+"---"+value);
        }
        System.out.println("-------Results of hublink AS detecting_depend on others---------");
        for(Map.Entry<String, Double> entry : s_list){
            String key=entry.getKey();
            double value=entry.getValue();
            System.out.println(key+"---"+value);
        }
       System.out.println("-------Results of hublink AS detecting_others depend on---------");
        for(Map.Entry<String, Double> entry : e_list){
            String key=entry.getKey();
            double value=entry.getValue();
            System.out.println(key+"---"+value);
        }

    }
}
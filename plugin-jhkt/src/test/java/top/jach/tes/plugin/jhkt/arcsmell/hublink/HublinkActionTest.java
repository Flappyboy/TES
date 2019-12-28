package top.jach.tes.plugin.jhkt.arcsmell.hublink;

import org.apache.commons.compress.utils.Lists;
import org.junit.Test;
import top.jach.tes.core.impl.domain.relation.PairRelation;

import java.util.*;

import static org.junit.Assert.*;

public class HublinkActionTest {

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
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (map.containsKey(nodes.get(i))) {
                int temp = map.get(nodes.get(i));
                map.put(nodes.get(i), temp + 1);
            } else {
                map.put(nodes.get(i), 1);
            }
        }
        Set set=map.entrySet();
        List<Map.Entry<String,Integer>> list=new ArrayList<Map.Entry<String,Integer>>(set);
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        //输出
        for(Map.Entry<String, Integer> entry : list){
            String key=entry.getKey();
            int value=entry.getValue();
            System.out.println(key+"---"+value);
        }*/
        List<String> sourceNodes=new ArrayList<String>();//存储开始节点名
        List<String> endNodes=new ArrayList<String>();//存储结束节点名
        for(PairRelation pr:relations){
            sourceNodes.add(pr.getSourceName());
            endNodes.add(pr.getTargetName());
        }
       // context.Logger().info("-------Results of hublink AS detecting_depend on others---------");
        //排序
        HashMap<String, Integer> sourceMap = new HashMap<>();
        HashMap<String, Integer> endMap = new HashMap<>();
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

        }


        Set s_set=sourceMap.entrySet();
        Set e_set=endMap.entrySet();
        List<Map.Entry<String,Integer>> s_list=new ArrayList<Map.Entry<String,Integer>>(s_set);
        Collections.sort(s_list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<Map.Entry<String,Integer>> e_list=new ArrayList<Map.Entry<String,Integer>>(e_set);
        Collections.sort(e_list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        //输出
        System.out.println("-------Results of hublink AS detecting_depend on others---------");
        for(Map.Entry<String, Integer> entry : s_list){
            String key=entry.getKey();
            int value=entry.getValue();
            System.out.println(key+"---"+value);
        }
       System.out.println("-------Results of hublink AS detecting_others depend on---------");
        for(Map.Entry<String, Integer> entry : e_list){
            String key=entry.getKey();
            int value=entry.getValue();
            System.out.println(key+"---"+value);
        }

    }
}
package top.jach.tes.plugin.jhkt.arcsmell.hublink;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class HublinkActionTest {

    @Test
    public void execute() {
        List<String> nodes=new ArrayList<String>();//存储节点名
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
        }

    }
}
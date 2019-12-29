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
    public static final String HUBLINK_IN="hublinkElements_s";
    public static final String HUBLINK_OUT="hublinkElements_e";


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


//该方法根据元素和元素之间的关系，以此为参数调用方法，输出架构异味
    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        PairRelationsInfo pairRelationsInfo = inputInfos.getInfo(PAIR_RELATIONS_INFO, PairRelationsInfo.class);
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
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
        //排序
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Integer> sourceMap = new HashMap<>();
        HashMap<String, Integer> endMap = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (map.containsKey(nodes.get(i))) {
                int temp1 = map.get(nodes.get(i));
                map.put(nodes.get(i), temp1 + 1);
            } else {
                map.put(nodes.get(i), 1);
            }
            if(i<sourceNodes.size()) {
                if (sourceMap.containsKey(sourceNodes.get(i))) {
                    int temp1 = sourceMap.get(sourceNodes.get(i));
                    sourceMap.put(sourceNodes.get(i), temp1 + 1);
                } else {
                    sourceMap.put(sourceNodes.get(i), 1);
                }
            }
            if(i<endNodes.size()) {
                if (endMap.containsKey(endNodes.get(i))) {
                    int temp2 = endMap.get(endNodes.get(i));
                    endMap.put(endNodes.get(i), temp2 + 1);
                } else {
                    endMap.put(endNodes.get(i), 1);
                }
            }
        }


        Set set=map.entrySet();
        Set s_set=sourceMap.entrySet();
        Set e_set=endMap.entrySet();
        List<Map.Entry<String,Integer>> list=new ArrayList<Map.Entry<String,Integer>>(set);
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<Map.Entry<String,Integer>> s_list=new ArrayList<Map.Entry<String,Integer>>(s_set);
        Collections.sort(s_list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<Map.Entry<String,Integer>> e_list=new ArrayList<Map.Entry<String,Integer>>(e_set);
        Collections.sort(e_list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        //输出
    /*    System.out.println("-------Results of hublink AS detecting_depend on others---------");
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
        }*/

        ElementsValue elementHublink=ElementsValue.createInfo();
        elementHublink.setName(HUBLINK_IN_AND_OUT);
        for(Map.Entry<String,Integer> entry:list){
            String key=entry.getKey();
            int value=entry.getValue();
            elementHublink.put(key,(double)value);
        }
        ElementsValue elementHublink_s=ElementsValue.createInfo();
        elementHublink_s.setName(HUBLINK_IN);
        for(Map.Entry<String,Integer> entry:s_list){
            String key=entry.getKey();
            int value=entry.getValue();
            elementHublink_s.put(key,(double)value);
        }
        ElementsValue elementHublink_e=ElementsValue.createInfo();
        elementHublink_e.setName(HUBLINK_OUT);
        for(Map.Entry<String,Integer> entry:e_list){
            String key=entry.getKey();
            int value=entry.getValue();
            elementHublink_e.put(key,(double)value);
        }
        return DefaultOutputInfos.WithSaveFlag(elementHublink,elementHublink_s,elementHublink_e);

    }
}

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
        List<String> nodes=new ArrayList<String>();//存储节点名
        for(PairRelation pr:relations){
            nodes.add(pr.getSourceName());
            nodes.add(pr.getTargetName());
        }
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
        // context.Logger().info("-------Results of hublink AS detecting---------");
        /*for(Map.Entry<String, Integer> entry : list){
            String key=entry.getKey();
            int value=entry.getValue();
            System.out.println(key+"---"+value);
        }*/
        ElementsValue elementCyclic = ElementsValue.createInfo();
        for(Map.Entry<String, Integer> entry : list){
            String key=entry.getKey();
            int value=entry.getValue();
            elementCyclic.put(key, (double) value);
        }
        return DefaultOutputInfos.WithSaveFlag(elementCyclic);
    }
}

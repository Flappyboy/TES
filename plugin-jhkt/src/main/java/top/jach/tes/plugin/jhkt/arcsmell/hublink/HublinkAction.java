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
    public static HashMap<String, Integer> findHublink(List<String> array){
        // 第一个值为出现的节点名，第二个值为出现的次数
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < array.size(); i++) {
            if (map.containsKey(array.get(i))) {
                int temp = map.get(array.get(i));
                map.put(array.get(i), temp + 1);
            } else {
                map.put(array.get(i), 1);
            }
        }
        //遍历hashmap，找出value值大于MAX_NODE_COUNT的key的列表
        List<String> keyList = new ArrayList();
        HashMap<String, Integer> resultMap = new HashMap<>();
        for(String getKey: map.keySet()){
            if(map.get(getKey)>=MAX_NODE_COUNT){
                resultMap.put(getKey,map.get(getKey));//记录出现次数大于100节点名及出现次数
            }
        }
        return resultMap;
    }

//该方法根据元素和元素之间的关系，以此为参数调用方法，输出架构异味
    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        //ElementsInfo<Element> elementsInfo = inputInfos.getInfo(Elements_INFO, ElementsInfo.class);
        PairRelationsInfo pairRelationsInfo = inputInfos.getInfo(PAIR_RELATIONS_INFO, PairRelationsInfo.class);
        //构建有向图
       // List<Element> nodess = Lists.newArrayList(elementsInfo.iterator());
        //List<String> elements=new ArrayList<String>();//存储节点名即可
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        List<String> nodes=new ArrayList<String>();//存储开始节点名
        //List<String> enodes=new ArrayList<String>();//存储结束节点名
        for(PairRelation pr:relations){
            nodes.add(pr.getSourceName());
            nodes.add(pr.getTargetName());
        }
       /* for(String str:nodes){
            context.Logger().info(str);
        }*/
        HashMap<String, Integer> results = findHublink(nodes);
        //输出键值对
        if(results.isEmpty()){
            context.Logger().info("no Hub-link dependence");
        }
        Set<Map.Entry<String,Integer>> set=results.entrySet();
        for(Map.Entry<String,Integer> entry:set){
            String key=entry.getKey();
            int value=entry.getValue();
            context.Logger().info(key+"--"+value);
        }

        return null;
    }
}

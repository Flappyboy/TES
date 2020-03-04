package top.jach.tes.plugin.jhkt.arcsmell.cyclic;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.compress.utils.Lists;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.Element;
import top.jach.tes.core.impl.domain.element.ElementsInfo;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.info.value.StringInfo;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.core.impl.domain.relation.PairRelation;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;

import javax.lang.model.util.Elements;
import java.util.*;

public class CyclicAction implements Action {
    public static final String Elements_INFO = "elements_info";
    public static final String PAIR_RELATIONS_INFO = "PairRelationsInfo";
    public static final int MAX_NODE_COUNT = 100;
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
    //寻找环回路信息
    private static void findCycle(int v,List<Integer> trace,List<String> result,List<String> nodes,int[][] matrix)
    {
        int j;
        //添加闭环信息
        if((j=trace.indexOf(v))!=-1) {
            StringBuffer sb = new StringBuffer();
            String startNode = nodes.get(trace.get(j));
            while(j<trace.size()) {
                sb.append(nodes.get(trace.get(j))+"-");//最终输出结果为a-b-c-a这样的形式
                j++;
            }
            result.add(sb.toString());//如果要打印出环回路，则sb.toString()前面加"cycle:"
            //若add了+startNode,在后面计数的时候节点出现times会重复
            return;
        }
        trace.add(v);
        for(int i=0;i<nodes.size();i++){
            if(matrix[v][i]==1) {
                findCycle(i,trace,result,nodes,matrix);
            }
        }
        trace.remove(trace.size()-1);
    }

//该方法根据元素和元素之间的关系，以此为参数调用方法，输出架构异味
    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        ElementsInfo<Element> elementsInfo = inputInfos.getInfo(Elements_INFO, ElementsInfo.class);
        PairRelationsInfo pairRelationsInfo = inputInfos.getInfo(PAIR_RELATIONS_INFO, PairRelationsInfo.class);
        //构建有向图
        List<Element> nodess = Lists.newArrayList(elementsInfo.iterator());
        List<String> elements=new ArrayList<String>();//存储节点名即可
        for(Element e:nodess){
            elements.add(e.getElementName());
        }
        List<PairRelation> relations = Lists.newArrayList(pairRelationsInfo.getRelations().iterator());
        int [][] matrix=new int[100][100];//有向图的邻接矩阵
        if(elements.size()>100){
            context.Logger().info("elements 超长");
        }
        else{
            //初始化矩阵
            for(PairRelation pr:relations){
                int startIndex=elements.indexOf(pr.getSourceName());
                int endIndex=elements.indexOf(pr.getTargetName());
                if(startIndex>=0&&endIndex>=0){
                    matrix[startIndex][endIndex]=1;
                }
            }

            // 从出发节点到当前节点的轨迹
            List<Integer> trace =new ArrayList<Integer>();
            //存储要打印输出的环回路
            List<String> result = new ArrayList<>();
            //存储要计数并输出的节点名
            List<HashSet<String>> quch=new ArrayList<HashSet<String>>();//泛型。最终输出里面那层String加上-
            List<String> output_res=new ArrayList<>();
            //存储要去重的环回路
            List<List<String>> huans=new ArrayList<List<String>>();
            if(matrix.length>0){
                findCycle(0,trace,result,elements,matrix);
            }
            //result中会存在依赖节点数相同但路径不同的环，比如2-3-2和3-2-3,节点元素都是2和3,其实同一个依赖环路
            context.Logger().info("-------Results of cyclic-dependence AS detecting---------");
            if(result.size()==0){
                result.add("no cycle dependence");
            }
            //遍历result里存储的每一个元素（也就是一个环），将元素按-切分成子字符串,并存入output_res中
            for(int i=0;i<result.size();i++){
                List<String> tmp=Arrays.asList(result.get(i).split("-"));//String[]转为List<String>
                if (!huans.contains(tmp)){//过滤掉存在含有的节点元素均相同的环路
                    huans.add(tmp);//主要是为了运用contains方法
                    HashSet<String> quch_res=new HashSet<>();
                    for (String str:tmp){
                        quch_res.add(str);//[2,3,2]变成[2,3]
                    }
                    if (!quch.contains(quch_res)){
                        quch.add(quch_res);
                        for(String outp:tmp){//若这个环是未曾出现过的，则将这个环的节点元素加入到输出里面去
                            output_res.add(outp);
                        }
                    }
                    else{
                        result.remove(i);
                    }
                }
                else{
                    result.remove(i);
                }
            }
            //打印输出环回路
            for (HashSet set : quch) {
                if(set.size()>=2){//去除自身依赖自身的情况
                    Iterator<String> iterator=set.iterator();
                    while(iterator.hasNext()){
                        System.out.print(iterator.next()+"-");
                    }
                    System.out.println("cycle");
                }
            }
            //得到节点出现次数的排序，并打印输出节点名与出现在环中的次数
            HashMap<String, Integer> map = new HashMap<>();
            for (int i = 0; i < output_res.size(); i++) {
                if (map.containsKey(output_res.get(i))) {
                    int temp = map.get(output_res.get(i));
                    map.put(output_res.get(i), temp + 1);
                } else {
                    map.put(output_res.get(i), 1);
                }
            }
            Set set=map.entrySet();
            List<Map.Entry<String,Integer>> list=new ArrayList<Map.Entry<String,Integer>>(set);
            Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            //输出节点名---出现次数键值对
            /*for(Map.Entry<String, Integer> entry : list){
                String key=entry.getKey();
                int value=entry.getValue();
                System.out.println("Node"+key+"--appears--"+value+"--times");
            }*/
            ElementsValue elementCyclic = ElementsValue.createInfo();
            for(Map.Entry<String, Integer> entry : list){
                String key=entry.getKey();
                int value=entry.getValue();
                elementCyclic.put(key, (double) value);
            }
            return DefaultOutputInfos.WithSaveFlag(elementCyclic);
        }
        return null;//暂时return null就可以了
    }
}

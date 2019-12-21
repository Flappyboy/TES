package top.jach.tes.plugin.jhkt.arcsmell.cyclic;

import org.apache.commons.compress.utils.Lists;
import org.junit.Test;
import top.jach.tes.core.impl.domain.relation.PairRelation;

import java.util.*;

import static org.junit.Assert.*;
//主要测试点：1、是否能正确打印出环回路；2、是否有重复环回路；
// 3、原先每个回路起点与终点一致，导致节点在环路出现次数会重复计数；4、自身构成的回路是否排除
public class CyclicActionTest {
//主要错误出在寻找环回路的那个方法：1、加上startNode则节点出现次数计数会重复计数；2、找到的环路有重复的，要去除重复环路
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
        List<String> elements=new ArrayList<>();//表示所有节点名
        elements.add("0");
        elements.add("1");
        elements.add("2");
        elements.add("3");
        elements.add("4");
        elements.add("5");
        elements.add("6");
        elements.add("7");
        elements.add("8");

        int [][] matrix=new int[100][100];//有向图的邻接矩阵
        if(elements.size()>100){
            System.out.println("elements 超长");
        }
        else{
            //初始化矩阵
            for(PairRelation pr:relations){
                int startIndex=elements.indexOf(pr.getSourceName());
                int endIndex=elements.indexOf(pr.getTargetName());
                // String gp="n";
                if(startIndex>=0&&endIndex>=0){
                    matrix[startIndex][endIndex]=1;
                }
            }
            // 从出发节点到当前节点的轨迹
            List<Integer> trace =new ArrayList<Integer>();
            //存储要打印输出的环回路
            List<String> result = new ArrayList<>();
            //存储要计数并输出的节点名
            List<HashSet<String>> quch=new ArrayList<HashSet<String>>();//最终输出里面那层String加上-
            List<String> output_res=new ArrayList<>();
            //存储要去重的环回路
            List<List<String>> huans=new ArrayList<List<String>>();
            if(matrix.length>0){
                findCycle(0,trace,result,elements,matrix);
            }
            //result中会存在依赖节点数相同但路径不同的环，比如2-3-2和3-2-3,节点元素都是2和3,其实同一个依赖环路
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
            for(Map.Entry<String, Integer> entry : list){
                String key=entry.getKey();
                int value=entry.getValue();
                System.out.println("Node"+key+"--appears--"+value+"--times");
            }

        }
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
}